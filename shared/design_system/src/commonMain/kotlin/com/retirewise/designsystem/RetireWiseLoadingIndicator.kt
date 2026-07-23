package com.retirewise.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/**
 * Resolves the accessible/visible label for a loading indicator: the
 * caller-supplied [label] when present and non-blank, otherwise a
 * plain-language default. Independent of Compose runtime so it can be unit
 * tested directly.
 */
fun retireWiseLoadingContentDescription(label: String?): String = label?.takeIf { it.isNotBlank() } ?: "Loading"

/**
 * An indeterminate loading indicator always paired with a plain-language
 * label, per CLAUDE.md rule 15 (handle loading, empty, success, and error
 * states) and docs/DESIGN_SYSTEM.md section 18.5 (no colour/animation-only
 * communication of state).
 */
@Composable
fun RetireWiseLoadingIndicator(
    modifier: Modifier = Modifier,
    label: String? = null,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography
    val displayedLabel = retireWiseLoadingContentDescription(label)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .semantics { contentDescription = displayedLabel },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.sm),
    ) {
        CircularProgressIndicator(color = colors.primary)
        Text(text = displayedLabel, style = typography.bodyMedium, color = colors.textSecondary)
    }
}
