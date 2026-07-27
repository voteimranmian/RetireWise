package com.retirewise.onboarding.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.retirewise.designsystem.RetireWiseCheckboxOption
import com.retirewise.designsystem.RetireWiseTheme
import com.retirewise.onboarding.domain.OnboardingQuestion

/**
 * Zero or more answers chosen from a fixed list of [options] (retirement
 * priorities question). An empty selection is treated the same as "Skip for
 * now" once Next is pressed, so [nextEnabled] is always true here.
 */
@Composable
fun <T> OnboardingMultiChoiceQuestion(
    question: OnboardingQuestion,
    options: List<T>,
    selected: Set<T>,
    optionLabel: (T) -> String,
    onToggle: (T) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    onDontKnow: () -> Unit,
    canGoBack: Boolean,
    modifier: Modifier = Modifier,
) {
    OnboardingQuestionFrame(
        question = question,
        onNext = onNext,
        onBack = onBack,
        onSkip = onSkip,
        onDontKnow = onDontKnow,
        canGoBack = canGoBack,
        nextEnabled = true,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.xs),
        ) {
            options.forEach { option ->
                RetireWiseCheckboxOption(
                    label = optionLabel(option),
                    checked = option in selected,
                    onCheckedChange = { onToggle(option) },
                )
            }
        }
    }
}
