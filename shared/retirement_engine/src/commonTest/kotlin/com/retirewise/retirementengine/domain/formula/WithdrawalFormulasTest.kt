package com.retirewise.retirementengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.retirementengine.domain.AccountBalances
import kotlin.test.Test
import kotlin.test.assertEquals

class WithdrawalFormulasTest {
    @Test
    fun nonPositiveDeficitWithdrawsNothing() {
        val balances = AccountBalances(nonRegistered = Money.ofDollars(1000.0))

        val result = withdrawToCoverDeficit(balances, Money.ZERO)

        assertEquals(balances, result.balances)
        assertEquals(Money.ZERO, result.amountWithdrawn)
        assertEquals(Money.ZERO, result.unmetShortfall)
    }

    @Test
    fun withdrawsFromNonRegisteredFirst() {
        val balances =
            AccountBalances(
                rrspOrRrif = Money.ofDollars(1000.0),
                tfsa = Money.ofDollars(1000.0),
                nonRegistered = Money.ofDollars(1000.0),
            )

        val result = withdrawToCoverDeficit(balances, Money.ofDollars(300.0))

        assertEquals(Money.ofDollars(700.0), result.balances.nonRegistered)
        assertEquals(Money.ofDollars(1000.0), result.balances.rrspOrRrif)
        assertEquals(Money.ofDollars(1000.0), result.balances.tfsa)
        assertEquals(Money.ofDollars(300.0), result.amountWithdrawn)
        assertEquals(Money.ZERO, result.unmetShortfall)
    }

    @Test
    fun fallsThroughToRrspOrRrifThenTfsaWhenNonRegisteredIsExhausted() {
        val balances =
            AccountBalances(
                rrspOrRrif = Money.ofDollars(500.0),
                tfsa = Money.ofDollars(500.0),
                nonRegistered = Money.ofDollars(100.0),
            )

        val result = withdrawToCoverDeficit(balances, Money.ofDollars(700.0))

        assertEquals(Money.ZERO, result.balances.nonRegistered)
        assertEquals(Money.ZERO, result.balances.rrspOrRrif)
        assertEquals(Money.ofDollars(400.0), result.balances.tfsa)
        assertEquals(Money.ofDollars(700.0), result.amountWithdrawn)
        assertEquals(Money.ZERO, result.unmetShortfall)
    }

    @Test
    fun reportsUnmetShortfallWithoutGoingNegative() {
        val balances =
            AccountBalances(
                rrspOrRrif = Money.ofDollars(100.0),
                tfsa = Money.ofDollars(100.0),
                nonRegistered = Money.ofDollars(100.0),
            )

        val result = withdrawToCoverDeficit(balances, Money.ofDollars(1000.0))

        assertEquals(Money.ZERO, result.balances.nonRegistered)
        assertEquals(Money.ZERO, result.balances.rrspOrRrif)
        assertEquals(Money.ZERO, result.balances.tfsa)
        assertEquals(Money.ofDollars(300.0), result.amountWithdrawn)
        assertEquals(Money.ofDollars(700.0), result.unmetShortfall)
    }
}
