package com.retirewise.core.value

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoneyTest {
    @Test
    fun ofDollarsRoundTripsThroughDollarDouble() {
        val money = Money.ofDollars(1234.5678)

        assertEquals(1234.5678, money.toDollarDouble())
    }

    @Test
    fun plusAddsAmounts() {
        val sum = Money.ofDollars(100.0) + Money.ofDollars(50.25)

        assertEquals(Money.ofDollars(150.25), sum)
    }

    @Test
    fun minusSubtractsAmounts() {
        val difference = Money.ofDollars(100.0) - Money.ofDollars(30.0)

        assertEquals(Money.ofDollars(70.0), difference)
    }

    @Test
    fun unaryMinusNegatesAmount() {
        val negated = -Money.ofDollars(42.0)

        assertEquals(Money.ofDollars(-42.0), negated)
    }

    @Test
    fun timesRateScalesAmount() {
        val grown = Money.ofDollars(1000.0) * Rate.ofPercent(5.0)

        assertEquals(Money.ofDollars(50.0), grown)
    }

    @Test
    fun compareToOrdersByMagnitude() {
        assertTrue(Money.ofDollars(10.0) < Money.ofDollars(20.0))
        assertTrue(Money.ofDollars(20.0) > Money.ofDollars(10.0))
        assertEquals(Money.ofDollars(10.0), Money.ofDollars(10.0))
    }

    @Test
    fun coerceAtLeastClampsToMinimum() {
        val clamped = Money.ofDollars(-5.0).coerceAtLeast(Money.ZERO)

        assertEquals(Money.ZERO, clamped)
    }

    @Test
    fun coerceAtLeastLeavesValueAboveMinimumUnchanged() {
        val unchanged = Money.ofDollars(100.0).coerceAtLeast(Money.ZERO)

        assertEquals(Money.ofDollars(100.0), unchanged)
    }

    @Test
    fun roundedToCentsRoundsHalfUpForPositiveAmounts() {
        val money = Money.ofMinorUnits(125) // 0.0125 dollars => 1.25 cents

        assertEquals(1L, money.roundedToCents())
    }

    @Test
    fun roundedToCentsRoundsHalfUpAtExactHalfway() {
        val money = Money.ofMinorUnits(150) // exactly 1.5 cents

        assertEquals(2L, money.roundedToCents())
    }

    @Test
    fun roundedToCentsRoundsHalfUpForNegativeAmounts() {
        val money = Money.ofMinorUnits(-150) // exactly -1.5 cents

        assertEquals(-2L, money.roundedToCents())
    }

    @Test
    fun zeroHasNoValue() {
        assertEquals(0.0, Money.ZERO.toDollarDouble())
    }
}
