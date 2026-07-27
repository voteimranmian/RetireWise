package com.retirewise.onboarding.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.retirewise.designsystem.RetireWiseRadioOption
import com.retirewise.designsystem.RetireWiseTheme
import com.retirewise.onboarding.domain.OnboardingQuestion

/** A yes/no answer (workplace pension, home ownership questions). */
@Composable
fun OnboardingYesNoQuestion(
    question: OnboardingQuestion,
    selected: Boolean?,
    onSelect: (Boolean) -> Unit,
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
            RetireWiseRadioOption(label = "Yes", selected = selected == true, onClick = { onSelect(true) })
            RetireWiseRadioOption(label = "No", selected = selected == false, onClick = { onSelect(false) })
        }
    }
}
