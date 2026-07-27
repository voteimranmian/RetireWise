package com.retirewise.retirementengine.domain.formula

import com.retirewise.core.value.Rate
import com.retirewise.retirementengine.domain.AccountBalances

const val FORMULA_GROWTH_COMPOUND_ANNUAL_V1 = "GROWTH_COMPOUND_ANNUAL_V1"

/**
 * Applies one year of investment growth to every account bucket, using a
 * single blended [rate] across all three (per-account-type asset allocation
 * is a documented future enhancement, not modeled this phase).
 *
 * Ordering convention (must stay consistent for reproducibility per
 * docs/FINANCIAL_RULES.md section 11.5): this is applied *after*
 * [ContributionFormulas.applyContributions] for the same year, i.e. growth
 * compounds on the post-contribution balance.
 */
fun applyGrowth(
    balances: AccountBalances,
    rate: Rate,
): AccountBalances =
    AccountBalances(
        rrspOrRrif = balances.rrspOrRrif + balances.rrspOrRrif * rate,
        tfsa = balances.tfsa + balances.tfsa * rate,
        nonRegistered = balances.nonRegistered + balances.nonRegistered * rate,
    )
