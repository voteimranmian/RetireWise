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
 * The financial profile categories that make up a retirement plan, per
 * docs/PRD.md section 8.2. Independent of Compose runtime so the category
 * list can be unit tested directly.
 */
fun planCategories(): List<String> =
    listOf(
        "Goals",
        "Income",
        "Expenses",
        "Savings",
        "Investments",
        "Pensions",
        "Government benefits",
        "Property",
        "Debt",
        "Assumptions",
    )

/**
 * Plan — financial profile and retirement plan (docs/PRD.md section 8.2).
 * Category completion (Complete/Partially complete/Not started, per PRD
 * section 19.3) needs the profile module (Phase 4), so every category shows
 * as "Not started" for now rather than fabricated progress.
 */
@Composable
fun PlanScreen(modifier: Modifier = Modifier) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(RetireWiseTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.md),
    ) {
        Text(text = "Your plan", style = typography.headlineLarge, color = colors.textPrimary)

        planCategories().forEach { category ->
            RetireWiseCard(modifier = Modifier.fillMaxWidth()) {
                Text(text = category, style = typography.bodyLarge, color = colors.textPrimary)
                Text(text = "Not started", style = typography.bodyMedium, color = colors.textSecondary)
            }
        }
    }
}
