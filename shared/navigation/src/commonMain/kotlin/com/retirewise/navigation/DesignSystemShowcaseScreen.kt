package com.retirewise.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.retirewise.designsystem.RetireWiseButton
import com.retirewise.designsystem.RetireWiseButtonVariant
import com.retirewise.designsystem.RetireWiseCard
import com.retirewise.designsystem.RetireWiseChartContainer
import com.retirewise.designsystem.RetireWiseCheckboxOption
import com.retirewise.designsystem.RetireWiseErrorState
import com.retirewise.designsystem.RetireWiseLoadingIndicator
import com.retirewise.designsystem.RetireWiseProgressBar
import com.retirewise.designsystem.RetireWiseRadioOption
import com.retirewise.designsystem.RetireWiseStepIndicator
import com.retirewise.designsystem.RetireWiseTextField
import com.retirewise.designsystem.RetireWiseTheme

/**
 * Phase 1 exit criteria per docs/RELEASE_PLAN.md: a screen that exercises
 * every shared/design_system component. This is verification scaffolding,
 * not a real product screen (see [Screen.DesignSystemShowcase]).
 */
@Composable
fun DesignSystemShowcaseScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography

    var textFieldValue by remember { mutableStateOf("") }
    var selectedRiskOption by remember { mutableStateOf(1) }
    var cppChecked by remember { mutableStateOf(true) }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(RetireWiseTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.xl),
    ) {
        Text(text = "Design system showcase", style = typography.headlineLarge, color = colors.textPrimary)

        ShowcaseSection(title = "Buttons") {
            RetireWiseButton(
                label = "Primary action",
                onClick = {},
                variant = RetireWiseButtonVariant.Primary,
                modifier = Modifier.fillMaxWidth(),
            )
            RetireWiseButton(
                label = "Secondary action",
                onClick = {},
                variant = RetireWiseButtonVariant.Secondary,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        ShowcaseSection(title = "Card") {
            RetireWiseCard(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Card title", style = typography.bodyLarge, color = colors.textPrimary)
                Text(
                    text = "Supporting card content goes here.",
                    style = typography.bodyMedium,
                    color = colors.textSecondary,
                )
            }
        }

        ShowcaseSection(title = "Text field") {
            RetireWiseTextField(
                label = "Annual income",
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                supportingText = "Before tax, in Canadian dollars",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        ShowcaseSection(title = "Selection controls") {
            listOf("Conservative", "Moderate", "Aggressive").forEachIndexed { index, label ->
                RetireWiseRadioOption(
                    label = label,
                    selected = selectedRiskOption == index,
                    onClick = { selectedRiskOption = index },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            RetireWiseCheckboxOption(
                label = "Include Canada Pension Plan",
                checked = cppChecked,
                onCheckedChange = { cppChecked = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        ShowcaseSection(title = "Progress") {
            RetireWiseProgressBar(
                progress = 0.6f,
                label = "On track for your goal",
                modifier = Modifier.fillMaxWidth(),
            )
            RetireWiseStepIndicator(currentStep = 2, totalSteps = 4)
        }

        ShowcaseSection(title = "Chart container") {
            RetireWiseChartContainer(
                title = "Savings projection",
                takeaway = "Your savings are projected to last through age 90.",
                accessibleSummary =
                    "Line chart showing savings balance from age 65 to 95, reaching zero at age 90.",
                onViewAssumptionsClick = {},
                onViewDataTableClick = {},
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Chart placeholder", style = typography.labelSmall, color = colors.textSecondary)
                }
            }
        }

        ShowcaseSection(title = "Loading") {
            RetireWiseLoadingIndicator(label = "Loading your plan")
        }

        ShowcaseSection(title = "Error") {
            RetireWiseErrorState(
                title = "Something went wrong",
                message = "We couldn't load your plan. Please try again.",
                onRetryClick = {},
            )
        }

        RetireWiseButton(
            label = "Back",
            onClick = onBackClick,
            variant = RetireWiseButtonVariant.Secondary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ShowcaseSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography

    Column(verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.sm)) {
        Text(text = title, style = typography.headlineMedium, color = colors.textPrimary)
        content()
    }
}
