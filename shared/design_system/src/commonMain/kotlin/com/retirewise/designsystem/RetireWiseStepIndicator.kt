package com.retirewise.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * Resolves the fill colour for each dot in a step indicator: steps up to and
 * including [currentStep] (1-based) are rendered in the primary colour
 * (completed/current), remaining steps use the divider colour. Independent
 * of Compose runtime so it can be unit tested directly.
 */
fun retireWiseStepIndicatorDotColors(
    currentStep: Int,
    totalSteps: Int,
    colors: RetireWiseColors,
): List<Color> = (1..totalSteps).map { step -> if (step <= currentStep) colors.primary else colors.divider }

/**
 * A step-of-total progress indicator for multi-step flows (e.g. onboarding),
 * per docs/DESIGN_SYSTEM.md section 18.2 ("clear progress indicators"). The
 * step count is always exposed via [contentDescription], never dots alone.
 */
@Composable
fun RetireWiseStepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
    contentDescription: String = "Step $currentStep of $totalSteps",
) {
    val colors = RetireWiseTheme.colors
    val dotColors = retireWiseStepIndicatorDotColors(currentStep, totalSteps, colors)

    Row(
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        horizontalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.xs),
    ) {
        dotColors.forEach { dotColor ->
            Surface(
                color = dotColor,
                shape = CircleShape,
                modifier = Modifier.size(8.dp),
                content = {},
            )
        }
    }
}
