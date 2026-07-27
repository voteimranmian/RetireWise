package com.retirewise.retirementengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.retirementengine.domain.AccountBalances

const val FORMULA_WITHDRAWAL_SEQUENTIAL_NONREG_RRSP_TFSA_V1 = "WITHDRAWAL_SEQUENTIAL_NONREG_RRSP_TFSA_V1"

/** Result of attempting to withdraw enough to cover a shortfall. */
data class WithdrawalResult(
    val balances: AccountBalances,
    val amountWithdrawn: Money,
    val unmetShortfall: Money,
)

/**
 * Withdraws up to [deficit] from [balances] in a fixed order — non-registered,
 * then RRSP/RRIF, then TFSA (tax-optimized withdrawal sequencing is a later-
 * phase enhancement, not modeled here). Every balance is clamped at
 * [Money.ZERO] and never goes negative — "no borrowing"
 * (docs/TEST_STRATEGY.md section 24.3, invariant 4). If [deficit] exceeds
 * the total available, the remainder is reported as
 * [WithdrawalResult.unmetShortfall] rather than fabricating a negative
 * balance.
 */
fun withdrawToCoverDeficit(
    balances: AccountBalances,
    deficit: Money,
): WithdrawalResult {
    if (deficit <= Money.ZERO) {
        return WithdrawalResult(balances, Money.ZERO, Money.ZERO)
    }

    var remaining = deficit
    var withdrawn = Money.ZERO

    fun drawFrom(available: Money): Money {
        val draw = if (available < remaining) available else remaining
        remaining -= draw
        withdrawn += draw
        return available - draw
    }

    val nonRegistered = drawFrom(balances.nonRegistered)
    val rrspOrRrif = drawFrom(balances.rrspOrRrif)
    val tfsa = drawFrom(balances.tfsa)

    return WithdrawalResult(
        balances = AccountBalances(rrspOrRrif = rrspOrRrif, tfsa = tfsa, nonRegistered = nonRegistered),
        amountWithdrawn = withdrawn,
        unmetShortfall = remaining.coerceAtLeast(Money.ZERO),
    )
}
