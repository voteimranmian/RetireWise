package com.retirewise.onboarding.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.retirewise.designsystem.RetireWiseTextField
import com.retirewise.onboarding.domain.OnboardingQuestion

/**
 * A free-form numeric answer (age, income, savings, contributions, debt, and
 * target spending questions).
 */
@Composable
fun OnboardingNumericQuestion(
    question: OnboardingQuestion,
    value: String,
    onValueChange: (String) -> Unit,
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
        nextEnabled = value.isNotBlank(),
        modifier = modifier,
    ) {
        RetireWiseTextField(
            label = "Your answer",
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            contentDescription = question.prompt,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
