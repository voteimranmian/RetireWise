package com.retirewise.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Card corner radius is deliberately moderate, not fully rounded — see
 * docs/DESIGN_SYSTEM.md section 18.1 ("rounded but not playful cards").
 */
object RetireWiseCardDefaults {
    val cornerRadius: Dp = 16.dp
    val shape: Shape = RoundedCornerShape(cornerRadius)
}

/**
 * Whether a card should be rendered as a single tappable action, independent
 * of Compose runtime so it can be unit tested directly.
 */
fun retireWiseCardIsInteractive(onClick: (() -> Unit)?): Boolean = onClick != null

/**
 * A container surface for grouped content (goal tiles, insight cards,
 * scenario summaries, etc). Pass [onClick] only when the whole card is a
 * single tappable action; otherwise place interactive elements inside
 * [content] individually so screen readers announce them correctly.
 */
@Composable
fun RetireWiseCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = RetireWiseTheme.colors
    val cardModifier =
        if (retireWiseCardIsInteractive(onClick)) {
            modifier.clickable(onClick = onClick ?: {})
        } else {
            modifier
        }

    Surface(
        modifier = cardModifier,
        shape = RetireWiseCardDefaults.shape,
        color = colors.surface,
        contentColor = colors.textPrimary,
        border = BorderStroke(1.dp, colors.divider),
    ) {
        Column(
            modifier = Modifier.padding(RetireWiseTheme.spacing.md),
            content = content,
        )
    }
}
