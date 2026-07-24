package com.retirewise.navigation

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdaptiveNavigationLayoutTest {
    @Test
    fun narrowWidthUsesBottomBar() {
        assertFalse(usesNavigationRail(windowWidthDp = 400))
    }

    @Test
    fun wideWidthUsesNavigationRail() {
        assertTrue(usesNavigationRail(windowWidthDp = 900))
    }

    @Test
    fun breakpointWidthUsesNavigationRail() {
        assertTrue(usesNavigationRail(windowWidthDp = NAVIGATION_RAIL_BREAKPOINT_DP))
    }
}
