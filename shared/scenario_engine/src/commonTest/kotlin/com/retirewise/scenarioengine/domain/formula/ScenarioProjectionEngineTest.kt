package com.retirewise.scenarioengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import com.retirewise.retirementengine.domain.AccountBalances
import com.retirewise.retirementengine.domain.Assumptions
import com.retirewise.retirementengine.domain.MaritalStatus
import com.retirewise.retirementengine.domain.ProjectionRequest
import com.retirewise.retirementengine.domain.formula.project
import com.retirewise.scenarioengine.domain.PlanId
import com.retirewise.scenarioengine.domain.ScenarioChangeSet
import com.retirewise.scenarioengine.domain.ScenarioId
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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

class ScenarioProjectionEngineTest {
    @Test
    fun runScenarioMatchesDirectProjectionOfTheAppliedRequest() {
        val base = baseRequest()
        val change = ScenarioChangeSet(retirementAge = 62)

        val viaScenario = runScenario(base, change, CALCULATION_DATE)
        val direct = project(applyScenarioChange(base, change), CALCULATION_DATE)

        assertEquals(direct, viaScenario)
    }

    @Test
    fun createAndRunScenarioPopulatesEveryFieldOfTheScenario() {
        val base = baseRequest()
        val change = ScenarioChangeSet(retirementAge = 62)
        val id = ScenarioId("scenario-1")
        val basePlanId = PlanId("plan-1")

        val scenario =
            createAndRunScenario(
                id = id,
                name = "Retire at 62",
                basePlanId = basePlanId,
                base = base,
                change = change,
                calculationDate = CALCULATION_DATE,
                createdAt = CREATED_AT,
            )

        assertEquals(id, scenario.id)
        assertEquals("Retire at 62", scenario.name)
        assertEquals(basePlanId, scenario.basePlanId)
        assertEquals(change, scenario.changedAssumptions)
        assertEquals(CREATED_AT, scenario.createdAt)

        val projection = runScenario(base, change, CALCULATION_DATE)
        assertEquals(projection.metadata.engineVersion, scenario.calculationVersion)

        assertNotNull(scenario.projectionSummary)
        assertEquals(62, scenario.projectionSummary?.retirementAge)
    }
}
