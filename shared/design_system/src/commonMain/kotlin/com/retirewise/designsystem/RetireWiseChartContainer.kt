package com.retirewise.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/**
 * Resolves which optional action buttons a chart container should show,
 * independent of Compose runtime so it can be unit tested directly.
 */
data class RetireWiseChartActions(
    val showAssumptionsAction: Boolean,
    val showDataTableAction: Boolean,
)

fun retireWiseChartActions(
    onViewAssumptionsClick: (() -> Unit)?,
    onViewDataTableClick: (() -> Unit)?,
): RetireWiseChartActions =
    RetireWiseChartActions(
        showAssumptionsAction = onViewAssumptionsClick != null,
        showDataTableAction = onViewDataTableClick != null,
    )

/**
 * A card wrapper that enforces the accessible-chart requirements of
 * docs/DESIGN_SYSTEM.md section 18.6: every chart must have a title, a
 * plain-language takeaway, an accessible data summary (exposed to screen
 * readers instead of the raw visual), an assumptions link, and a data table
 * alternative.
 *
 * This container does not draw the chart itself — [chart] is supplied by
 * the caller. No charting library is bundled yet (see CLAUDE.md rule 10 on
 * third-party dependencies); this is the accessible scaffold future chart
 * content will plug into.
 */
@Composable
fun RetireWiseChartContainer(
    title: String,
    takeaway: String,
    accessibleSummary: String,
    modifier: Modifier = Modifier,
    onViewAssumptionsClick: (() -> Unit)? = null,
    onViewDataTableClick: (() -> Unit)? = null,
    chart: @Composable () -> Unit,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography
    val actions = retireWiseChartActions(onViewAssumptionsClick, onViewDataTableClick)

    RetireWiseCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.sm)) {
            Text(text = title, style = typography.headlineMedium, color = colors.textPrimary)
            Text(text = takeaway, style = typography.bodyLarge, color = colors.textPrimary)

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = accessibleSummary },
            ) {
                chart()
            }

            if (actions.showAssumptionsAction) {
                RetireWiseButton(
                    label = "View assumptions",
                    onClick = { onViewAssumptionsClick?.invoke() },
                    variant = RetireWiseButtonVariant.Secondary,
                )
            }

            if (actions.showDataTableAction) {
                RetireWiseButton(
                    label = "View data table",
                    onClick = { onViewDataTableClick?.invoke() },
                    variant = RetireWiseButtonVariant.Secondary,
                )
            }
        }
    }
}
