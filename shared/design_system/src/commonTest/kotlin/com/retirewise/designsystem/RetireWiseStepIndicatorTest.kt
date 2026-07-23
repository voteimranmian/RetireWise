package com.retirewise.designsystem

import kotlin.test.Test
import kotlin.test.assertEquals

class RetireWiseStepIndicatorTest {
    @Test
    fun stepsUpToAndIncludingCurrentStepArePrimary() {
        val colors = LightRetireWiseColors

        val result = retireWiseStepIndicatorDotColors(currentStep = 2, totalSteps = 4, colors = colors)

        assertEquals(
            listOf(colors.primary, colors.primary, colors.divider, colors.divider),
            result,
        )
    }

    @Test
    fun allDotsAreDividerWhenCurrentStepIsZero() {
        val colors = LightRetireWiseColors

        val result = retireWiseStepIndicatorDotColors(currentStep = 0, totalSteps = 3, colors = colors)

        assertEquals(listOf(colors.divider, colors.divider, colors.divider), result)
    }

    @Test
    fun allDotsArePrimaryWhenCurrentStepIsTheLastStep() {
        val colors = LightRetireWiseColors

        val result = retireWiseStepIndicatorDotColors(currentStep = 3, totalSteps = 3, colors = colors)

        assertEquals(listOf(colors.primary, colors.primary, colors.primary), result)
    }
}
