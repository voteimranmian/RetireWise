package com.retirewise.retirementengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import com.retirewise.retirementengine.domain.AccountBalances
import kotlin.test.Test
import kotlin.test.assertEquals

class GrowthFormulasTest {
    @Test
    fun growthCompoundsEveryAccountIndependently() {
        val balances =
            AccountBalances(
                rrspOrRrif = Money.ofDollars(1000.0),
                tfsa = Money.ofDollars(2000.0),
                nonRegistered = Money.ofDollars(500.0),
            )

        val result = applyGrowth(balances, Rate.ofPercent(10.0))

        assertEquals(Money.ofDollars(1100.0), result.rrspOrRrif)
        assertEquals(Money.ofDollars(2200.0), result.tfsa)
        assertEquals(Money.ofDollars(550.0), result.nonRegistered)
    }

    @Test
    fun zeroRateLeavesBalancesUnchanged() {
        val balances = AccountBalances(rrspOrRrif = Money.ofDollars(1000.0))

        val result = applyGrowth(balances, Rate.ZERO)

        assertEquals(balances, result)
    }

    @Test
    fun zeroBalanceStaysZeroRegardlessOfRate() {
        val result = applyGrowth(AccountBalances(), Rate.ofPercent(7.0))

        assertEquals(AccountBalances(), result)
    }
}
