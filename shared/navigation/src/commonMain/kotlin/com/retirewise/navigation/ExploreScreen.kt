package com.retirewise.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

        exploreScenarioTypes().forEach { scenarioType ->
            RetireWiseCard(modifier = Modifier.fillMaxWidth()) {
                Text(text = scenarioType, style = typography.bodyLarge, color = colors.textPrimary)
                Text(
                    text = "Available once your plan is built",
                    style = typography.bodyMedium,
                    color = colors.textSecondary,
                )
            }
        }
    }
}
