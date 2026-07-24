package com.retirewise.authentication.data

import com.retirewise.authentication.domain.AuthProvider
import com.retirewise.authentication.domain.AuthRepository
import com.retirewise.authentication.domain.AuthResult

/**
 * Phase 3 client-side scaffold: this app has no backend account service and
 * no Apple/Google OAuth credentials configured in this environment, so every
 * provider is reported as [AuthResult.NotConfigured] rather than silently
 * granting a fake session. Replace with real implementations (backend-backed
 * email/password, Sign in with Apple, Google Sign-In) once that
 * infrastructure exists — see docs/RELEASE_PLAN.md Phase 3.
 */
class NotConfiguredAuthRepository : AuthRepository {
    override suspend fun signIn(provider: AuthProvider): AuthResult = AuthResult.NotConfigured(provider)
}
