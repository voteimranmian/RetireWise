package com.retirewise.scenariocomparison.domain

import com.retirewise.scenarioengine.domain.PlanId
import com.retirewise.scenarioengine.domain.Scenario
import com.retirewise.scenarioengine.domain.ScenarioChangeSet
import com.retirewise.scenarioengine.domain.ScenarioId
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ScenarioBuilderStateTest {
    private fun scenario(id: String) =
        Scenario(
            id = ScenarioId(id),
            name = id,
            basePlanId = PlanId("current-plan"),
            changedAssumptions = ScenarioChangeSet(retirementAge = 60),
            calculationVersion = "SCENARIO_ENGINE_V1",
            createdAt = Instant.fromEpochMilliseconds(0),
            projectionSummary = null,
        )

    @Test
    fun canAddScenarioIsTrueBelowTheCapAndFalseAtIt() {
        var state = ScenarioBuilderState()
        assertTrue(canAddScenario(state))

        state = addScenario(state, scenario("1"))
        assertTrue(canAddScenario(state))
        state = addScenario(state, scenario("2"))
        assertTrue(canAddScenario(state))
        state = addScenario(state, scenario("3"))
        assertFalse(canAddScenario(state))
        assertEquals(3, state.builtScenarios.size)
    }

    @Test
    fun addScenarioThrowsOnceCapIsReached() {
        var state = ScenarioBuilderState()
        repeat(MAX_BUILT_SCENARIOS) { state = addScenario(state, scenario(it.toString())) }

        assertFailsWith<IllegalStateException> { addScenario(state, scenario("overflow")) }
    }

    @Test
    fun addScenarioClosesTheConfiguringLever() {
        val state = ScenarioBuilderState(configuringLever = ScenarioLever.DELAY_CPP)
        val updated = addScenario(state, scenario("1"))
        assertEquals(null, updated.configuringLever)
    }

    @Test
    fun removeScenarioDropsOnlyTheMatchingId() {
        var state = ScenarioBuilderState()
        state = addScenario(state, scenario("1"))
        state = addScenario(state, scenario("2"))

        val updated = removeScenario(state, ScenarioId("1"))
        assertEquals(listOf(ScenarioId("2")), updated.builtScenarios.map { it.id })
    }

    @Test
    fun removeScenarioIsANoOpForAnUnknownId() {
        val state = addScenario(ScenarioBuilderState(), scenario("1"))
        val updated = removeScenario(state, ScenarioId("unknown"))
        assertEquals(state.builtScenarios, updated.builtScenarios)
    }
}
