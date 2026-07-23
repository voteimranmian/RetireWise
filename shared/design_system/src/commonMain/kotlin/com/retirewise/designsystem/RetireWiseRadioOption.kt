package com.retirewise.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/**
 * A single option within a mutually-exclusive choice, per
 * docs/DESIGN_SYSTEM.md section 18.2. Group several of these under a shared
 * `Modifier.selectableGroup()` container and manage `selected` state
 * externally (this component is stateless/controlled).
 *
 * The whole row is the tap target and carries the accessibility role and
 * selected state, so screen readers announce it as a radio button (see
 * CLAUDE.md rule 16); callers must supply [contentDescription] whenever
 * [label] alone would not be clear.
 */
@Composable
fun RetireWiseRadioOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String = label,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography

    Row(
        modifier =
            modifier
                .selectable(
                    selected = selected,
                    enabled = enabled,
                    role = Role.RadioButton,
                    onClick = onClick,
                )
                .semantics { this.contentDescription = contentDescription }
                .padding(vertical = RetireWiseTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.sm),
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            enabled = enabled,
            colors =
                RadioButtonDefaults.colors(
                    selectedColor = colors.primary,
                    unselectedColor = colors.divider,
                ),
        )
        Text(text = label, style = typography.bodyMedium, color = colors.textPrimary)
    }
}
