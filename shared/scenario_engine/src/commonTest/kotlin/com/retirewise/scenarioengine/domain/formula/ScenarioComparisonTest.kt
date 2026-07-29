package com.retirewise.scenarioengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import com.retirewise.retirementengine.domain.AccountBalances
import com.retirewise.retirementengine.domain.Assumptions
import com.retirewise.retirementengine.domain.MaritalStatus
import com.retirewise.retirementengine.domain.ProjectionRequest
import com.retirewise.retirementengine.domain.formula.project
import com.retirewise.scenarioengine.domain.PlanId
import com.retirewise.scenarioengine.domain.Scenario
import com.retirewise.scenarioengine.domain.ScenarioChangeSet
import com.retirewise.scenarioengine.domain.ScenarioId
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private val CALCULATION_DATE = LocalDate(2026, 1, 1)
private val CREATED_AT = Instant.parse("2026-07-27T00:00:00Z")

private fun baseRequest(): ProjectionRequest.Ready =
    ProjectionRequest.Ready(
        currentAge = 40,
        retirementAge = 65,
        province = null,
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
                assumptionSetVersion = "SCENARIO_TEST_V1",
            ),
    )

private fun scenario(
    name: String,
    change: ScenarioChangeSet = ScenarioChangeSet(),
): Scenario =
    createAndRunScenario(
        id = ScenarioId(name),
        name = name,
        basePlanId = PlanId("plan-1"),
        base = baseRequest(),
        change = change,
        calculationDate = CALCULATION_DATE,
        createdAt = CREATED_AT,
    )

private fun baseSummary() = summarizeProjection(baseRequest(), project(baseRequest(), CALCULATION_DATE))

class ScenarioComparisonTest {
    @Test
    fun comparingExactlyThreeScenariosSucceeds() {
        val scenarios = listOf(scenario("A"), scenario("B"), scenario("C"))
        val result = compareScenarios(baseSummary(), scenarios)
        assertEquals(3, result.scenarioRows.size)
    }

    @Test
    fun comparingFewerThanThreeScenariosSucceeds() {
        assertEquals(0, compareScenarios(baseSummary(), emptyList()).scenarioRows.size)
        assertEquals(1, compareScenarios(baseSummary(), listOf(scenario("A"))).scenarioRows.size)
        assertEquals(2, compareScenarios(baseSummary(), listOf(scenario("A"), scenario("B"))).scenarioRows.size)
    }

    @Test
    fun comparingMoreThanThreeScenariosThrows() {
        val scenarios = listOf(scenario("A"), scenario("B"), scenario("C"), scenario("D"))
        assertFailsWith<IllegalArgumentException> {
            compareScenarios(baseSummary(), scenarios)
        }
    }

    @Test
    fun aScenarioWithoutAProjectionSummaryThrows() {
        val unrun =
            Scenario(
                id = ScenarioId("unrun"),
                name = "Unrun",
                basePlanId = PlanId("plan-1"),
                changedAssumptions = ScenarioChangeSet(),
                calculationVersion = null,
                createdAt = CREATED_AT,
                projectionSummary = null,
            )

        // requireNotNull throws IllegalArgumentException (not IllegalStateException).
        assertFailsWith<IllegalArgumentException> {
            compareScenarios(baseSummary(), listOf(unrun))
        }
    }
}
