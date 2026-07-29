package com.retirewise.scenariocomparison.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.retirewise.core.value.Money
import com.retirewise.designsystem.RetireWiseButton
import com.retirewise.designsystem.RetireWiseTextField
import com.retirewise.designsystem.RetireWiseTheme
import com.retirewise.scenariocomparison.domain.LeverInputKind
import com.retirewise.scenariocomparison.domain.LeverInputValue
import com.retirewise.scenariocomparison.domain.ScenarioLever

/**
 * A single input appropriate to [lever]'s [LeverInputKind] (age or dollar
 * amount), followed by a "Run scenario" button. Reuses
 * [RetireWiseTextField] with a numeric keyboard for both kinds — the same
 * approach `shared/onboarding`'s age/income questions already use — rather
 * than introducing a new stepper control the design system doesn't have yet.
 */
@Composable
fun ScenarioLeverInputForm(
    lever: ScenarioLever,
    onRunScenario: (LeverInputValue) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember(lever) { mutableStateOf("") }
    val typography = RetireWiseTheme.typography
    val colors = RetireWiseTheme.colors

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.md),
    ) {
        Text(text = lever.displayLabel, style = typography.headlineMedium, color = colors.textPrimary)

        RetireWiseTextField(
            label = if (lever.inputKind == LeverInputKind.AGE) "Age" else "Amount ($)",
            value = text,
            onValueChange = { text = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )

        val input = scenarioLeverInputValue(lever, text)
        RetireWiseButton(
            label = "Run scenario",
            onClick = { input?.let(onRunScenario) },
            enabled = input != null,
        )
    }
}

/**
 * Parses [text] into the [LeverInputValue] [lever] needs, or `null` while
 * the field is blank/unparsable. Independent of Compose runtime so it can be
 * unit tested directly.
 */
fun scenarioLeverInputValue(
    lever: ScenarioLever,
    text: String,
): LeverInputValue? =
    when (lever.inputKind) {
        LeverInputKind.AGE -> text.toIntOrNull()?.let { LeverInputValue.Age(it) }
        LeverInputKind.MONEY -> text.toDoubleOrNull()?.let { LeverInputValue.Amount(Money.ofDollars(it)) }
        null -> null
    }
