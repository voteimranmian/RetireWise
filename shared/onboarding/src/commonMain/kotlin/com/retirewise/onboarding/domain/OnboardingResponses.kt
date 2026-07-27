package com.retirewise.onboarding.domain

import com.retirewise.profile.domain.CanadianProvince
import com.retirewise.profile.domain.PlanningMode
import com.retirewise.profile.domain.RetirementPriority

/**
 * The answers collected so far during onboarding, one nullable field per
 * question in [OnboardingQuestion]. A field stays null if the user skips the
 * question or answers "I do not know".
 */
data class OnboardingResponses(
    val age: Int? = null,
    val province: CanadianProvince? = null,
    val targetRetirementAge: Int? = null,
    val annualIncome: Double? = null,
    val retirementSavings: Double? = null,
    val hasWorkplacePension: Boolean? = null,
    val monthlyContribution: Double? = null,
    val ownsHome: Boolean? = null,
    val expectedDebtAtRetirement: Double? = null,
    val targetMonthlySpending: Double? = null,
    val planningMode: PlanningMode? = null,
    val priorities: Set<RetirementPriority> = emptySet(),
)
