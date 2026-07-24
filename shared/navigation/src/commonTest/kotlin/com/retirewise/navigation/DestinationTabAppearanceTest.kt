package com.retirewise.navigation

import com.retirewise.designsystem.LightRetireWiseColors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DestinationTabAppearanceTest {
    @Test
    fun selectedTabUsesPrimaryColorAndIsEmphasized() {
        val appearance = destinationTabAppearance(selected = true, colors = LightRetireWiseColors)

        assertEquals(LightRetireWiseColors.primary, appearance.textColor)
        assertTrue(appearance.emphasized)
    }

    @Test
    fun unselectedTabUsesSecondaryColorAndIsNotEmphasized() {
        val appearance = destinationTabAppearance(selected = false, colors = LightRetireWiseColors)

        assertEquals(LightRetireWiseColors.textSecondary, appearance.textColor)
        assertFalse(appearance.emphasized)
    }
}
