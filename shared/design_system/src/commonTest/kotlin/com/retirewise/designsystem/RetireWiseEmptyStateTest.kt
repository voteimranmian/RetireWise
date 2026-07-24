package com.retirewise.designsystem

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RetireWiseEmptyStateTest {
    @Test
    fun actionShownWhenCallbackProvided() {
        assertTrue(retireWiseEmptyStateShowsAction(onActionClick = {}))
    }

    @Test
    fun actionHiddenWhenNoCallbackProvided() {
        assertFalse(retireWiseEmptyStateShowsAction(onActionClick = null))
    }
}
