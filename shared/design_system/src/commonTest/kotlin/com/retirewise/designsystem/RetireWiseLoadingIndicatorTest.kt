package com.retirewise.designsystem

import kotlin.test.Test
import kotlin.test.assertEquals

class RetireWiseLoadingIndicatorTest {
    @Test
    fun defaultLabelUsedWhenNullProvided() {
        assertEquals("Loading", retireWiseLoadingContentDescription(null))
    }

    @Test
    fun defaultLabelUsedWhenBlankProvided() {
        assertEquals("Loading", retireWiseLoadingContentDescription("   "))
    }

    @Test
    fun customLabelUsedWhenProvided() {
        assertEquals("Loading your plan", retireWiseLoadingContentDescription("Loading your plan"))
    }
}
