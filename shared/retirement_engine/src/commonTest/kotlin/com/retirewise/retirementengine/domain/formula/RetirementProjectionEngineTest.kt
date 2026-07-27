package com.retirewise.retirementengine.domain.formula

import com.retirewise.benefitsengine.domain.CanadaBenefitRuleRepositoryV1
import com.retirewise.benefitsengine.domain.formula.cppAdjustedAnnualAmount
import com.retirewise.benefitsengine.domain.formula.gisAnnualAmount
import com.retirewise.benefitsengine.domain.formula.oasAdjustedAnnualAmount
import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import com.retirewise.profile.domain.CanadianProvince
import com.retirewise.retirementengine.domain.AccountBalances
import com.retirewise.retirementengine.domain.Assumptions
import com.retirewise.retirementengine.domain.MaritalStatus
import com.retirewise.retirementengine.domain.PlanStatus
import com.retirewise.retirementengine.domain.ProjectionRequest
import com.retirewise.retirementengine.domain.ProjectionValue
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

private val CALCULATION_DATE = LocalDate(2026, 1, 1)

/**
 * Golden household integration tests (docs/TEST_STRATEGY.md section 24.2).
 * Covers 5 of 10 households feasible without couples modeling, business
 * income, real mortgage amortization, or a tax engine (needed for #8's OAS
 * recovery tax): #1, #3, #7, #9, #10.
 *
 * #7 and #10 assert their government-benefit expectations by calling the
 * same `benefits_engine` formula functions the production code calls,
 * rather than hand-derived cents — the formulas themselves already have
 * dedicated unit tests (`CppBenefitFormulasTest` etc.); these golden tests
 * exist to verify `RetirementProjectionEngine` wires them correctly.
 */
class RetirementProjectionEngineTest {
    @Test
    fun goldenHousehold1SingleEmployeeNoPensionStaysFundedThroughout() {
        // Overwhelmingly well-funded by design (safe withdrawal ratio far
        // below the sustainable-forever threshold), so FUNDED is guaranteed
        // for every year rather than depending on precise multi-decade
        // compounding.
        val request =
            ProjectionRequest.Ready(
                currentAge = 40,
                retirementAge = 65,
                province = CanadianProvince.ONTARIO,
                maritalStatus = MaritalStatus.SINGLE,
                employmentIncome = Money.ofDollars(90000.0),
                startingBalances = AccountBalances(rrspOrRrif = Money.ofDollars(500000.0)),
                employeeAnnualContribution = Money.ofDollars(12000.0),
                expectedDebtAtRetirement = Money.ZERO,
                targetAnnualSpending = Money.ofDollars(20000.0),
                assumptions =
                    Assumptions(
                        incomeGrowthRate = Rate.ofPercent(2.0),
                        expectedReturnRate = Rate.ofPercent(5.0),
                        inflationRate = Rate.ofPercent(2.0),
                        assumptionSetVersion = "GOLDEN_TEST_V1",
                        employerAnnualContribution = Money.ofDollars(6000.0),
                    ),
            )

        val projection = project(request, CALCULATION_DATE)

        assertEquals(61, projection.entries.size) // ages 40..100 inclusive
        val firstYear = projection.entries.first()
        assertEquals(40, firstYear.age)
        assertEquals(2026, firstYear.calendarYear)
        assertEquals(Money.ofDollars(90000.0), firstYear.employmentIncome)
        assertEquals(Money.ZERO, firstYear.expenses)
        // (500,000 + 12,000 + 6,000) * 1.05 = 543,900
        assertEquals(Money.ofDollars(543900.0), firstYear.rrspOrRrifBalance)
        assertEquals(PlanStatus.FUNDED, firstYear.planStatus)

        val retirementYear = projection.entries.single { it.age == 65 }
        assertEquals(Money.ZERO, retirementYear.employmentIncome)
        assertTrue(retirementYear.expenses > Money.ZERO)

        assertTrue(projection.entries.all { it.planStatus == PlanStatus.FUNDED })
        assertTrue(projection.entries.all { it.accountWithdrawals == Money.ZERO || it.age >= 65 })
    }

