package com.retirewise.retirementengine.domain

import com.retirewise.core.value.Money

/**
 * The three account buckets tracked per projection year
 * (docs/FINANCIAL_RULES.md section 11.3). RRSP and RRIF share a single
 * bucket because the output shape has one combined "RRSP or RRIF balance"
 * row, and modeling RRIF minimum withdrawal rules is explicitly Release-two
 * scope (docs/RELEASE_PLAN.md).
 */
data class AccountBalances(
    val rrspOrRrif: Money = Money.ZERO,
    val tfsa: Money = Money.ZERO,
    val nonRegistered: Money = Money.ZERO,
) {
    val total: Money get() = rrspOrRrif + tfsa + nonRegistered
}
