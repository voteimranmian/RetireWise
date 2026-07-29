package com.retirewise.scenarioengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import com.retirewise.retirementengine.domain.AccountBalances
import com.retirewise.retirementengine.domain.Assumptions
import com.retirewise.retirementengine.domain.MaritalStatus
import com.retirewise.retirementengine.domain.ProjectionRequest
import com.retirewise.retirementengine.domain.formula.project
import com.retirewise.scenarioengine.domain.ScenarioChangeSet
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

private val CALCULATION_DATE = LocalDate(2026, 1, 1)

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

class ScenarioApplicationTest {
    @Test
    fun cloneBasePlanReturnsAStructurallyEqualRequest() {
        val base = baseRequest()
        assertEquals(base, cloneBasePlan(base))
    }

    @Test
    fun identityChangeSetProducesAnUnchangedRequestAndIdenticalProjection() {
        val base = baseRequest()

        assertEquals(base, applyScenarioChange(base, ScenarioChangeSet()))
        assertEquals(
            project(base, CALCULATION_DATE),
            runScenario(base, ScenarioChangeSet(), CALCULATION_DATE),
        )
    }

    @Test
    fun retirementAgeChangeOnlyAffectsRetirementAge() {
        val base = baseRequest()
        val changed = applyScenarioChange(base, ScenarioChangeSet(retirementAge = 62))
        assertEquals(base.copy(retirementAge = 62), changed)
    }

    @Test
    fun cppStartAgeChangeLandsOnNestedAssumptionsNotTopLevel() {
        val base = baseRequest()
        val changed = applyScenarioChange(base, ScenarioChangeSet(cppStartAge = 70))
        assertEquals(base.copy(assumptions = base.assumptions.copy(cppStartAge = 70)), changed)
    }

    @Test
    fun oasStartAgeChangeLandsOnNestedAssumptionsNotTopLevel() {
        val base = baseRequest()
        val changed = applyScenarioChange(base, ScenarioChangeSet(oasStartAge = 70))
        assertEquals(base.copy(assumptions = base.assumptions.copy(oasStartAge = 70)), changed)
    }

    @Test
    fun employeeAnnualContributionChangeOnlyAffectsThatField() {
        val base = baseRequest()
        val newContribution = Money.ofDollars(20000.0)
        val changed = applyScenarioChange(base, ScenarioChangeSet(employeeAnnualContribution = newContribution))
        assertEquals(base.copy(employeeAnnualContribution = newContribution), changed)
    }

    @Test
    fun targetAnnualSpendingChangeOnlyAffectsThatField() {
        val base = baseRequest()
        val newSpending = Money.ofDollars(30000.0)
        val changed = applyScenarioChange(base, ScenarioChangeSet(targetAnnualSpending = newSpending))
        assertEquals(base.copy(targetAnnualSpending = newSpending), changed)
    }

    @Test
    fun combinedLeversAllTakeEffectTogether() {
        val base = baseRequest()
        val change =
            ScenarioChangeSet(
                retirementAge = 68,
                employeeAnnualContribution = Money.ofDollars(15000.0),
            )
        val changed = applyScenarioChange(base, change)
        assertEquals(
            base.copy(retirementAge = 68, employeeAnnualContribution = Money.ofDollars(15000.0)),
            changed,
        )
    }
}
