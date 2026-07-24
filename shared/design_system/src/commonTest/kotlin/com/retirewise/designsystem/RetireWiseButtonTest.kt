package com.retirewise.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

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

    @Test
    fun trailingIconShownWhenProvided() {
        assertTrue(retireWiseButtonShowsTrailingIcon(trailingIcon = Icons.Filled.ArrowForward))
    }

    @Test
    fun trailingIconHiddenWhenNotProvided() {
        assertFalse(retireWiseButtonShowsTrailingIcon(trailingIcon = null))
    }
}
