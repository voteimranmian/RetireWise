package com.retirewise.profile.data

import com.retirewise.profile.domain.Profile
import com.retirewise.profile.domain.ProfileRepository
import com.retirewise.profile.domain.RetirementGoal

/**
 * In-memory only implementation. There is no deployed backend yet
 * (docs/RELEASE_PLAN.md Phase 3 is blocked on OAuth credentials and a
 * deployed backend), so the profile does not survive process death. This is
 * an honest placeholder, not a cache in front of real persistence.
 */
class InMemoryProfileRepository : ProfileRepository {
    private var profile: Profile? = null
    private var goal: RetirementGoal? = null

    override suspend fun save(
        profile: Profile,
        goal: RetirementGoal,
    ) {
        this.profile = profile
        this.goal = goal
    }

    override suspend fun current(): Profile? = profile

    override suspend fun currentGoal(): RetirementGoal? = goal
}
