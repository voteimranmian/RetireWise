package com.retirewise.designsystem

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RetireWiseCardTest {
    @Test
    fun isInteractiveWhenOnClickProvided() {
        assertTrue(retireWiseCardIsInteractive(onClick = {}))
    }

    @Test
    fun isNotInteractiveWhenOnClickIsNull() {
        assertFalse(retireWiseCardIsInteractive(onClick = null))
    }
}
