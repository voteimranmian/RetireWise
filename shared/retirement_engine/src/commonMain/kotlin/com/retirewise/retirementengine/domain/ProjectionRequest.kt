package com.retirewise.retirementengine.domain

import com.retirewise.core.value.Money
import com.retirewise.profile.domain.CanadianProvince
import com.retirewise.profile.domain.Profile
import com.retirewise.profile.domain.RetirementGoal

private const val MONTHS_PER_YEAR = 12

private const val FIELD_AGE = "age"
private const val FIELD_RETIREMENT_AGE = "targetRetirementAge"
private const val FIELD_ANNUAL_INCOME = "annualIncome"
private const val FIELD_RETIREMENT_SAVINGS = "retirementSavings"
private const val FIELD_MONTHLY_CONTRIBUTION = "monthlyContribution"
private const val FIELD_TARGET_MONTHLY_SPENDING = "targetMonthlySpending"

/**
 * The validation boundary between [Profile]/[RetirementGoal]'s nullable,
 * skippable onboarding fields and the total (non-null) math functions in
 * `domain.formula`. Building a [Ready] request is the only way to run
 * [com.retirewise.retirementengine.domain.formula.project].
 */
sealed interface ProjectionRequest {
    data class Ready(
        val currentAge: Int,
        val retirementAge: Int,
        val province: CanadianProvince?,
        val maritalStatus: MaritalStatus,
        val employmentIncome: Money,
        val startingBalances: AccountBalances,
        val employeeAnnualContribution: Money,
        val expectedDebtAtRetirement: Money,
        val targetAnnualSpending: Money,
        val assumptions: Assumptions,
    ) : ProjectionRequest

    data class Incomplete(val missingFields: Set<String>) : ProjectionRequest
}

/**
 * Maps a (possibly incomplete) [Profile]/[RetirementGoal] pair plus explicit
 * [Assumptions] into a [ProjectionRequest]. Missing required fields produce
 * [ProjectionRequest.Incomplete] rather than a projection built on fabricated
 * defaults.
 */
fun toProjectionRequest(
    profile: Profile,
    goal: RetirementGoal,
    assumptions: Assumptions,
): ProjectionRequest {
    val missing = mutableSetOf<String>()
    if (profile.age == null) missing.add(FIELD_AGE)
    if (goal.targetRetirementAge == null) missing.add(FIELD_RETIREMENT_AGE)
    if (profile.annualIncome == null) missing.add(FIELD_ANNUAL_INCOME)
    if (profile.retirementSavings == null) missing.add(FIELD_RETIREMENT_SAVINGS)
    if (profile.monthlyContribution == null) missing.add(FIELD_MONTHLY_CONTRIBUTION)
    if (goal.targetMonthlySpending == null) missing.add(FIELD_TARGET_MONTHLY_SPENDING)

    if (missing.isNotEmpty()) {
        return ProjectionRequest.Incomplete(missing)
    }

    return ProjectionRequest.Ready(
        currentAge = profile.age!!,
        retirementAge = goal.targetRetirementAge!!,
        province = profile.province,
        maritalStatus = MaritalStatus.SINGLE,
        employmentIncome = Money.ofDollars(profile.annualIncome!!),
        startingBalances = AccountBalances(rrspOrRrif = Money.ofDollars(profile.retirementSavings!!)),
        employeeAnnualContribution = Money.ofDollars(profile.monthlyContribution!! * MONTHS_PER_YEAR),
        expectedDebtAtRetirement = profile.expectedDebtAtRetirement?.let { Money.ofDollars(it) } ?: Money.ZERO,
        targetAnnualSpending = Money.ofDollars(goal.targetMonthlySpending!! * MONTHS_PER_YEAR),
        assumptions = assumptions,
    )
}
