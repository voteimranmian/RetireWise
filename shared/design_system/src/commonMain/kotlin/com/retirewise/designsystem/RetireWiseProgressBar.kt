package com.retirewise.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import kotlin.math.roundToInt

/**
 * Clamps [progress] to the valid 0f..1f range and formats it as a
 * plain-language whole-number percentage (e.g. "60%"), independent of
 * Compose runtime so it can be unit tested directly.
 */
fun retireWiseProgressPercentageLabel(progress: Float): String {
    val clamped = progress.coerceIn(0f, 1f)
    return "${(clamped * 100).roundToInt()}%"
}

/**
 * A determinate progress indicator with a plain-language label and
 * percentage, per docs/DESIGN_SYSTEM.md section 18.2 ("clear progress
 * indicators"). Progress is always paired with visible text, never
 * communicated by colour or bar length alone (section 18.5).
 */
@Composable
fun RetireWiseProgressBar(
    progress: Float,
    label: String,
    modifier: Modifier = Modifier,
    contentDescription: String = "$label, ${retireWiseProgressPercentageLabel(progress)}",
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography
    val clamped = progress.coerceIn(0f, 1f)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .semantics { this.contentDescription = contentDescription },
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.xs),
    ) {
        Text(text = label, style = typography.labelLarge, color = colors.textPrimary)
        LinearProgressIndicator(
            progress = { clamped },
            modifier = Modifier.fillMaxWidth(),
            color = colors.primary,
            trackColor = colors.divider,
        )
        Text(
            text = retireWiseProgressPercentageLabel(clamped),
            style = typography.labelSmall,
            color = colors.textSecondary,
        )
    }
}
