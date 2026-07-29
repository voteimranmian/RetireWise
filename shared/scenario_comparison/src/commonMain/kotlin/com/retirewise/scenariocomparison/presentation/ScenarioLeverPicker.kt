package com.retirewise.scenariocomparison.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
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
import com.retirewise.scenariocomparison.domain.ScenarioLever
import com.retirewise.scenariocomparison.domain.isSupported

/**
 * The leading icon for a [ScenarioLever]. Pure and independent of the
 * Compose runtime so the mapping can be unit tested directly. Falls back to
 * a generic document icon for any future lever not yet mapped here.
 */
fun scenarioLeverIcon(lever: ScenarioLever): ImageVector =
    when (lever) {
        ScenarioLever.RETIRE_EARLIER -> Icons.Filled.EventAvailable
        ScenarioLever.DELAY_RETIREMENT -> Icons.Filled.AccessTime
        ScenarioLever.DELAY_CPP -> Icons.Filled.AccountBalance
        ScenarioLever.DELAY_OAS -> Icons.Filled.AccountBalanceWallet
        ScenarioLever.INCREASE_SAVINGS -> Icons.Filled.TrendingUp
        ScenarioLever.PAY_OFF_MORTGAGE -> Icons.Filled.Payments
        ScenarioLever.DOWNSIZE_HOME -> Icons.Filled.Home
        ScenarioLever.WORK_PART_TIME -> Icons.Filled.Work
        ScenarioLever.CHANGE_RETIREMENT_SPENDING -> Icons.Filled.SwapHoriz
    }

/**
 * Lists all 9 [ScenarioLever] entries (docs/PRD.md section 8.3). The 3
 * unsupported ones render disabled with the same honesty-pattern copy
 * `ExploreScreen.kt` used before this phase; tapping a supported one invokes
 * [onLeverClick].
 */
@Composable
fun ScenarioLeverPicker(
    onLeverClick: (ScenarioLever) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.md),
    ) {
        ScenarioLever.entries.forEach { lever ->
            ScenarioLeverCard(
                lever = lever,
                onClick = { if (lever.isSupported()) onLeverClick(lever) },
            )
        }
    }
}

@Composable
private fun ScenarioLeverCard(
    lever: ScenarioLever,
    onClick: () -> Unit,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography
    val supported = lever.isSupported()

    RetireWiseCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = if (supported) onClick else null,
    ) {
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
                    text = if (supported) "Tap to configure" else "Available in a future release",
                    style = typography.bodyMedium,
                    color = colors.textSecondary,
                )
            }
        }
    }
}
