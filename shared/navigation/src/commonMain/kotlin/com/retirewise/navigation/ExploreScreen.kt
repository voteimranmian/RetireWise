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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.retirewise.designsystem.RetireWiseCard
import com.retirewise.designsystem.RetireWiseTheme

/**
 * The scenario types a user will be able to explore, per docs/PRD.md
 * section 8.3. Independent of Compose runtime so the list can be unit
 * tested directly.
 */
fun exploreScenarioTypes(): List<String> =
    listOf(
        "Retire earlier",
        "Delay retirement",
        "Delay CPP",
        "Delay OAS",
        "Increase savings",
        "Pay off mortgage",
        "Downsize home",
        "Work part time",
        "Change retirement spending",
    )

/**
 * The leading icon for a scenario type card. Pure and independent of
 * Compose runtime so the mapping can be unit tested directly. Falls back to
 * a generic document icon for any future scenario type not yet mapped here.
 */
fun exploreScenarioIcon(scenarioType: String): ImageVector =
    when (scenarioType) {
        "Retire earlier" -> Icons.Filled.EventAvailable
        "Delay retirement" -> Icons.Filled.AccessTime
        "Delay CPP" -> Icons.Filled.AccountBalance
        "Delay OAS" -> Icons.Filled.AccountBalanceWallet
        "Increase savings" -> Icons.Filled.TrendingUp
        "Pay off mortgage" -> Icons.Filled.Payments
        "Downsize home" -> Icons.Filled.Home
        "Work part time" -> Icons.Filled.Work
        "Change retirement spending" -> Icons.Filled.SwapHoriz
        else -> Icons.Filled.Description
    }

/**
 * Explore — scenario planning and comparison (docs/PRD.md section 8.3).
 * Running a scenario needs the scenario engine (Phase 7 per
 * docs/RELEASE_PLAN.md), so each type is listed as not yet available rather
 * than producing fabricated comparisons.
 */
@Composable
fun ExploreScreen(modifier: Modifier = Modifier) {
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
                exploreScenarioTypes().chunked(columnCount).forEach { rowTypes ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.md),
                    ) {
                        rowTypes.forEach { scenarioType ->
                            ExploreScenarioCard(
                                scenarioType = scenarioType,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        // Pad the final row so a lone card in a 2-column grid
                        // doesn't stretch to fill both columns.
                        repeat(columnCount - rowTypes.size) {
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
    scenarioType: String,
    modifier: Modifier = Modifier,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography

    RetireWiseCard(modifier = modifier.fillMaxWidth()) {
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
                    imageVector = exploreScenarioIcon(scenarioType),
                    contentDescription = null,
                    modifier = Modifier.padding(RetireWiseTheme.spacing.sm).size(20.dp),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = scenarioType, style = typography.bodyLarge, color = colors.textPrimary)
                Text(
                    text = "Available once your plan is built",
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
