package com.retirewise.authentication.domain

/** An authenticated session, returned by [AuthRepository.signIn] on success. */
data class AuthSession(
    val userId: String,
    val provider: AuthProvider,
)
