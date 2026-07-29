package com.retirewise.scenarioengine.domain.formula

import com.retirewise.scenarioengine.domain.Scenario
import com.retirewise.scenarioengine.domain.ScenarioProjectionSummary

private const val MAX_COMPARISON_SCENARIOS = 3

data class ScenarioComparisonRow(val scenario: Scenario, val summary: ScenarioProjectionSummary)

data class ScenarioComparisonResult(
    val baseSummary: ScenarioProjectionSummary,
    val scenarioRows: List<ScenarioComparisonRow>,
)

/**
 * docs/RELEASE_PLAN.md Phase 7 items 5-6's data-side building block.
 * Enforces docs/PRD.md section 19.5's "no more than three scenarios" rule.
 * Each [Scenario] must already have a non-null
 * [Scenario.projectionSummary] (i.e. already run via
 * [createAndRunScenario]/[runScenario]) — this function does not run
 * anything itself, keeping it a pure, cheap comparison step.
 */
fun compareScenarios(
    baseSummary: ScenarioProjectionSummary,
    scenarios: List<Scenario>,
): ScenarioComparisonResult {
    require(scenarios.size <= MAX_COMPARISON_SCENARIOS) {
        "Cannot compare more than $MAX_COMPARISON_SCENARIOS scenarios " +
            "(docs/PRD.md section 19.5); got ${scenarios.size}."
    }
    val rows =
        scenarios.map { scenario ->
            val summary =
                requireNotNull(scenario.projectionSummary) {
                    "Scenario ${scenario.id.value} has not been run yet — call createAndRunScenario/runScenario first."
                }
            ScenarioComparisonRow(scenario, summary)
        }
    return ScenarioComparisonResult(baseSummary, rows)
}
