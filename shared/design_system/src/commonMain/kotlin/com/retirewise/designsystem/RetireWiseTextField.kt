package com.retirewise.designsystem

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/**
 * Resolved supporting/error text and colour for a text field, independent of
 * Compose runtime state so it can be unit tested directly. Error text always
 * takes priority over supporting text when [isError] is true, per
 * docs/DESIGN_SYSTEM.md section 18.5 (clear error messages, no colour-only
 * communication).
 */
@Immutable
data class RetireWiseTextFieldState(
    val displayedSupportingText: String?,
    val supportingTextColor: Color,
)

fun retireWiseTextFieldState(
    isError: Boolean,
    supportingText: String?,
    errorText: String?,
    colors: RetireWiseColors,
): RetireWiseTextFieldState =
    if (isError && errorText != null) {
        RetireWiseTextFieldState(errorText, colors.critical)
    } else {
        RetireWiseTextFieldState(supportingText, colors.textSecondary)
    }

/**
 * A labelled text input per docs/DESIGN_SYSTEM.md section 18.5. Callers must
 * supply [contentDescription] whenever [label] alone would not be clear to a
 * screen reader (see CLAUDE.md rule 16).
 */
@Composable
fun RetireWiseTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    isError: Boolean = false,
    errorText: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    contentDescription: String = label,
) {
    val colors = RetireWiseTheme.colors
    val state = retireWiseTextFieldState(isError, supportingText, errorText, colors)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        label = { Text(label) },
        supportingText =
            state.displayedSupportingText?.let { text ->
                { Text(text = text, color = state.supportingTextColor) }
            },
        isError = isError,
        enabled = enabled,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.divider,
                errorBorderColor = colors.critical,
            ),
    )
}
