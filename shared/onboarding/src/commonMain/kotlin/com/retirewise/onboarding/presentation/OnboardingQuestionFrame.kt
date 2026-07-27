package com.retirewise.onboarding.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.retirewise.designsystem.RetireWiseButton
import com.retirewise.designsystem.RetireWiseButtonVariant
import com.retirewise.designsystem.RetireWiseTheme
import com.retirewise.onboarding.domain.OnboardingQuestion

/**
 * Shared chrome for every onboarding question screen: the prompt, an
 * "Explain why" toggle, the question-specific [content], and the
 * Back/Next/Skip/"I do not know" affordances required by docs/PRD.md
 * section 9.1.
 */
@Composable
fun OnboardingQuestionFrame(
    question: OnboardingQuestion,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    onDontKnow: () -> Unit,
    canGoBack: Boolean,
    nextEnabled: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = RetireWiseTheme.colors
    val typography = RetireWiseTheme.typography
    var showExplanation by remember(question) { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.lg),
    ) {
        Text(text = question.prompt, style = typography.headlineLarge, color = colors.textPrimary)

        Text(
            text = if (showExplanation) "Hide why we ask" else "Explain why you need this",
            style = typography.labelSmall,
            color = colors.textSecondary,
            modifier = Modifier.clickable { showExplanation = !showExplanation },
        )
        if (showExplanation) {
            Text(text = question.explanation, style = typography.bodyMedium, color = colors.textSecondary)
        }

        content()

        Row(
            horizontalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.sm),
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (canGoBack) {
                RetireWiseButton(
                    label = "Back",
                    onClick = onBack,
                    variant = RetireWiseButtonVariant.Secondary,
                )
            }
            RetireWiseButton(
                label = "Next",
                onClick = onNext,
                enabled = nextEnabled,
                variant = RetireWiseButtonVariant.Primary,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(RetireWiseTheme.spacing.lg),
        ) {
            Text(
                text = "Skip for now",
                style = typography.labelSmall,
                color = colors.textSecondary,
                modifier = Modifier.clickable(onClick = onSkip),
            )
            Text(
                text = "I do not know",
                style = typography.labelSmall,
                color = colors.textSecondary,
                modifier = Modifier.clickable(onClick = onDontKnow),
            )
        }
    }
}
