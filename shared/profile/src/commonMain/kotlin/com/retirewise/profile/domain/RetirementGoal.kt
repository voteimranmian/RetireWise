package com.retirewise.profile.domain

/** The user's stated retirement goal, captured during onboarding. */
data class RetirementGoal(
    val targetRetirementAge: Int? = null,
    val targetMonthlySpending: Double? = null,
    val priorities: Set<RetirementPriority> = emptySet(),
)
