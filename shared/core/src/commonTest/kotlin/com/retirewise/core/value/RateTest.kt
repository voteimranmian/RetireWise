package com.retirewise.core.value

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RateTest {
    @Test
    fun ofPercentConvertsToFraction() {
        assertEquals(0.05, Rate.ofPercent(5.0).fraction)
    }

    @Test
    fun zeroHasNoFraction() {
        assertEquals(0.0, Rate.ZERO.fraction)
    }

    @Test
    fun nonFiniteFractionIsRejected() {
        assertFailsWith<IllegalArgumentException> { Rate(Double.NaN) }
        assertFailsWith<IllegalArgumentException> { Rate(Double.POSITIVE_INFINITY) }
    }
}
