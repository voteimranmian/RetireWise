package com.retirewise.onboarding.domain

import com.retirewise.profile.domain.CanadianProvince
import com.retirewise.profile.domain.PlanningMode
import com.retirewise.profile.domain.RetirementPriority
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OnboardingResultTest {
    @Test
    fun profileFromFullyAnsweredResponsesCopiesEveryField() {
        val responses =
            OnboardingResponses(
                age = 45,
                province = CanadianProvince.ONTARIO,
                annualIncome = 90000.0,
                retirementSavings = 150000.0,
                hasWorkplacePension = true,
                monthlyContribution = 500.0,
                ownsHome = true,
                expectedDebtAtRetirement = 0.0,
                planningMode = PlanningMode.PLANNING_WITH_PARTNER,
            )

        val profile = profileFrom(responses)

        assertEquals(45, profile.age)
        assertEquals(CanadianProvince.ONTARIO, profile.province)
        assertEquals(90000.0, profile.annualIncome)
        assertEquals(150000.0, profile.retirementSavings)
        assertEquals(true, profile.hasWorkplacePension)
        assertEquals(500.0, profile.monthlyContribution)
        assertEquals(true, profile.ownsHome)
        assertEquals(0.0, profile.expectedDebtAtRetirement)
        assertEquals(PlanningMode.PLANNING_WITH_PARTNER, profile.planningMode)
    }

    @Test
    fun goalFromFullyAnsweredResponsesCopiesEveryField() {
        val responses =
            OnboardingResponses(
                targetRetirementAge = 65,
                targetMonthlySpending = 4000.0,
                priorities = setOf(RetirementPriority.TRAVEL, RetirementPriority.HEALTH),
            )

        val goal = goalFrom(responses)

        assertEquals(65, goal.targetRetirementAge)
        assertEquals(4000.0, goal.targetMonthlySpending)
        assertEquals(setOf(RetirementPriority.TRAVEL, RetirementPriority.HEALTH), goal.priorities)
    }

    @Test
    fun profileAndGoalFromAllNullResponsesAreAllNull() {
        val responses = OnboardingResponses()

        val profile = profileFrom(responses)
        val goal = goalFrom(responses)

        assertNull(profile.age)
        assertNull(profile.province)
        assertNull(goal.targetRetirementAge)
        assertNull(goal.targetMonthlySpending)
        assertEquals(emptySet(), goal.priorities)
    }
}
