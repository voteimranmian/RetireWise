package com.retirewise.retirementengine.domain.formula

import com.retirewise.benefitsengine.domain.CanadaBenefitRuleRepositoryV1
import com.retirewise.benefitsengine.domain.formula.cppAdjustedAnnualAmount
import com.retirewise.benefitsengine.domain.formula.oasAdjustedAnnualAmount
import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import com.retirewise.retirementengine.domain.AccountBalances
import com.retirewise.retirementengine.domain.Assumptions
import com.retirewise.retirementengine.domain.MaritalStatus
import com.retirewise.retirementengine.domain.ProjectionRequest
import com.retirewise.retirementengine.domain.ProjectionValue
import com.retirewise.retirementengine.domain.VersionedProjection
import kotlinx.datetime.LocalDate
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val CALCULATION_DATE = LocalDate(2026, 1, 1)
private const val ITERATIONS = 300
private const val SEED = 1234L

private fun VersionedProjection.finalNetWorth(): Money = entries.last().netWorth

private fun randomMoney(
    random: Random,
    minDollars: Double,
    maxDollars: Double,
): Money = Money.ofDollars(minDollars + random.nextDouble() * (maxDollars - minDollars))

private fun randomRate(
    random: Random,
    minPercent: Double,
    maxPercent: Double,
): Rate = Rate.ofPercent(minPercent + random.nextDouble() * (maxPercent - minPercent))

/**
 * Pension/other retirement income is deliberately left at zero (the
 * [Assumptions] default) for every generated scenario: the retirement-delay
 * invariant below compares two different [ProjectionRequest.Ready.retirementAge]
 * values, and an indexed pension's amount depends on years-since-retirement,
 * which would confound that comparison with an unrelated effect.
 *
 * CPP/OAS estimates and start ages are included for roughly half of the
 * generated scenarios, so the reconciliation invariant below actually
 * exercises the government-benefits path. This is safe alongside the other
 * invariants: `cppStartAge`/`oasStartAge` are independent of
 * [ProjectionRequest.Ready.retirementAge], so they stay identical between the
 * two variants each of those invariants compares.
 */
private fun randomBaseRequest(random: Random): ProjectionRequest.Ready {
    val currentAge = random.nextInt(30, 56)
    val retirementAge = random.nextInt(currentAge + 5, 81)
    val includesGovernmentBenefits = random.nextBoolean()
    return ProjectionRequest.Ready(
        currentAge = currentAge,
        retirementAge = retirementAge,
        province = null,
        maritalStatus = MaritalStatus.SINGLE,
        employmentIncome = randomMoney(random, 20000.0, 150000.0),
        startingBalances = AccountBalances(rrspOrRrif = randomMoney(random, 0.0, 500000.0)),
        employeeAnnualContribution = randomMoney(random, 0.0, 20000.0),
        expectedDebtAtRetirement = randomMoney(random, 0.0, 100000.0),
        targetAnnualSpending = randomMoney(random, 10000.0, 80000.0),
        assumptions =
            Assumptions(
                incomeGrowthRate = randomRate(random, 0.0, 4.0),
                expectedReturnRate = randomRate(random, 2.0, 8.0),
                inflationRate = randomRate(random, 0.0, 4.0),
                assumptionSetVersion = "PROPERTY_TEST_V1",
                projectionEndAge = 92,
                cppStartAge = if (includesGovernmentBenefits) random.nextInt(60, 71) else null,
                oasStartAge = if (includesGovernmentBenefits) random.nextInt(65, 71) else null,
                estimatedCppAmountAtAge65 =
                    if (includesGovernmentBenefits) randomMoney(random, 1000.0, 20000.0) else null,
                estimatedOasAmountAtAge65 =
                    if (includesGovernmentBenefits) randomMoney(random, 1000.0, 15000.0) else null,
            ),
    )
}

/**
 * Hand-rolled seeded property-based checks (docs/TEST_STRATEGY.md section
 * 24.3) rather than adding a property-testing dependency for 5 invariants —
 * no such library exists in the version catalogue (see plan notes / ADR
 * discussion). 300 iterations of a fixed-seed [Random] make failures
 * reproducible.
 */
