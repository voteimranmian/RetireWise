package com.retirewise.scenarioengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import com.retirewise.retirementengine.domain.AccountBalances
import com.retirewise.retirementengine.domain.Assumptions
import com.retirewise.retirementengine.domain.MaritalStatus
import com.retirewise.retirementengine.domain.ProjectionRequest
import com.retirewise.retirementengine.domain.ProjectionValue
import com.retirewise.retirementengine.domain.formula.project
import com.retirewise.scenarioengine.domain.ScenarioNarrative
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

private val CALCULATION_DATE = LocalDate(2026, 1, 1)

private fun request(
    projectionEndAge: Int = 100,
    retirementAge: Int = 65,
    targetAnnualSpending: Money = Money.ofDollars(20000.0),
    estimatedCppAmountAtAge65: Money? = null,
    cppStartAge: Int? = null,
): ProjectionRequest.Ready =
    ProjectionRequest.Ready(
        currentAge = 40,
        retirementAge = retirementAge,
        province = null,
        maritalStatus = MaritalStatus.SINGLE,
        employmentIncome = Money.ofDollars(90000.0),
        startingBalances = AccountBalances(rrspOrRrif = Money.ofDollars(500000.0)),
        employeeAnnualContribution = Money.ofDollars(12000.0),
        expectedDebtAtRetirement = Money.ZERO,
        targetAnnualSpending = targetAnnualSpending,
        assumptions =
            Assumptions(
                incomeGrowthRate = Rate.ofPercent(2.0),
                expectedReturnRate = Rate.ofPercent(5.0),
                inflationRate = Rate.ofPercent(2.0),
                assumptionSetVersion = "SCENARIO_TEST_V1",
                projectionEndAge = projectionEndAge,
                estimatedCppAmountAtAge65 = estimatedCppAmountAtAge65,
                cppStartAge = cppStartAge,
            ),
    )

class ScenarioSummaryFormulasTest {
    @Test
    fun netWorthAt80And90AreKnownWhenBothAgesAreInRange() {
        val req = request()
        val summary = summarizeProjection(req, project(req, CALCULATION_DATE))

        assertIs<ProjectionValue.Known>(summary.netWorthAtAge80)
        assertIs<ProjectionValue.Known>(summary.netWorthAtAge90)
    }

    @Test
    fun netWorthAt90IsNotYetModeledWhenProjectionEndsBefore90() {
        val req = request(projectionEndAge = 85)
        val summary = summarizeProjection(req, project(req, CALCULATION_DATE))

        assertIs<ProjectionValue.Known>(summary.netWorthAtAge80)
        assertIs<ProjectionValue.NotYetModeled>(summary.netWorthAtAge90)
    }

    @Test
    fun governmentBenefitsAtRetirementIsNotYetModeledWhenNoEstimateSupplied() {
        val req = request()
        val summary = summarizeProjection(req, project(req, CALCULATION_DATE))

        assertIs<ProjectionValue.NotYetModeled>(summary.governmentBenefitsAtRetirement)
    }

    @Test
    fun governmentBenefitsAtRetirementIsKnownWhenEstimateAndStartAgeAreSupplied() {
        val req =
            request(
                estimatedCppAmountAtAge65 = Money.ofDollars(8000.0),
                cppStartAge = 65,
            )
        val summary = summarizeProjection(req, project(req, CALCULATION_DATE))

        assertIs<ProjectionValue.Known>(summary.governmentBenefitsAtRetirement)
    }

    @Test
    fun isSustainableThroughProjectionEndIsTrueForAComfortablyFundedScenario() {
        val req = request()
        val summary = summarizeProjection(req, project(req, CALCULATION_DATE))

        assertTrue(summary.isSustainableThroughProjectionEnd)
    }

    @Test
    fun isSustainableThroughProjectionEndIsFalseForAScenarioEngineeredToDeplete() {
        val req = request(targetAnnualSpending = Money.ofDollars(500000.0))
        val summary = summarizeProjection(req, project(req, CALCULATION_DATE))

        assertFalse(summary.isSustainableThroughProjectionEnd)
    }

    @Test
    fun taxAndNarrativeFieldsAreAlwaysUnmodeledThisPhase() {
        val req = request()
        val summary = summarizeProjection(req, project(req, CALCULATION_DATE))

        assertIs<ProjectionValue.NotYetModeled>(summary.monthlyAfterTaxIncomeAtRetirement)
        assertIs<ProjectionValue.NotYetModeled>(summary.lifetimeTaxesPaid)
        assertIs<ScenarioNarrative.NotYetGenerated>(summary.mainAdvantage)
        assertIs<ScenarioNarrative.NotYetGenerated>(summary.mainTradeoff)
        assertIs<ScenarioNarrative.NotYetGenerated>(summary.keyRisk)
    }

    @Test
    fun estimatedEstateAtProjectionEndEqualsTheLastEntrysEstateValue() {
        val req = request()
        val projection = project(req, CALCULATION_DATE)
        val summary = summarizeProjection(req, projection)

        assertEquals(projection.entries.last().estateValue, summary.estimatedEstateAtProjectionEnd)
    }
}
