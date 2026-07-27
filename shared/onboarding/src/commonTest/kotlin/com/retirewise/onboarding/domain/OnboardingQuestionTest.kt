package com.retirewise.onboarding.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OnboardingQuestionTest {
    @Test
    fun allTwelvePrdQuestionsArePresent() {
        assertEquals(12, OnboardingQuestion.entries.size)
    }

    @Test
    fun everyQuestionHasANonBlankPromptAndExplanation() {
        OnboardingQuestion.entries.forEach { question ->
            assertTrue(question.prompt.isNotBlank(), "Prompt for $question must not be blank")
            assertTrue(question.explanation.isNotBlank(), "Explanation for $question must not be blank")
        }
    }

    @Test
    fun questionsAreInPrdOrder() {
        val expectedOrder =
            listOf(
                OnboardingQuestion.Age,
                OnboardingQuestion.Province,
                OnboardingQuestion.TargetRetirementAge,
                OnboardingQuestion.AnnualIncome,
                OnboardingQuestion.RetirementSavings,
                OnboardingQuestion.WorkplacePension,
                OnboardingQuestion.MonthlyContribution,
                OnboardingQuestion.HomeOwnership,
                OnboardingQuestion.ExpectedDebt,
                OnboardingQuestion.TargetMonthlySpending,
                OnboardingQuestion.PlanningMode,
                OnboardingQuestion.RetirementPriorities,
            )

        assertEquals(expectedOrder, OnboardingQuestion.entries)
    }
}
