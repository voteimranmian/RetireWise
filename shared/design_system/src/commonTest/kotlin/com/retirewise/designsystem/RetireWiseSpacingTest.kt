package com.retirewise.designsystem

import kotlin.test.Test
import kotlin.test.assertTrue

class RetireWiseSpacingTest {
    @Test
    fun spacingScaleIsMonotonicallyIncreasing() {
        val values =
            listOf(
                RetireWiseSpacing.xs,
                RetireWiseSpacing.sm,
                RetireWiseSpacing.md,
                RetireWiseSpacing.lg,
                RetireWiseSpacing.xl,
                RetireWiseSpacing.xxl,
            )

        for (index in 0 until values.size - 1) {
            assertTrue(values[index] < values[index + 1])
        }
    }
}
