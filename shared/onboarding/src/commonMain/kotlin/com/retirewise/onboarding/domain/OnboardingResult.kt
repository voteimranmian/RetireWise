package com.retirewise.onboarding.domain

import com.retirewise.profile.domain.Profile
import com.retirewise.profile.domain.RetirementGoal

fun profileFrom(responses: OnboardingResponses): Profile =
    Profile(
        age = responses.age,
        province = responses.province,
        annualIncome = responses.annualIncome,
        retirementSavings = responses.retirementSavings,
        hasWorkplacePension = responses.hasWorkplacePension,
        monthlyContribution = responses.monthlyContribution,
        ownsHome = responses.ownsHome,
        expectedDebtAtRetirement = responses.expectedDebtAtRetirement,
        planningMode = responses.planningMode,
    )

fun goalFrom(responses: OnboardingResponses): RetirementGoal =
    RetirementGoal(
        targetRetirementAge = responses.targetRetirementAge,
        targetMonthlySpending = responses.targetMonthlySpending,
        priorities = responses.priorities,
    )
