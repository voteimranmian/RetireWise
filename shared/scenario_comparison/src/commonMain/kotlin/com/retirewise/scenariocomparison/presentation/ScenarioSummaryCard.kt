package com.retirewise.scenariocomparison.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.retirewise.core.value.Money
import com.retirewise.designsystem.RetireWiseButton
import com.retirewise.designsystem.RetireWiseButtonVariant
import com.retirewise.designsystem.RetireWiseCard
import com.retirewise.designsystem.RetireWiseTheme
import com.retirewise.retirementengine.domain.ProjectionValue
import com.retirewise.scenarioengine.domain.ScenarioNarrative
import com.retirewise.scenarioengine.domain.ScenarioProjectionSummary
import kotlin.math.roundToLong

/**
 * Formats a [Money] amount as a whole-dollar Canadian-dollar display string
 * (e.g. "$42,000"). Presentation-layer-only, deliberately not on [Money]
 * itself — [Money]/[com.retirewise.core.value.Rate] stay domain/display
 * agnostic per shared/core's own scope.
 */
fun formatMoney(money: Money): String {
    val dollars = (money.toDollarDouble()).roundToLong()
    val negative = dollars < 0
    val digits = dollars.toString().trimStart('-')
    val grouped =
        digits.reversed().chunked(3).joinToString(",").reversed()
    return (if (negative) "-$" else "$") + grouped
}

/** Renders a [ProjectionValue] as either a formatted dollar figure or its honest not-yet-modeled reason. */
private fun projectionValueText(value: ProjectionValue): String =
    when (value) {
        is ProjectionValue.Known -> formatMoney(value.amount)
        is ProjectionValue.NotYetModeled -> value.reason
    }

/** Renders a [ScenarioNarrative] as either its generated text or its honest not-yet-generated reason. */
private fun narrativeText(narrative: ScenarioNarrative): String =
    when (narrative) {
        is ScenarioNarrative.Generated -> narrative.text
        is ScenarioNarrative.NotYetGenerated -> narrative.reason
    }

/**
 * One [ScenarioProjectionSummary] — the base plan's or a built scenario's —
 * rendered per docs/PRD.md section 19.5. Tax and narrative fields show their
 * existing honest placeholder text rather than being hidden, matching the
 * honesty pattern used throughout the rest of the app.
 */
@Composable
fun ScenarioSummaryCard(
    title: String,
    summary: ScenarioProjectionSummary,
    modifier: Modifier = Modifier,
    onRemoveClick: (() -> Unit)? = null,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography

    RetireWiseCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.sm)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = title, style = typography.headlineMedium, color = colors.textPrimary)
                if (onRemoveClick != null) {
                    RetireWiseButton(
                        label = "Remove",
                        onClick = onRemoveClick,
                        variant = RetireWiseButtonVariant.Secondary,
                    )
                }
            }

            SummaryRow("Retirement age", summary.retirementAge.toString())
            SummaryRow("Net worth at 80", projectionValueText(summary.netWorthAtAge80))
            SummaryRow("Net worth at 90", projectionValueText(summary.netWorthAtAge90))
            SummaryRow("Estimated estate", formatMoney(summary.estimatedEstateAtProjectionEnd))
            SummaryRow("Government benefits at retirement", projectionValueText(summary.governmentBenefitsAtRetirement))
            SummaryRow(
                "Sustainability",
                if (summary.isSustainableThroughProjectionEnd) "On track" else "Funds may run out",
            )
            SummaryRow(
                "Monthly after-tax income at retirement",
                projectionValueText(summary.monthlyAfterTaxIncomeAtRetirement),
            )
            SummaryRow("Lifetime taxes paid", projectionValueText(summary.lifetimeTaxesPaid))
            SummaryRow("Main advantage", narrativeText(summary.mainAdvantage))
            SummaryRow("Main tradeoff", narrativeText(summary.mainTradeoff))
            SummaryRow("Key risk", narrativeText(summary.keyRisk))
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography

    Column {
        Text(text = label, style = typography.bodyMedium, color = colors.textSecondary)
        Text(text = value, style = typography.bodyLarge, color = colors.textPrimary)
    }
}
