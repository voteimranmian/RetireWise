package com.retirewise.scenarioengine.domain.formula

import com.retirewise.retirementengine.domain.AnnualProjectionEntry
import com.retirewise.retirementengine.domain.PlanStatus
import com.retirewise.retirementengine.domain.ProjectionRequest
import com.retirewise.retirementengine.domain.ProjectionValue
import com.retirewise.retirementengine.domain.VersionedProjection
import com.retirewise.scenarioengine.domain.ScenarioNarrative
import com.retirewise.scenarioengine.domain.ScenarioProjectionSummary

private const val NET_WORTH_COMPARISON_AGE_1 = 80
private const val NET_WORTH_COMPARISON_AGE_2 = 90

private val TAX_ENGINE_NOT_YET_MODELED =
    ProjectionValue.NotYetModeled(
        "Requires a tax engine, which does not exist yet " +
            "(docs/ADR/0008-scenario-planning-scope-and-comparison-metrics-for-phase-7.md).",
    )
private val NARRATIVE_NOT_YET_GENERATED =
    ScenarioNarrative.NotYetGenerated(
        "Requires the AI advisor (Phase 8, docs/RELEASE_PLAN.md).",
    )

private fun netWorthAtAge(
    entries: List<AnnualProjectionEntry>,
    age: Int,
): ProjectionValue =
    entries.firstOrNull { it.age == age }
        ?.let { ProjectionValue.Known(it.netWorth) }
        ?: ProjectionValue.NotYetModeled("Age $age falls outside this plan's projected age range.")

/**
 * docs/RELEASE_PLAN.md Phase 7 item 5's per-scenario building block:
 * summarizes one already-run [VersionedProjection] into the fields
 * docs/PRD.md section 19.5 asks a comparison screen to show, using this
 * phase's documented scope (see
 * docs/ADR/0008-scenario-planning-scope-and-comparison-metrics-for-phase-7.md).
 */
fun summarizeProjection(
    request: ProjectionRequest.Ready,
    projection: VersionedProjection,
): ScenarioProjectionSummary {
    val entries = projection.entries
    val retirementEntry = entries.firstOrNull { it.age == request.retirementAge }
    return ScenarioProjectionSummary(
        retirementAge = request.retirementAge,
        netWorthAtAge80 = netWorthAtAge(entries, NET_WORTH_COMPARISON_AGE_1),
        netWorthAtAge90 = netWorthAtAge(entries, NET_WORTH_COMPARISON_AGE_2),
        estimatedEstateAtProjectionEnd = entries.last().estateValue,
        governmentBenefitsAtRetirement =
            retirementEntry?.governmentBenefits
                ?: ProjectionValue.NotYetModeled("Retirement age falls outside this plan's projected age range."),
        // planStatus only ever transitions FUNDED -> DEPLETED and never resets
        // (see RetirementProjectionEngine's per-year loop), so checking the
        // last entry is equivalent to "never became depleted through any
        // prior year."
        isSustainableThroughProjectionEnd = entries.last().planStatus == PlanStatus.FUNDED,
        monthlyAfterTaxIncomeAtRetirement = TAX_ENGINE_NOT_YET_MODELED,
        lifetimeTaxesPaid = TAX_ENGINE_NOT_YET_MODELED,
        mainAdvantage = NARRATIVE_NOT_YET_GENERATED,
        mainTradeoff = NARRATIVE_NOT_YET_GENERATED,
        keyRisk = NARRATIVE_NOT_YET_GENERATED,
    )
}
