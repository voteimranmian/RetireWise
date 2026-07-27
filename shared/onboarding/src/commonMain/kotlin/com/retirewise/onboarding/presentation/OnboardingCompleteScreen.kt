package com.retirewise.onboarding.presentation

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
import com.retirewise.designsystem.RetireWiseButton
import com.retirewise.designsystem.RetireWiseTheme
import com.retirewise.profile.domain.Profile
import com.retirewise.profile.domain.RetirementGoal

/**
 * The Phase 4 exit criteria's "placeholder readiness result" (docs/PRD.md
 * section 9.1): it echoes back only what the user actually entered. It never
 * shows a computed readiness percentage or projected dollar figures — those
 * require the deterministic retirement engine (Phase 5), per CLAUDE.md's
 * non-negotiable against fabricated financial values.
 */
@Composable
fun OnboardingCompleteScreen(
    profile: Profile,
    goal: RetirementGoal,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography
    val summaryLines = onboardingCompleteSummaryLines(profile, goal)

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(RetireWiseTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.lg),
    ) {
        Text(text = "Your plan is on its way", style = typography.headlineLarge, color = colors.textPrimary)
        Text(
            text =
                "Thanks for sharing this with us. We don't calculate your retirement readiness " +
                    "yet in this build — that comes from RetireWise's retirement engine, which " +
                    "isn't wired up here. For now, here is what we've noted:",
            style = typography.bodyLarge,
            color = colors.textSecondary,
        )

        if (summaryLines.isEmpty()) {
            Text(
                text = "You skipped every question, so there is nothing to show yet.",
                style = typography.bodyMedium,
                color = colors.textSecondary,
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.xs)) {
                summaryLines.forEach { line ->
                    Text(text = line, style = typography.bodyMedium, color = colors.textPrimary)
                }
            }
        }

        RetireWiseButton(
            label = "Back to Today",
            onClick = onDone,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * The plain-language lines shown on the completion screen, one per answered
 * (non-null / non-empty) field. Independent of Compose runtime so it can be
 * unit tested directly.
 */
fun onboardingCompleteSummaryLines(
    profile: Profile,
    goal: RetirementGoal,
): List<String> =
    buildList {
        profile.age?.let { add("Age: $it") }
        profile.province?.let { add("Province or territory: ${it.displayLabel}") }
        goal.targetRetirementAge?.let { add("Target retirement age: $it") }
        profile.annualIncome?.let { add("Approximate annual income: $it") }
        profile.retirementSavings?.let { add("Current retirement savings: $it") }
        profile.hasWorkplacePension?.let { add("Workplace pension: ${if (it) "Yes" else "No"}") }
        profile.monthlyContribution?.let { add("Monthly retirement contribution: $it") }
        profile.ownsHome?.let { add("Owns home: ${if (it) "Yes" else "No"}") }
        profile.expectedDebtAtRetirement?.let { add("Expected debt at retirement: $it") }
        goal.targetMonthlySpending?.let { add("Target monthly spending in retirement: $it") }
        profile.planningMode?.let { add("Planning mode: ${it.displayLabel}") }
        if (goal.priorities.isNotEmpty()) {
            add("What matters most: ${goal.priorities.joinToString(", ") { it.displayLabel }}")
        }
    }
