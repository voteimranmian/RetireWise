package com.retirewise.navigation.di

import com.retirewise.navigation.WelcomeContentProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Phase 0 dependency injection wiring. Feature modules add their own Koin
 * modules alongside this one as they are implemented (see docs/ARCHITECTURE.md
 * section 15 — each feature has its own `di` package).
 */
val appModule =
    module {
        factoryOf(::WelcomeContentProvider)
    }
