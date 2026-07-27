package com.retirewise.onboarding.domain

/** Persists in-progress onboarding answers so the user can resume later. */
interface OnboardingDraftRepository {
    suspend fun save(state: OnboardingState)

    suspend fun load(): OnboardingState?

    suspend fun clear()
}
