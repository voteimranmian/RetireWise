package com.retirewise.onboarding.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.retirewise.designsystem.RetireWiseRadioOption
import com.retirewise.designsystem.RetireWiseTheme
import com.retirewise.onboarding.domain.OnboardingQuestion

/** A single answer chosen from a fixed, mutually-exclusive list of [options]. */
@Composable
fun <T> OnboardingSingleChoiceQuestion(
    question: OnboardingQuestion,
    options: List<T>,
    selected: T?,
    optionLabel: (T) -> String,
    onSelect: (T) -> Unit,
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
        nextEnabled = selected != null,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.xs),
        ) {
            options.forEach { option ->
                RetireWiseRadioOption(
                    label = optionLabel(option),
                    selected = selected == option,
                    onClick = { onSelect(option) },
                )
            }
        }
    }
}
