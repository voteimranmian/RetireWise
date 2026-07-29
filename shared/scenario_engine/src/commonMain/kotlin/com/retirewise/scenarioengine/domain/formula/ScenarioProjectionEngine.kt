package com.retirewise.scenarioengine.domain.formula

import com.retirewise.benefitsengine.domain.BenefitRuleRepository
import com.retirewise.benefitsengine.domain.CanadaBenefitRuleRepositoryV1
import com.retirewise.retirementengine.domain.ProjectionRequest
import com.retirewise.retirementengine.domain.VersionedProjection
import com.retirewise.retirementengine.domain.formula.project
import com.retirewise.scenarioengine.domain.PlanId
import com.retirewise.scenarioengine.domain.Scenario
import com.retirewise.scenarioengine.domain.ScenarioChangeSet
import com.retirewise.scenarioengine.domain.ScenarioId
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

/** Bumped whenever scenario-application or summary-derivation behavior changes. */
const val SCENARIO_ENGINE_VERSION = "SCENARIO_ENGINE_V1"

/**
 * docs/RELEASE_PLAN.md Phase 7 item 4 ("Run calculations"). Runs one
 * scenario's changed plan through the existing, unmodified retirement
 * engine — no retirement math is reimplemented here.
 */
fun runScenario(
    base: ProjectionRequest.Ready,
    change: ScenarioChangeSet,
    calculationDate: LocalDate,
    benefitRuleRepository: BenefitRuleRepository = CanadaBenefitRuleRepositoryV1,
): VersionedProjection = project(applyScenarioChange(base, change), calculationDate, benefitRuleRepository)

/**
 * Composes clone -> apply -> run -> summarize (docs/RELEASE_PLAN.md Phase 7
 * items 2-5) into a fully populated [Scenario]. The only new "math" here is
 * summary derivation ([summarizeProjection]); the projection itself is
 * entirely [com.retirewise.retirementengine.domain.formula.project]'s.
 */
fun createAndRunScenario(
    id: ScenarioId,
    name: String,
    basePlanId: PlanId,
    base: ProjectionRequest.Ready,
    change: ScenarioChangeSet,
    calculationDate: LocalDate,
    createdAt: Instant,
    benefitRuleRepository: BenefitRuleRepository = CanadaBenefitRuleRepositoryV1,
): Scenario {
    val changedRequest = applyScenarioChange(base, change)
    val projection = project(changedRequest, calculationDate, benefitRuleRepository)
    return Scenario(
        id = id,
        name = name,
        basePlanId = basePlanId,
        changedAssumptions = change,
        calculationVersion = projection.metadata.engineVersion,
        createdAt = createdAt,
        projectionSummary = summarizeProjection(changedRequest, projection),
    )
}
