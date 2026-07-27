package com.retirewise.retirementengine.domain.formula

import com.retirewise.benefitsengine.domain.BenefitRuleRepository
import com.retirewise.benefitsengine.domain.CanadaBenefitRuleRepositoryV1
import com.retirewise.benefitsengine.domain.GovernmentBenefitEstimate
import com.retirewise.benefitsengine.domain.formula.FORMULA_BENEFIT_CPP_ACTUARIAL_ADJUSTMENT_V1
import com.retirewise.benefitsengine.domain.formula.FORMULA_BENEFIT_GIS_LINEAR_APPROXIMATION_V1
import com.retirewise.benefitsengine.domain.formula.FORMULA_BENEFIT_OAS_DEFERRAL_V1
import com.retirewise.benefitsengine.domain.formula.cppAdjustedAnnualAmount
import com.retirewise.benefitsengine.domain.formula.gisAnnualAmount
import com.retirewise.benefitsengine.domain.formula.oasAdjustedAnnualAmount
import com.retirewise.core.value.Money
import com.retirewise.retirementengine.domain.AnnualProjectionEntry
import com.retirewise.retirementengine.domain.Assumptions
import com.retirewise.retirementengine.domain.CalculationMetadata
import com.retirewise.retirementengine.domain.PlanStatus
import com.retirewise.retirementengine.domain.ProjectionRequest
import com.retirewise.retirementengine.domain.ProjectionValue
import com.retirewise.retirementengine.domain.VersionedProjection
import kotlinx.datetime.LocalDate

/** Bumped whenever formula *behavior* changes (docs/FINANCIAL_RULES.md section 11.5). */
const val ENGINE_VERSION = "RETIREMENT_ENGINE_V1"

private val NOT_YET_MODELED_GOVERNMENT_BENEFITS =
    ProjectionValue.NotYetModeled(
        "Neither Assumptions.estimatedCppAmountAtAge65 nor estimatedOasAmountAtAge65 was supplied.",
    )
private val NOT_YET_MODELED_GROSS_INCOME =
    ProjectionValue.NotYetModeled("Requires income tax estimation to be complete.")
private val NOT_YET_MODELED_TAXES =
    ProjectionValue.NotYetModeled("Income tax estimation is not yet implemented.")
private val NOT_YET_MODELED_AFTER_TAX_INCOME =
    ProjectionValue.NotYetModeled("Requires tax estimation to be complete.")

private val FORMULA_IDENTIFIERS =
    setOf(
        FORMULA_CONTRIBUTION_FLAT_ANNUAL_V1,
        FORMULA_GROWTH_COMPOUND_ANNUAL_V1,
        FORMULA_INFLATION_CPI_INDEXED_V1,
        FORMULA_EXPENSE_INFLATED_RETIREMENT_SPENDING_V1,
        FORMULA_INCOME_EMPLOYMENT_GROWTH_V1,
        FORMULA_INCOME_PENSION_AND_OTHER_PASSTHROUGH_V1,
        FORMULA_WITHDRAWAL_SEQUENTIAL_NONREG_RRSP_TFSA_V1,
        FORMULA_BENEFIT_CPP_ACTUARIAL_ADJUSTMENT_V1,
        FORMULA_BENEFIT_OAS_DEFERRAL_V1,
        FORMULA_BENEFIT_GIS_LINEAR_APPROXIMATION_V1,
    )

/**
 * Computes this year's government benefit total from whichever of
 * [Assumptions.estimatedCppAmountAtAge65]/[Assumptions.estimatedOasAmountAtAge65]
 * the caller supplied. Returns `null` (rather than a partial estimate) only
 * when *neither* was supplied, so [AnnualProjectionEntry.governmentBenefits]
 * can fall back to [NOT_YET_MODELED_GOVERNMENT_BENEFITS]. GIS is only
 * included once OAS has actually started, matching the real-world rule that
 * GIS is paid only to OAS recipients.
 */
private fun governmentBenefitEstimateForYear(
    age: Int,
    assumptions: Assumptions,
    pensionIncome: Money,
    otherIncome: Money,
    benefitRuleRepository: BenefitRuleRepository,
): GovernmentBenefitEstimate? {
    val cppEstimate = assumptions.estimatedCppAmountAtAge65
    val oasEstimate = assumptions.estimatedOasAmountAtAge65
    if (cppEstimate == null && oasEstimate == null) return null

    val ruleSet = benefitRuleRepository.current()

    val cppAmount =
        if (cppEstimate != null && assumptions.cppStartAge != null && age >= assumptions.cppStartAge) {
            cppAdjustedAnnualAmount(cppEstimate, assumptions.cppStartAge, ruleSet)
        } else {
            Money.ZERO
        }

    val oasStarted = oasEstimate != null && assumptions.oasStartAge != null && age >= assumptions.oasStartAge
    val oasAmount =
        if (oasStarted) {
            oasAdjustedAnnualAmount(oasEstimate!!, assumptions.oasStartAge!!, ruleSet)
        } else {
            Money.ZERO
        }
    // Documented simplification: the GIS income test here excludes account
    // withdrawals (see docs/ADR/0007-government-benefit-scope-and-sourcing-for-phase-6.md).
    val gisAmount =
        if (oasStarted) {
            gisAnnualAmount(pensionIncome + otherIncome + cppAmount, ruleSet)
        } else {
            Money.ZERO
        }

    return GovernmentBenefitEstimate(cppAmount, oasAmount, gisAmount, ruleSet.ruleVersion)
}

