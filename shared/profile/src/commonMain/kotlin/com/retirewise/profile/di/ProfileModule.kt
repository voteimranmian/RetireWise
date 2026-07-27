package com.retirewise.profile.di

import com.retirewise.profile.data.InMemoryProfileRepository
import com.retirewise.profile.domain.ProfileRepository
import org.koin.dsl.module

val profileModule =
    module {
        single<ProfileRepository> { InMemoryProfileRepository() }
    }
