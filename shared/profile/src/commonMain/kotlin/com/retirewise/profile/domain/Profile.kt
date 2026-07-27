package com.retirewise.profile.domain

/**
 * The durable output of onboarding (docs/PRD.md section 9.1). Every field is
 * nullable because onboarding allows skipping any question ("Skip for now" /
 * "I do not know"); the deterministic retirement engine (Phase 5) is
 * responsible for handling incomplete profiles.
 */
data class Profile(
    val age: Int? = null,
    val province: CanadianProvince? = null,
    val annualIncome: Double? = null,
    val retirementSavings: Double? = null,
    val hasWorkplacePension: Boolean? = null,
    val monthlyContribution: Double? = null,
    val ownsHome: Boolean? = null,
    val expectedDebtAtRetirement: Double? = null,
    val planningMode: PlanningMode? = null,
)
