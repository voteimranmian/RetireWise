package com.retirewise.onboarding.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OnboardingStateTest {
    @Test
    fun currentQuestionForFreshStateIsTheFirstQuestion() {
        val state = OnboardingState()

        assertEquals(OnboardingQuestion.Age, onboardingCurrentQuestion(state))
    }

    @Test
    fun advanceMovesToTheNextQuestion() {
        val state = onboardingAdvance(OnboardingState())

        assertEquals(OnboardingQuestion.Province, onboardingCurrentQuestion(state))
        assertEquals(1, state.currentQuestionIndex)
    }

    @Test
    fun advancePastTheLastQuestionDoesNotGoOutOfBounds() {
        var state = OnboardingState()
        repeat(OnboardingQuestion.entries.size + 5) {
            state = onboardingAdvance(state)
        }

        assertEquals(OnboardingQuestion.entries.size, state.currentQuestionIndex)
        assertNull(onboardingCurrentQuestion(state))
    }

    @Test
    fun goBackFromTheFirstQuestionStaysAtTheFirstQuestion() {
        val state = onboardingGoBack(OnboardingState())

        assertEquals(0, state.currentQuestionIndex)
    }

    @Test
    fun goBackMovesToThePreviousQuestion() {
        val advanced = onboardingAdvance(onboardingAdvance(OnboardingState()))

        val state = onboardingGoBack(advanced)

        assertEquals(1, state.currentQuestionIndex)
    }

    @Test
    fun isCompleteIsFalseUntilEveryQuestionIsAnswered() {
        var state = OnboardingState()
        repeat(OnboardingQuestion.entries.size - 1) {
            state = onboardingAdvance(state)
        }

        assertFalse(onboardingIsComplete(state))

        state = onboardingAdvance(state)

        assertTrue(onboardingIsComplete(state))
    }

    @Test
    fun progressReflectsFractionOfQuestionsAnswered() {
        val state = OnboardingState()

        assertEquals(0f, onboardingProgress(state))

        val complete = OnboardingState(currentQuestionIndex = OnboardingQuestion.entries.size)

        assertEquals(1f, onboardingProgress(complete))
    }
}
