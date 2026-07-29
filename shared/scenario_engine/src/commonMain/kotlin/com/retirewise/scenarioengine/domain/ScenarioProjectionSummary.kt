package com.retirewise.scenarioengine.domain

import com.retirewise.core.value.Money
import com.retirewise.retirementengine.domain.ProjectionValue

/**
 * One field per docs/PRD.md section 19.5 comparison-screen metric that this
 * engine can actually compute today, using the same
 * [ProjectionValue] `Known`/`NotYetModeled` honesty pattern established in
 * shared/retirement_engine for fields it cannot yet compute. See
 * docs/ADR/0008-scenario-planning-scope-and-comparison-metrics-for-phase-7.md
 * for the full scope decision.
 */
data class ScenarioProjectionSummary(
    val retirementAge: Int,
    val netWorthAtAge80: ProjectionValue,
    val netWorthAtAge90: ProjectionValue,
    val estimatedEstateAtProjectionEnd: Money,
    val governmentBenefitsAtRetirement: ProjectionValue,
    val isSustainableThroughProjectionEnd: Boolean,
    /** Always [ProjectionValue.NotYetModeled] this phase — no tax engine exists yet. */
    val monthlyAfterTaxIncomeAtRetirement: ProjectionValue,
    /** Always [ProjectionValue.NotYetModeled] this phase — no tax engine exists yet. */
    val lifetimeTaxesPaid: ProjectionValue,
    val mainAdvantage: ScenarioNarrative,
    val mainTradeoff: ScenarioNarrative,
    val keyRisk: ScenarioNarrative,
)

/**
 * Mirrors [ProjectionValue]'s honesty pattern for narrative text instead of
 * a dollar amount: never fabricate "advantage/tradeoff/risk" prose without
 * the AI advisor (Phase 8, docs/RELEASE_PLAN.md) actually generating it.
 */
sealed interface ScenarioNarrative {
    data class Generated(val text: String) : ScenarioNarrative

    data class NotYetGenerated(val reason: String) : ScenarioNarrative
}
