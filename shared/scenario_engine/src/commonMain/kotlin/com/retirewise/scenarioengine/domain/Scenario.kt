package com.retirewise.scenarioengine.domain

import kotlinx.datetime.Instant

/**
 * Opaque, caller-supplied identifier for a [Scenario]. No uniqueness or
 * persistence guarantee is enforced at this layer.
 */
data class ScenarioId(val value: String)

/**
 * Opaque, caller-supplied identifier for the base plan a [Scenario] is
 * relative to. No persisted "Plan" entity exists anywhere in the codebase
 * yet (docs/DATA_MODEL.md has no such entity — the closest analogs,
 * `Profile`/`RetirementGoal`, are in-memory only per the Phase 4 status
 * note), so [PlanId] carries no referential-integrity guarantee this
 * phase — a documented gap, not an oversight (see
 * docs/ADR/0008-scenario-planning-scope-and-comparison-metrics-for-phase-7.md).
 */
data class PlanId(val value: String)

/**
 * docs/DATA_MODEL.md section 16.10's `Scenario` entity, mapped onto real
 * types. [changedAssumptions] is typed as [ScenarioChangeSet] — a
 * nullable-field patch over
 * [com.retirewise.retirementengine.domain.ProjectionRequest.Ready] — rather
 * than a full [com.retirewise.retirementengine.domain.Assumptions]
 * replacement, because the levers docs/PRD.md section 8.3 asks for
 * (retirement age, employee contribution, target spending) live on
 * `ProjectionRequest.Ready` itself, not on `Assumptions` alone. See ADR-0008
 * for the alternatives considered.
 *
 * [calculationVersion] and [projectionSummary] are null until the scenario
 * has actually been run via
 * [com.retirewise.scenarioengine.domain.formula.createAndRunScenario] /
 * [com.retirewise.scenarioengine.domain.formula.runScenario] — this mirrors
 * [com.retirewise.retirementengine.domain.ProjectionValue]'s honesty
 * pattern of never fabricating a value the engine hasn't actually computed.
 */
data class Scenario(
    val id: ScenarioId,
    val name: String,
    val basePlanId: PlanId,
    val changedAssumptions: ScenarioChangeSet,
    val calculationVersion: String?,
    val createdAt: Instant,
    val projectionSummary: ScenarioProjectionSummary?,
)