class RetirementProjectionInvariantsTest {
    @Test
    fun increasingSpendingNeverImprovesFinalNetWorth() {
        val random = Random(SEED)
        repeat(ITERATIONS) {
            val base = randomBaseRequest(random)
            val higherSpending = base.copy(targetAnnualSpending = base.targetAnnualSpending + Money.ofDollars(5000.0))

            val baseNetWorth = project(base, CALCULATION_DATE).finalNetWorth()
            val higherSpendingNetWorth = project(higherSpending, CALCULATION_DATE).finalNetWorth()

            assertTrue(higherSpendingNetWorth <= baseNetWorth)
        }
    }

    @Test
    fun increasingContributionsNeverReducesFinalNetWorth() {
        val random = Random(SEED)
        repeat(ITERATIONS) {
            val base = randomBaseRequest(random)
            val higherContribution =
                base.copy(employeeAnnualContribution = base.employeeAnnualContribution + Money.ofDollars(2000.0))

            val baseNetWorth = project(base, CALCULATION_DATE).finalNetWorth()
            val higherContributionNetWorth = project(higherContribution, CALCULATION_DATE).finalNetWorth()

            assertTrue(higherContributionNetWorth >= baseNetWorth)
        }
    }

    @Test
    fun delayingRetirementNeverWorsensFinalNetWorth() {
        val random = Random(SEED)
        repeat(ITERATIONS) {
            val base = randomBaseRequest(random)
            val delayedRetirementAge = (base.retirementAge + 2).coerceAtMost(base.assumptions.projectionEndAge - 1)
            val delayed = base.copy(retirementAge = delayedRetirementAge)

            val baseNetWorth = project(base, CALCULATION_DATE).finalNetWorth()
            val delayedNetWorth = project(delayed, CALCULATION_DATE).finalNetWorth()

            assertTrue(delayedNetWorth >= baseNetWorth)
        }
    }

    @Test
    fun accountBalancesNeverGoNegative() {
        val random = Random(SEED)
        repeat(ITERATIONS) {
            val projection = project(randomBaseRequest(random), CALCULATION_DATE)

            for (entry in projection.entries) {
                assertTrue(entry.rrspOrRrifBalance >= Money.ZERO)
                assertTrue(entry.tfsaBalance >= Money.ZERO)
                assertTrue(entry.nonRegisteredBalance >= Money.ZERO)
            }
        }
    }

    @Test
    fun knownIncomeComponentsReconcileWithSavingsSurplusOrDeficit() {
        val random = Random(SEED)
        repeat(ITERATIONS) {
            val projection = project(randomBaseRequest(random), CALCULATION_DATE)

            for (entry in projection.entries) {
                val governmentBenefits = (entry.governmentBenefits as? ProjectionValue.Known)?.amount ?: Money.ZERO
                val expectedSurplus =
                    entry.employmentIncome + entry.pensionIncome + entry.otherIncome +
                        governmentBenefits + entry.accountWithdrawals - entry.expenses
                assertEquals(expectedSurplus, entry.savingsSurplusOrDeficit)
            }
        }
    }

    @Test
    fun deferringCppOrOasStartAgeNeverDecreasesTheAdjustedAnnualBenefit() {
        val random = Random(SEED)
        val ruleSet = CanadaBenefitRuleRepositoryV1.current()

        repeat(ITERATIONS) {
            val estimate = randomMoney(random, 1000.0, 20000.0)
            val earlierAge = random.nextInt(ruleSet.cppMinStartAge, ruleSet.cppMaxStartAge)
            val laterAge = random.nextInt(earlierAge + 1, ruleSet.cppMaxStartAge + 1)

            val earlierCpp = cppAdjustedAnnualAmount(estimate, earlierAge, ruleSet)
            val laterCpp = cppAdjustedAnnualAmount(estimate, laterAge, ruleSet)
            assertTrue(laterCpp >= earlierCpp)

            val earlierOasAge = earlierAge.coerceAtLeast(ruleSet.oasStandardAge)
            val laterOasAge = laterAge.coerceAtLeast(ruleSet.oasStandardAge)
            val earlierOas = oasAdjustedAnnualAmount(estimate, earlierOasAge, ruleSet)
            val laterOas = oasAdjustedAnnualAmount(estimate, laterOasAge, ruleSet)
            assertTrue(laterOas >= earlierOas)
        }
    }
}
