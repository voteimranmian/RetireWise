package com.retirewise.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.retirewise.designsystem.RetireWiseCard
import com.retirewise.designsystem.RetireWiseTheme
import com.retirewise.scenariocomparison.domain.ScenarioLever
import com.retirewise.scenariocomparison.domain.isSupported
import com.retirewise.scenariocomparison.presentation.scenarioLeverIcon

/**
 * Explore — scenario planning and comparison (docs/PRD.md section 8.3).
 * The 9 scenario types come from [ScenarioLever]/`shared/scenario_comparison`
 * (Phase 7b). The 6 supported levers open [ScenarioLeverCard]'s
 * [onScenarioTypeClick]; the 3 still-unbuilt ones (pay off mortgage, downsize
 * home, work part time) keep listing as not yet available rather than
 * producing fabricated comparisons.
 */
@Composable
fun ExploreScreen(
    onScenarioTypeClick: (ScenarioLever) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(RetireWiseTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.md),
    ) {
        Text(text = "Explore scenarios", style = typography.headlineLarge, color = colors.textPrimary)

        BoxWithConstraints {
            val columnCount = if (usesNavigationRail(windowWidthDp = maxWidth.value.toInt())) 2 else 1

            Column(verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.md)) {
                ScenarioLever.entries.chunked(columnCount).forEach { rowLevers ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.md),
                    ) {
                        rowLevers.forEach { lever ->
                            ExploreScenarioCard(
                                lever = lever,
                                onClick = { if (lever.isSupported()) onScenarioTypeClick(lever) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        // Pad the final row so a lone card in a 2-column grid
                        // doesn't stretch to fill both columns.
                        repeat(columnCount - rowLevers.size) {
                            Column(modifier = Modifier.weight(1f)) {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExploreScenarioCard(
    lever: ScenarioLever,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography
    val supported = lever.isSupported()

    RetireWiseCard(modifier = modifier.fillMaxWidth(), onClick = if (supported) onClick else null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.sm),
        ) {
            Surface(
                shape = CircleShape,
                color = colors.secondaryContainer,
                contentColor = colors.onSecondaryContainer,
            ) {
                Icon(
                    imageVector = scenarioLeverIcon(lever),
                    contentDescription = null,
                    modifier = Modifier.padding(RetireWiseTheme.spacing.sm).size(20.dp),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = lever.displayLabel, style = typography.bodyLarge, color = colors.textPrimary)
                Text(
                    text = if (supported) "Tap to configure" else "Available once your plan is built",
                    style = typography.bodyMedium,
                    color = colors.textSecondary,
                )
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = colors.outline,
            )
        }
    }
}