    @Test
    fun goldenHousehold3DefinedBenefitPensionIndexesFromRetirementNotPlanStart() {
        val request =
            ProjectionRequest.Ready(
                currentAge = 45,
                retirementAge = 60,
                province = CanadianProvince.ONTARIO,
                maritalStatus = MaritalStatus.SINGLE,
                employmentIncome = Money.ofDollars(70000.0),
                startingBalances = AccountBalances(rrspOrRrif = Money.ofDollars(50000.0)),
                employeeAnnualContribution = Money.ofDollars(5000.0),
                expectedDebtAtRetirement = Money.ZERO,
                targetAnnualSpending = Money.ofDollars(45000.0),
                assumptions =
                    Assumptions(
                        incomeGrowthRate = Rate.ofPercent(2.0),
                        expectedReturnRate = Rate.ofPercent(4.0),
                        inflationRate = Rate.ofPercent(2.0),
                        assumptionSetVersion = "GOLDEN_TEST_V1",
                        pensionIncomeAtRetirement = Money.ofDollars(40000.0),
                        pensionIndexedToInflation = true,
                        projectionEndAge = 90,
                    ),
            )

        val projection = project(request, CALCULATION_DATE)

        val atRetirement = projection.entries.single { it.age == 60 }
        // Pension is expressed as of retirement — must not be inflated by the
        // 15 years spent working before retirement.
        assertEquals(Money.ofDollars(40000.0), atRetirement.pensionIncome)
        assertEquals(Money.ZERO, atRetirement.employmentIncome)
        // Expenses (~$60.6k) exceed the pension alone ($40k), so a withdrawal
        // must occur in the retirement year itself.
        assertTrue(atRetirement.accountWithdrawals > Money.ZERO)

        val tenYearsIntoRetirement = projection.entries.single { it.age == 70 }
        assertEquals(
            inflateAmount(Money.ofDollars(40000.0), Rate.ofPercent(2.0), yearsFromStart = 10),
            tenYearsIntoRetirement.pensionIncome,
        )
    }

    @Test
    fun goldenHousehold9EarlyRetirementAt55EventuallyDepletes() {
        // Deliberately underfunded: 20 years of modest contributions cannot
        // sustain 35 years of decumulation at this spending level, exercising
        // the long-decumulation / DEPLETED path.
        val request =
            ProjectionRequest.Ready(
                currentAge = 35,
                retirementAge = 55,
                province = CanadianProvince.ONTARIO,
                maritalStatus = MaritalStatus.SINGLE,
                employmentIncome = Money.ofDollars(60000.0),
                startingBalances = AccountBalances(rrspOrRrif = Money.ofDollars(20000.0)),
                employeeAnnualContribution = Money.ofDollars(3000.0),
                expectedDebtAtRetirement = Money.ZERO,
                targetAnnualSpending = Money.ofDollars(50000.0),
                assumptions =
                    Assumptions(
                        incomeGrowthRate = Rate.ofPercent(2.0),
                        expectedReturnRate = Rate.ofPercent(4.0),
                        inflationRate = Rate.ofPercent(2.0),
                        assumptionSetVersion = "GOLDEN_TEST_V1",
                        projectionEndAge = 90,
                    ),
            )

        val projection = project(request, CALCULATION_DATE)

        val firstYear = projection.entries.first()
        assertEquals(Money.ofDollars(60000.0), firstYear.employmentIncome)
        // (20,000 + 3,000) * 1.04 = 23,920
        assertEquals(Money.ofDollars(23920.0), firstYear.rrspOrRrifBalance)
        assertEquals(PlanStatus.FUNDED, firstYear.planStatus)

        assertEquals(PlanStatus.DEPLETED, projection.entries.last().planStatus)
        assertTrue(projection.entries.all { it.rrspOrRrifBalance >= Money.ZERO })
        assertTrue(projection.entries.all { it.tfsaBalance >= Money.ZERO })
        assertTrue(projection.entries.all { it.nonRegisteredBalance >= Money.ZERO })

        // Once depleted, the status never recovers to FUNDED.
        val depletionIndex = projection.entries.indexOfFirst { it.planStatus == PlanStatus.DEPLETED }
        assertTrue(depletionIndex >= 0)
        assertTrue(projection.entries.drop(depletionIndex).all { it.planStatus == PlanStatus.DEPLETED })
    }

