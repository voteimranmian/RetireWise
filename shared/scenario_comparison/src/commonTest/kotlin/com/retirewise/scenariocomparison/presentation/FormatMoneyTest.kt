package com.retirewise.scenariocomparison.presentation

import com.retirewise.core.value.Money
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatMoneyTest {
    @Test
    fun formatsWholeDollarsWithThousandsSeparators() {
        assertEquals("$42,000", formatMoney(Money.ofDollars(42000.0)))
        assertEquals("$1,234,567", formatMoney(Money.ofDollars(1234567.0)))
    }

    @Test
    fun formatsZero() {
        assertEquals("$0", formatMoney(Money.ZERO))
    }

    @Test
    fun formatsSmallAmountsWithoutSeparators() {
        assertEquals("$500", formatMoney(Money.ofDollars(500.0)))
    }

    @Test
    fun formatsNegativeAmounts() {
        assertEquals("-$1,000", formatMoney(Money.ofDollars(-1000.0)))
    }
}
