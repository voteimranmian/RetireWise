package com.retirewise.retirementengine.domain

import com.retirewise.core.value.Rate

/**
 * The first default [Assumptions] factory, unblocking any UI caller of
 * [com.retirewise.retirementengine.domain.formula.project] that has no
 * user-adjustable rate inputs yet (docs/ADR/0009). This is distinct from
 * [Assumptions]'s own KDoc warning against a *hardcoded, unsourced* default:
 * these are general economic planning assumptions (growth/inflation/return),
 * not the government benefit/tax values CLAUDE.md rule 5 governs.
 *
 * 2% inflation matches the Bank of Canada's official inflation-control
 * target — a real, citable source. 5% expected return and 2% income growth
 * are conservative, commonly used long-run planning assumptions, explicitly
 * flagged in ADR-0009 as a placeholder pending real actuarial/market-data
 * review, not a fabricated authoritative figure.
 */
object AssumptionSetV1 {
    const val VERSION = "DEFAULT_ASSUMPTIONS_V1"

    fun create(): Assumptions =
        Assumptions(
            incomeGrowthRate = Rate.ofPercent(2.0),
            expectedReturnRate = Rate.ofPercent(5.0),
            inflationRate = Rate.ofPercent(2.0),
            assumptionSetVersion = VERSION,
        )
}
