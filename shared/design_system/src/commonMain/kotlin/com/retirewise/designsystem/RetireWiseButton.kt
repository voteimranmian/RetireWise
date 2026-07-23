package com.retirewise.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * The two button emphases used across RetireWise per docs/DESIGN_SYSTEM.md
 * section 18.2. There is deliberately no "tertiary"/"text" variant yet —
 * add one only once a real screen needs it.
 */
enum class RetireWiseButtonVariant {
    Primary,
    Secondary,
}

/** Resolved container/content/border colours for a button variant. Pure and
 * independent of Compose runtime state, so it can be unit tested directly. */
@Immutable
data class RetireWiseButtonStyle(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color?,
)

fun retireWiseButtonStyle(
    variant: RetireWiseButtonVariant,
    colors: RetireWiseColors,
): RetireWiseButtonStyle =
    when (variant) {
        RetireWiseButtonVariant.Primary ->
            RetireWiseButtonStyle(
                containerColor = colors.primary,
                contentColor = colors.surface,
                borderColor = null,
            )
        RetireWiseButtonVariant.Secondary ->
            RetireWiseButtonStyle(
                containerColor = colors.surface,
                contentColor = colors.textPrimary,
                borderColor = colors.divider,
            )
    }

object RetireWiseButtonDefaults {
    val shape: Shape = androidx.compose.foundation.shape.RoundedCornerShape(percent = 50)
    val contentPadding =
        PaddingValues(horizontal = RetireWiseSpacing.xl, vertical = RetireWiseSpacing.md)
}

/**
 * Primary interactive action per docs/DESIGN_SYSTEM.md. Callers must supply
 * [contentDescription] whenever [label] alone would not be clear to a
 * screen reader (see CLAUDE.md rule 16).
 */
@Composable
fun RetireWiseButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: RetireWiseButtonVariant = RetireWiseButtonVariant.Primary,
    enabled: Boolean = true,
    contentDescription: String = label,
) {
    val style = retireWiseButtonStyle(variant, RetireWiseTheme.colors)
    val labelStyle = RetireWiseTheme.typography.labelLarge
    val buttonModifier = modifier.semantics { this.contentDescription = contentDescription }

    if (style.borderColor != null) {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            shape = RetireWiseButtonDefaults.shape,
            border = BorderStroke(1.dp, style.borderColor),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = style.contentColor),
            contentPadding = RetireWiseButtonDefaults.contentPadding,
            modifier = buttonModifier,
        ) {
            Text(label, style = labelStyle)
        }
    } else {
        Button(
            onClick = onClick,
            enabled = enabled,
            shape = RetireWiseButtonDefaults.shape,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = style.containerColor,
                    contentColor = style.contentColor,
                ),
            contentPadding = RetireWiseButtonDefaults.contentPadding,
            modifier = buttonModifier,
        ) {
            Text(label, style = labelStyle)
        }
    }
}
