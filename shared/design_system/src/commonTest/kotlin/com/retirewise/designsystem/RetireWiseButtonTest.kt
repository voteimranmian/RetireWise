package com.retirewise.designsystem

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RetireWiseButtonTest {
    @Test
    fun primaryVariantUsesFilledPrimaryContainerAndNoBorder() {
        val style = retireWiseButtonStyle(RetireWiseButtonVariant.Primary, LightRetireWiseColors)

        assertEquals(LightRetireWiseColors.primary, style.containerColor)
        assertEquals(LightRetireWiseColors.surface, style.contentColor)
        assertNull(style.borderColor)
    }

    @Test
    fun secondaryVariantIsOutlinedOnSurface() {
        val style = retireWiseButtonStyle(RetireWiseButtonVariant.Secondary, LightRetireWiseColors)

        assertEquals(LightRetireWiseColors.surface, style.containerColor)
        assertEquals(LightRetireWiseColors.textPrimary, style.contentColor)
        assertEquals(LightRetireWiseColors.divider, style.borderColor)
    }
}
