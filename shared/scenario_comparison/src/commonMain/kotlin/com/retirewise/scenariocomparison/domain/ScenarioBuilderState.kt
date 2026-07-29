package com.retirewise.scenariocomparison.domain

import com.retirewise.retirementengine.domain.ProjectionRequest
import com.retirewise.scenarioengine.domain.Scenario
import com.retirewise.scenarioengine.domain.ScenarioId
import com.retirewise.scenarioengine.domain.ScenarioProjectionSummary

/** docs/PRD.md section 19.5: no more than three scenarios compared at once. */
const val MAX_BUILT_SCENARIOS = 3

/**
 * The Explore screen's in-progress state: the base plan (once loaded), the
 * scenarios the user has built and run so far, and which lever (if any) is
 * currently being configured via [ScenarioLeverInputForm]. Pure data plus
 * pure transition functions below, mirroring
 * [com.retirewise.onboarding.domain.OnboardingState]'s style so state
 * changes stay unit-testable without the Compose runtime.
 */
data class ScenarioBuilderState(
    val baseRequest: ProjectionRequest.Ready? = null,
    val baseSummary: ScenarioProjectionSummary? = null,
    val builtScenarios: List<Scenario> = emptyList(),
    val configuringLever: ScenarioLever? = null,
)

/** True while fewer than [MAX_BUILT_SCENARIOS] scenarios have been built. */
fun canAddScenario(state: ScenarioBuilderState): Boolean = state.builtScenarios.size < MAX_BUILT_SCENARIOS

/**
 * Appends [scenario] to [ScenarioBuilderState.builtScenarios] and closes the
 * lever-configuration form. Throws [IllegalStateException] if the cap has
 * already been reached — callers must check [canAddScenario] before
 * offering the "add scenario" affordance.
 */
fun addScenario(
    state: ScenarioBuilderState,
    scenario: Scenario,
): ScenarioBuilderState {
    check(canAddScenario(state)) { "Cannot add another scenario: $MAX_BUILT_SCENARIOS already built." }
    return state.copy(
        builtScenarios = state.builtScenarios + scenario,
        configuringLever = null,
    )
}

/** Removes the scenario with the given [id], if present. */
fun removeScenario(
    state: ScenarioBuilderState,
    id: ScenarioId,
): ScenarioBuilderState = state.copy(builtScenarios = state.builtScenarios.filterNot { it.id == id })
