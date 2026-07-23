package com.retirewise.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign

/**
 * Whether an error state should show a retry action, independent of Compose
 * runtime so it can be unit tested directly.
 */
fun retireWiseErrorStateShowsRetry(onRetryClick: (() -> Unit)?): Boolean = onRetryClick != null

/**
 * A plain-language error state, per CLAUDE.md rule 15 (handle loading,
 * empty, success, and error states) and docs/DESIGN_SYSTEM.md section 18.5
 * (clear error messages — the title and message text always carry the
 * meaning, never the critical colour accent alone).
 */
@Composable
fun RetireWiseErrorState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    retryLabel: String = "Try again",
    onRetryClick: (() -> Unit)? = null,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography
    val showsRetry = retireWiseErrorStateShowsRetry(onRetryClick)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .semantics { contentDescription = "$title. $message" },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.sm),
    ) {
        Text(
            text = title,
            style = typography.headlineMedium,
            color = colors.critical,
            textAlign = TextAlign.Center,
        )
        Text(
            text = message,
            style = typography.bodyLarge,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
        )
        if (showsRetry) {
            RetireWiseButton(
                label = retryLabel,
                onClick = { onRetryClick?.invoke() },
                variant = RetireWiseButtonVariant.Primary,
            )
        }
    }
}
