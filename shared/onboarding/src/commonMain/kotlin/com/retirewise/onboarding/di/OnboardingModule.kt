package com.retirewise.onboarding.di

import com.retirewise.onboarding.data.InMemoryOnboardingDraftRepository
import com.retirewise.onboarding.domain.OnboardingDraftRepository
import org.koin.dsl.module

val onboardingModule =
    module {
        single<OnboardingDraftRepository> { InMemoryOnboardingDraftRepository() }
    }
