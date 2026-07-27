package com.retirewise.profile.domain

/** Whether the user is planning their retirement alone or with a partner. */
enum class PlanningMode(val displayLabel: String) {
    PLANNING_ALONE("Planning alone"),
    PLANNING_WITH_PARTNER("Planning with a spouse or partner"),
}
