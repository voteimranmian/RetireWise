package com.retirewise.designsystem

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RetireWiseErrorStateTest {
    @Test
    fun retryShownWhenCallbackProvided() {
        assertTrue(retireWiseErrorStateShowsRetry(onRetryClick = {}))
    }

    @Test
    fun retryHiddenWhenNoCallbackProvided() {
        assertFalse(retireWiseErrorStateShowsRetry(onRetryClick = null))
    }
}
