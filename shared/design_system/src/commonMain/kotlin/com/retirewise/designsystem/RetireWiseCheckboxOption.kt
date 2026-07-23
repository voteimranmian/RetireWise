package com.retirewise.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/**
 * A single independently-toggleable option, per docs/DESIGN_SYSTEM.md
 * section 18.2. Use [RetireWiseRadioOption] instead when only one of several
 * options may be selected at a time.
 *
 * The whole row is the tap target and carries the accessibility role and
 * checked state, so screen readers announce it as a checkbox (see
 * CLAUDE.md rule 16); callers must supply [contentDescription] whenever
 * [label] alone would not be clear.
 */
@Composable
fun RetireWiseCheckboxOption(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String = label,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography

    Row(
        modifier =
            modifier
                .toggleable(
                    value = checked,
                    enabled = enabled,
                    role = Role.Checkbox,
                    onValueChange = onCheckedChange,
                )
                .semantics { this.contentDescription = contentDescription }
                .padding(vertical = RetireWiseTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.sm),
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            colors =
                CheckboxDefaults.colors(
                    checkedColor = colors.primary,
                    uncheckedColor = colors.divider,
                ),
        )
        Text(text = label, style = typography.bodyMedium, color = colors.textPrimary)
    }
}
