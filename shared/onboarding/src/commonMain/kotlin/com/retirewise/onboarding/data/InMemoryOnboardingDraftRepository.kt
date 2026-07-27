package com.retirewise.onboarding.data

import com.retirewise.onboarding.domain.OnboardingDraftRepository
import com.retirewise.onboarding.domain.OnboardingState

/**
 * In-memory only implementation. There is no deployed backend yet
 * (docs/RELEASE_PLAN.md Phase 3 is blocked on OAuth credentials and a
 * deployed backend), so a draft does not survive process death. This is an
 * honest placeholder, not a cache in front of real persistence.
 */
class InMemoryOnboardingDraftRepository : OnboardingDraftRepository {
    private var draft: OnboardingState? = null

    override suspend fun save(state: OnboardingState) {
        draft = state
    }

    override suspend fun load(): OnboardingState? = draft

    override suspend fun clear() {
        draft = null
    }
}
