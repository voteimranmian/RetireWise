package com.retirewise.authentication.domain

/**
 * Authenticates a user with a given [AuthProvider]. See
 * [com.retirewise.authentication.data.NotConfiguredAuthRepository] for the
 * only implementation that currently exists.
 */
interface AuthRepository {
    suspend fun signIn(provider: AuthProvider): AuthResult
}