    @Test
    fun metadataRecordsEngineVersionAndExplicitNotImplementedTaxRuleVersion() {
        val request =
            ProjectionRequest.Ready(
                currentAge = 60,
                retirementAge = 65,
                province = null,
                maritalStatus = MaritalStatus.SINGLE,
                employmentIncome = Money.ofDollars(50000.0),
                startingBalances = AccountBalances(),
                employeeAnnualContribution = Money.ZERO,
                expectedDebtAtRetirement = Money.ZERO,
                targetAnnualSpending = Money.ofDollars(30000.0),
                assumptions =
                    Assumptions(
                        incomeGrowthRate = Rate.ZERO,
                        expectedReturnRate = Rate.ZERO,
                        inflationRate = Rate.ZERO,
                        assumptionSetVersion = "GOLDEN_TEST_V1",
                        projectionEndAge = 66,
                    ),
            )

        val projection = project(request, CALCULATION_DATE)

        assertEquals(ENGINE_VERSION, projection.metadata.engineVersion)
        assertEquals("GOLDEN_TEST_V1", projection.metadata.assumptionSetVersion)
        assertEquals(CALCULATION_DATE, projection.metadata.calculationDate)
        assertEquals("NOT_IMPLEMENTED", projection.metadata.taxRuleVersion)
        // Government benefits, unlike tax, now has a real (defaulted) rule
        // repository, so its rule version is always stamped — even when this
        // request supplies no CPP/OAS estimates and governmentBenefits stays
        // NotYetModeled for every entry.
        assertEquals("CANADA_BENEFITS_V1", projection.metadata.governmentBenefitRuleVersion)
        assertTrue(FORMULA_INCOME_EMPLOYMENT_GROWTH_V1 in projection.metadata.formulaIdentifiers)
        assertTrue(FORMULA_WITHDRAWAL_SEQUENTIAL_NONREG_RRSP_TFSA_V1 in projection.metadata.formulaIdentifiers)

        val entry = projection.entries.first()
        assertIs<ProjectionValue.NotYetModeled>(entry.governmentBenefits)
        assertIs<ProjectionValue.NotYetModeled>(entry.grossIncome)
        assertIs<ProjectionValue.NotYetModeled>(entry.estimatedTaxes)
        assertIs<ProjectionValue.NotYetModeled>(entry.afterTaxIncome)
    }

