package com.retirewise.onboarding.presentation

import com.retirewise.profile.domain.CanadianProvince
import com.retirewise.profile.domain.PlanningMode
import com.retirewise.profile.domain.Profile
import com.retirewise.profile.domain.RetirementGoal
import com.retirewise.profile.domain.RetirementPriority
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OnboardingCompleteScreenLogicTest {
    @Test
    fun emptyProfileAndGoalProduceNoSummaryLines() {
        val lines = onboardingCompleteSummaryLines(Profile(), RetirementGoal())

        assertEquals(emptyList(), lines)
    }

    @Test
    fun onlyAnsweredFieldsProduceSummaryLines() {
        val profile = Profile(age = 45, province = CanadianProvince.ONTARIO)
        val goal = RetirementGoal(targetRetirementAge = 65)

        val lines = onboardingCompleteSummaryLines(profile, goal)

        assertEquals(3, lines.size)
        assertTrue(lines.any { it.contains("45") })
        assertTrue(lines.any { it.contains("Ontario") })
        assertTrue(lines.any { it.contains("65") })
    }

    @Test
    fun fullyAnsweredProfileAndGoalProduceOneLinePerField() {
        val profile =
            Profile(
                age = 45,
                province = CanadianProvince.ONTARIO,
                annualIncome = 90000.0,
                retirementSavings = 150000.0,
                hasWorkplacePension = true,
                monthlyContribution = 500.0,
                ownsHome = true,
                expectedDebtAtRetirement = 0.0,
                planningMode = PlanningMode.PLANNING_ALONE,
            )
        val goal =
            RetirementGoal(
                targetRetirementAge = 65,
                targetMonthlySpending = 4000.0,
                priorities = setOf(RetirementPriority.TRAVEL, RetirementPriority.FAMILY),
            )

        val lines = onboardingCompleteSummaryLines(profile, goal)

        assertEquals(12, lines.size)
        assertTrue(lines.any { it.contains("Travel") && it.contains("Family") })
    }
}
