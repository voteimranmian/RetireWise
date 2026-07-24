package com.retirewise.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
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

    @Test
    fun iconShownWhenProvided() {
        assertTrue(retireWiseEmptyStateShowsIcon(icon = Icons.Filled.CalendarToday))
    }

    @Test
    fun iconHiddenWhenNotProvided() {
        assertFalse(retireWiseEmptyStateShowsIcon(icon = null))
    }
}