/**
 * Runs the deterministic annual projection described in
 * docs/FINANCIAL_RULES.md section 11, from [ProjectionRequest.Ready.currentAge]
 * through [com.retirewise.retirementengine.domain.Assumptions.projectionEndAge]
 * inclusive. [calculationDate] anchors the calendar year of the first row
 * and is stamped into the returned [CalculationMetadata] for reproducibility.
 *
 * [benefitRuleRepository] is an explicit (defaulted) parameter rather than
 * an internal call so a specific historical rule-set version can be
 * re-supplied later, keeping old reports reproducible per
 * docs/FINANCIAL_RULES.md section 11.5.
 */
fun project(
    request: ProjectionRequest.Ready,
    calculationDate: LocalDate,
    benefitRuleRepository: BenefitRuleRepository = CanadaBenefitRuleRepositoryV1,
): VersionedProjection {
    val assumptions = request.assumptions
    var balances = request.startingBalances
    var planStatus = PlanStatus.FUNDED
    val entries = mutableListOf<AnnualProjectionEntry>()

    for (age in request.currentAge..assumptions.projectionEndAge) {
        val yearsFromStart = age - request.currentAge
        val isWorkingYear = age < request.retirementAge

        balances =
            applyContributions(
                balances = balances,
                employeeContribution = if (isWorkingYear) request.employeeAnnualContribution else Money.ZERO,
                employerContribution = if (isWorkingYear) assumptions.employerAnnualContribution else Money.ZERO,
                isWorkingYear = isWorkingYear,
            )
        balances = applyGrowth(balances, assumptions.expectedReturnRate)

        val employmentIncome =
            employmentIncomeForYear(
                baseAnnualIncome = request.employmentIncome,
                incomeGrowthRate = assumptions.incomeGrowthRate,
                age = age,
                retirementAge = request.retirementAge,
                yearsFromStart = yearsFromStart,
            )
        val pensionIncome =
            retirementIncomeForYear(
                baseAnnualAmount = assumptions.pensionIncomeAtRetirement,
                indexedToInflation = assumptions.pensionIndexedToInflation,
                inflationRate = assumptions.inflationRate,
                age = age,
                retirementAge = request.retirementAge,
            )
        val otherIncome =
            retirementIncomeForYear(
                baseAnnualAmount = assumptions.otherRetirementIncome,
                indexedToInflation = false,
                inflationRate = assumptions.inflationRate,
                age = age,
                retirementAge = request.retirementAge,
            )
        val expenses =
            retirementExpensesForYear(
                baseAnnualSpending = request.targetAnnualSpending,
                inflationRate = assumptions.inflationRate,
                age = age,
                retirementAge = request.retirementAge,
                yearsFromStart = yearsFromStart,
            )

        val benefitEstimate =
            governmentBenefitEstimateForYear(
                age = age,
                assumptions = assumptions,
                pensionIncome = pensionIncome,
                otherIncome = otherIncome,
                benefitRuleRepository = benefitRuleRepository,
            )
        val governmentBenefitsTotal = benefitEstimate?.totalAnnual ?: Money.ZERO

        val knownIncome = employmentIncome + pensionIncome + otherIncome + governmentBenefitsTotal
        val surplusBeforeWithdrawal = knownIncome - expenses

        var accountWithdrawals = Money.ZERO
        if (surplusBeforeWithdrawal < Money.ZERO) {
            val withdrawal = withdrawToCoverDeficit(balances, -surplusBeforeWithdrawal)
            balances = withdrawal.balances
            accountWithdrawals = withdrawal.amountWithdrawn
            if (withdrawal.unmetShortfall > Money.ZERO) {
                planStatus = PlanStatus.DEPLETED
            }
        }

        val savingsSurplusOrDeficit = knownIncome + accountWithdrawals - expenses
        val debtBalance = request.expectedDebtAtRetirement
        val netWorth = balances.total - debtBalance
        // Floored at zero: an estate cannot inherit debt beyond its assets in
        // this MVP model — a documented simplification alongside the flat-
        // carried debt balance.
        val estateValue = netWorth.coerceAtLeast(Money.ZERO)

        entries +=
            AnnualProjectionEntry(
                age = age,
                calendarYear = calculationDate.year + yearsFromStart,
                employmentIncome = employmentIncome,
                governmentBenefits =
                    benefitEstimate?.let { ProjectionValue.Known(it.totalAnnual) }
                        ?: NOT_YET_MODELED_GOVERNMENT_BENEFITS,
                pensionIncome = pensionIncome,
                accountWithdrawals = accountWithdrawals,
                otherIncome = otherIncome,
                grossIncome = NOT_YET_MODELED_GROSS_INCOME,
                estimatedTaxes = NOT_YET_MODELED_TAXES,
                afterTaxIncome = NOT_YET_MODELED_AFTER_TAX_INCOME,
                expenses = expenses,
                savingsSurplusOrDeficit = savingsSurplusOrDeficit,
                rrspOrRrifBalance = balances.rrspOrRrif,
                tfsaBalance = balances.tfsa,
                nonRegisteredBalance = balances.nonRegistered,
                debtBalance = debtBalance,
                netWorth = netWorth,
                estateValue = estateValue,
                planStatus = planStatus,
            )
    }

    return VersionedProjection(
        entries = entries,
        metadata =
            CalculationMetadata(
                engineVersion = ENGINE_VERSION,
                assumptionSetVersion = assumptions.assumptionSetVersion,
                calculationDate = calculationDate,
                formulaIdentifiers = FORMULA_IDENTIFIERS,
                governmentBenefitRuleVersion = benefitRuleRepository.current().ruleVersion,
            ),
    )
}
