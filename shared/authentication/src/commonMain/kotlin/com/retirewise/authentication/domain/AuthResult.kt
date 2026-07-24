package com.retirewise.authentication.domain

/** The outcome of [AuthRepository.signIn]. */
sealed interface AuthResult {
    data class Success(val session: AuthSession) : AuthResult

    /**
     * [provider] has no real implementation wired up yet — no backend
     * account service or Apple/Google OAuth credentials exist in this
     * environment (see docs/ADR — Phase 3 client-side scaffold). This is
     * distinct from [Failure]: it is an expected, permanent state of the
     * current build rather than a transient error.
     */
    data class NotConfigured(val provider: AuthProvider) : AuthResult

    data class Failure(val message: String) : AuthResult
}