    @Test
    fun goldenHousehold7LowerIncomeGisEligibleRetireeReceivesGisTopUp() {
        val request =
            ProjectionRequest.Ready(
                currentAge = 63,
                retirementAge = 65,
                province = CanadianProvince.ONTARIO,
                maritalStatus = MaritalStatus.SINGLE,
                employmentIncome = Money.ofDollars(35000.0),
                startingBalances = AccountBalances(rrspOrRrif = Money.ofDollars(15000.0)),
                employeeAnnualContribution = Money.ofDollars(1000.0),
                expectedDebtAtRetirement = Money.ZERO,
                targetAnnualSpending = Money.ofDollars(18000.0),
                assumptions =
                    Assumptions(
                        incomeGrowthRate = Rate.ZERO,
                        expectedReturnRate = Rate.ofPercent(3.0),
                        inflationRate = Rate.ofPercent(2.0),
                        assumptionSetVersion = "GOLDEN_TEST_V1",
                        projectionEndAge = 67,
                        cppStartAge = 65,
                        oasStartAge = 65,
                        estimatedCppAmountAtAge65 = Money.ofDollars(3000.0),
                        estimatedOasAmountAtAge65 = Money.ofDollars(9000.0),
                    ),
            )

        val projection = project(request, CALCULATION_DATE)

        assertEquals("CANADA_BENEFITS_V1", projection.metadata.governmentBenefitRuleVersion)

        val ruleSet = CanadaBenefitRuleRepositoryV1.current()
        // Started at the standard age (65) => no actuarial adjustment.
        val expectedCpp = Money.ofDollars(3000.0)
        val expectedOas = Money.ofDollars(9000.0)
        val expectedGis = gisAnnualAmount(expectedCpp, ruleSet)
        val expectedTotal = expectedCpp + expectedOas + expectedGis

        val atRetirement = projection.entries.single { it.age == 65 }
        assertEquals(ProjectionValue.Known(expectedTotal), atRetirement.governmentBenefits)
        // A lower-income retiree well below the GIS income cutoff receives a
        // non-zero top-up, not just CPP + OAS.
        assertTrue(expectedGis > Money.ZERO)

        // Both estimates were supplied, so governmentBenefits is Known for
        // every year (matching household #10 below) — it's just $0 before
        // the benefit has actually started, not NotYetModeled.
        val beforeRetirement = projection.entries.single { it.age == 64 }
        assertEquals(ProjectionValue.Known(Money.ZERO), beforeRetirement.governmentBenefits)
    }

    @Test
    fun goldenHousehold10DelayedCppAndOasIncreasesBenefitOnceStarted() {
        val request =
            ProjectionRequest.Ready(
                currentAge = 63,
                retirementAge = 65,
                province = CanadianProvince.ONTARIO,
                maritalStatus = MaritalStatus.SINGLE,
                employmentIncome = Money.ofDollars(80000.0),
                startingBalances = AccountBalances(rrspOrRrif = Money.ofDollars(800000.0)),
                employeeAnnualContribution = Money.ofDollars(10000.0),
                expectedDebtAtRetirement = Money.ZERO,
                targetAnnualSpending = Money.ofDollars(30000.0),
                assumptions =
                    Assumptions(
                        incomeGrowthRate = Rate.ZERO,
                        expectedReturnRate = Rate.ofPercent(4.0),
                        inflationRate = Rate.ofPercent(2.0),
                        assumptionSetVersion = "GOLDEN_TEST_V1",
                        projectionEndAge = 75,
                        // Delayed as long as possible past the standard age (65).
                        cppStartAge = 70,
                        oasStartAge = 70,
                        estimatedCppAmountAtAge65 = Money.ofDollars(10000.0),
                        estimatedOasAmountAtAge65 = Money.ofDollars(9000.0),
                    ),
            )

        val projection = project(request, CALCULATION_DATE)

        // Benefits are known to be zero before the chosen start age — Known,
        // not NotYetModeled, since the engine does know the amount (zero,
        // because the user chose to delay).
        val beforeStart = projection.entries.single { it.age == 69 }
        assertEquals(ProjectionValue.Known(Money.ZERO), beforeStart.governmentBenefits)

        val ruleSet = CanadaBenefitRuleRepositoryV1.current()
        val expectedCpp = cppAdjustedAnnualAmount(Money.ofDollars(10000.0), startAge = 70, ruleSet)
        val expectedOas = oasAdjustedAnnualAmount(Money.ofDollars(9000.0), startAge = 70, ruleSet)
        val expectedGis = gisAnnualAmount(expectedCpp, ruleSet)
        val expectedTotal = expectedCpp + expectedOas + expectedGis

        val atStart = projection.entries.single { it.age == 70 }
        assertEquals(ProjectionValue.Known(expectedTotal), atStart.governmentBenefits)
        // Deferral must strictly increase both benefits relative to the
        // age-65 estimate supplied.
        assertTrue(expectedCpp > Money.ofDollars(10000.0))
        assertTrue(expectedOas > Money.ofDollars(9000.0))
    }
}
