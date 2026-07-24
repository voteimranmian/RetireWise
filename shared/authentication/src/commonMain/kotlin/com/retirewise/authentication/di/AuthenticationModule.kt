package com.retirewise.authentication.di

import com.retirewise.authentication.data.InMemoryConsentRepository
import com.retirewise.authentication.data.NotConfiguredAuthRepository
import com.retirewise.authentication.domain.AuthRepository
import com.retirewise.authentication.domain.ConsentRepository
import org.koin.dsl.module

/**
 * Phase 3 client-side scaffold wiring — see [NotConfiguredAuthRepository] and
 * [InMemoryConsentRepository] doc comments for why these are the only
 * implementations that exist so far.
 */
val authenticationModule =
    module {
        single<AuthRepository> { NotConfiguredAuthRepository() }
        single<ConsentRepository> { InMemoryConsentRepository() }
    }
