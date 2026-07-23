package com.retirewise.designsystem

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RetireWiseTextFieldTest {
    @Test
    fun errorTextTakesPriorityOverSupportingTextWhenIsErrorTrue() {
        val state =
            retireWiseTextFieldState(
                isError = true,
                supportingText = "Enter a whole number",
                errorText = "This field is required",
                colors = LightRetireWiseColors,
            )

        assertEquals("This field is required", state.displayedSupportingText)
        assertEquals(LightRetireWiseColors.critical, state.supportingTextColor)
    }

    @Test
    fun supportingTextShownWhenNotInError() {
        val state =
            retireWiseTextFieldState(
                isError = false,
                supportingText = "Enter a whole number",
                errorText = "This field is required",
                colors = LightRetireWiseColors,
            )

        assertEquals("Enter a whole number", state.displayedSupportingText)
        assertEquals(LightRetireWiseColors.textSecondary, state.supportingTextColor)
    }

    @Test
    fun nullWhenNoSupportingOrErrorTextProvided() {
        val state =
            retireWiseTextFieldState(
                isError = false,
                supportingText = null,
                errorText = null,
                colors = LightRetireWiseColors,
            )

        assertNull(state.displayedSupportingText)
    }

    @Test
    fun fallsBackToSupportingTextWhenErrorTrueButNoErrorTextProvided() {
        val state =
            retireWiseTextFieldState(
                isError = true,
                supportingText = "Enter a whole number",
                errorText = null,
                colors = LightRetireWiseColors,
            )

        assertEquals("Enter a whole number", state.displayedSupportingText)
        assertEquals(LightRetireWiseColors.textSecondary, state.supportingTextColor)
    }
}
