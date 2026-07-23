package com.retirewise.designsystem

import kotlin.test.Test
import kotlin.test.assertEquals

class RetireWiseProgressBarTest {
    @Test
    fun formatsWholeNumberPercentages() {
        assertEquals("0%", retireWiseProgressPercentageLabel(0f))
        assertEquals("60%", retireWiseProgressPercentageLabel(0.6f))
        assertEquals("100%", retireWiseProgressPercentageLabel(1f))
    }

    @Test
    fun roundsToNearestWholeNumber() {
        assertEquals("33%", retireWiseProgressPercentageLabel(0.334f))
        assertEquals("67%", retireWiseProgressPercentageLabel(0.666f))
    }

    @Test
    fun clampsOutOfRangeProgressValues() {
        assertEquals("0%", retireWiseProgressPercentageLabel(-0.5f))
        assertEquals("100%", retireWiseProgressPercentageLabel(1.5f))
    }
}
