package com.retirewise.onboarding.domain

/** The current progress through the onboarding question flow. */
data class OnboardingState(
    val responses: OnboardingResponses = OnboardingResponses(),
    val currentQuestionIndex: Int = 0,
)

/**
 * The question to show for the current state, or null once every question
 * in [OnboardingQuestion.entries] has been answered.
 */
fun onboardingCurrentQuestion(state: OnboardingState): OnboardingQuestion? =
    OnboardingQuestion.entries.getOrNull(state.currentQuestionIndex)

fun onboardingAdvance(state: OnboardingState): OnboardingState {
    val nextIndex = (state.currentQuestionIndex + 1).coerceAtMost(OnboardingQuestion.entries.size)
    return state.copy(currentQuestionIndex = nextIndex)
}

fun onboardingGoBack(state: OnboardingState): OnboardingState {
    val previousIndex = (state.currentQuestionIndex - 1).coerceAtLeast(0)
    return state.copy(currentQuestionIndex = previousIndex)
}

fun onboardingIsComplete(state: OnboardingState): Boolean =
    state.currentQuestionIndex >= OnboardingQuestion.entries.size

fun onboardingProgress(state: OnboardingState): Float =
    state.currentQuestionIndex.toFloat() / OnboardingQuestion.entries.size.toFloat()
