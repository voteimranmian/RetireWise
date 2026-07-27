package com.retirewise.profile.domain

/** Persists and retrieves the user's profile and retirement goal. */
interface ProfileRepository {
    suspend fun save(
        profile: Profile,
        goal: RetirementGoal,
    )

    suspend fun current(): Profile?

    suspend fun currentGoal(): RetirementGoal?
}
