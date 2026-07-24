package com.retirewise.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.retirewise.designsystem.RetireWiseTheme

/**
 * Bottom navigation bar for compact/phone widths. See [DestinationNavigationRail]
 * for the expanded/tablet-desktop equivalent (docs/RELEASE_PLAN.md Phase 2
 * "Add platform adaptive behaviour").
 */
@Composable
fun DestinationNavigationBar(
    currentDestination: Destination?,
    onDestinationClick: (Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = RetireWiseTheme.colors

    Surface(modifier = modifier.fillMaxWidth(), color = colors.surfaceRaised, contentColor = colors.textPrimary) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = RetireWiseTheme.spacing.sm),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Destination.entries.forEach { destination ->
                DestinationTabLabel(
                    destination = destination,
                    selected = destination == currentDestination,
                    onClick = { onDestinationClick(destination) },
                )
            }
        }
    }
}

/**
 * Side navigation rail for expanded/tablet-desktop widths. See
 * [DestinationNavigationBar] for the compact/phone equivalent.
 */
@Composable
fun DestinationNavigationRail(
    currentDestination: Destination?,
    onDestinationClick: (Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = RetireWiseTheme.colors

    Surface(modifier = modifier.fillMaxHeight(), color = colors.surfaceRaised, contentColor = colors.textPrimary) {
        Column(
            modifier =
                Modifier.fillMaxHeight().padding(
                    horizontal = RetireWiseTheme.spacing.md,
                    vertical = RetireWiseTheme.spacing.lg,
                ),
            verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.lg),
        ) {
            Destination.entries.forEach { destination ->
                DestinationTabLabel(
                    destination = destination,
                    selected = destination == currentDestination,
                    onClick = { onDestinationClick(destination) },
                )
            }
        }
    }
}

@Composable
private fun DestinationTabLabel(
    destination: Destination,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography
    val appearance = destinationTabAppearance(selected = selected, colors = colors)

    Text(
        text = destination.label,
        style = if (appearance.emphasized) typography.labelLarge else typography.labelSmall,
        color = appearance.textColor,
        modifier =
            Modifier
                .selectable(selected = selected, role = Role.Tab, onClick = onClick)
                .semantics { contentDescription = "${destination.label} tab" }
                .padding(horizontal = RetireWiseTheme.spacing.sm, vertical = RetireWiseTheme.spacing.xs),
    )
}
