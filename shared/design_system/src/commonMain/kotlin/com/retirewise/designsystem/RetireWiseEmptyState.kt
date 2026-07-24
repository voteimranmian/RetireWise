package com.retirewise.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Whether an empty state should show an action, independent of Compose
 * runtime so it can be unit tested directly.
 */
fun retireWiseEmptyStateShowsAction(onActionClick: (() -> Unit)?): Boolean = onActionClick != null

/**
 * Whether an empty state should show a leading icon, independent of Compose
 * runtime so it can be unit tested directly.
 */
fun retireWiseEmptyStateShowsIcon(icon: ImageVector?): Boolean = icon != null

/**
 * A plain-language empty state for a screen or section with no content yet,
 * per CLAUDE.md rule 15 (handle loading, empty, success, and error states).
 * Uses the informational colour token rather than critical/caution, since an
 * empty state is a normal, expected condition rather than a problem.
 */
@Composable
fun RetireWiseEmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography
    val showsAction = retireWiseEmptyStateShowsAction(onActionClick)
    val showsIcon = retireWiseEmptyStateShowsIcon(icon)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .semantics { contentDescription = "$title. $message" },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.sm),
    ) {
        if (showsIcon && icon != null) {
            Icon(
                imageVector = icon,
                // Decorative only — the title/message semantics above already
                // describe this empty state to screen readers.
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = colors.outline,
            )
        }
        Text(
            text = title,
            style = typography.headlineMedium,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = message,
            style = typography.bodyLarge,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
        )
        if (showsAction && actionLabel != null) {
            RetireWiseButton(
                label = actionLabel,
                onClick = { onActionClick?.invoke() },
                variant = RetireWiseButtonVariant.Secondary,
            )
        }
    }
}
