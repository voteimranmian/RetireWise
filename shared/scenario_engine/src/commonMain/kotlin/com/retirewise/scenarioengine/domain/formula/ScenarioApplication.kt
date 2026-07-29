package com.retirewise.scenarioengine.domain.formula

import com.retirewise.retirementengine.domain.ProjectionRequest
import com.retirewise.scenarioengine.domain.ScenarioChangeSet

/**
 * docs/RELEASE_PLAN.md Phase 7 item 2 ("Clone base plan"). Because
 * [ProjectionRequest.Ready] and every value it contains (`Money`, `Rate`,
 * `Assumptions`, `AccountBalances`) are immutable data classes, "cloning"
 * needs no deep-copy machinery — the base value is already safe to
 * reference and derive from via `.copy()`. This function exists only so
 * item 2 has a named, traceable implementation rather than being folded
 * silently into [applyScenarioChange].
 */
fun cloneBasePlan(base: ProjectionRequest.Ready): ProjectionRequest.Ready = base

/**
 * docs/RELEASE_PLAN.md Phase 7 item 3 ("Apply scenario changes"). Applies a
 * [ScenarioChangeSet] on top of a cloned base plan via `.copy()`. Fields
 * left null in [change] keep the base value. [ScenarioChangeSet.cppStartAge]
 * / [ScenarioChangeSet.oasStartAge] land on the nested `assumptions`, not
 * the top-level request, since that is where
 * [com.retirewise.retirementengine.domain.Assumptions] declares them.
 */
fun applyScenarioChange(
    base: ProjectionRequest.Ready,
    change: ScenarioChangeSet,
): ProjectionRequest.Ready {
    val cloned = cloneBasePlan(base)
    val updatedAssumptions =
        cloned.assumptions.copy(
            cppStartAge = change.cppStartAge ?: cloned.assumptions.cppStartAge,
            oasStartAge = change.oasStartAge ?: cloned.assumptions.oasStartAge,
        )
    return cloned.copy(
        retirementAge = change.retirementAge ?: cloned.retirementAge,
        employeeAnnualContribution = change.employeeAnnualContribution ?: cloned.employeeAnnualContribution,
        targetAnnualSpending = change.targetAnnualSpending ?: cloned.targetAnnualSpending,
        assumptions = updatedAssumptions,
    )
}
