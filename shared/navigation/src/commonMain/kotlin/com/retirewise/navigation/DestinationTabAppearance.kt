package com.retirewise.navigation

import androidx.compose.ui.graphics.Color
import com.retirewise.designsystem.RetireWiseColors

/**
 * How a destination tab label should render. [emphasized] drives a text
 * style change (not colour alone) so selection is never communicated by
 * colour alone, per docs/DESIGN_SYSTEM.md section 18.5.
 */
data class DestinationTabAppearance(
    val textColor: Color,
    val emphasized: Boolean,
)

/**
 * Resolves the appearance of a destination tab label from its selection
 * state. Independent of Compose runtime so it can be unit tested directly.
 */
fun destinationTabAppearance(
    selected: Boolean,
    colors: RetireWiseColors,
): DestinationTabAppearance =
    DestinationTabAppearance(
        textColor = if (selected) colors.primary else colors.textSecondary,
        emphasized = selected,
    )
