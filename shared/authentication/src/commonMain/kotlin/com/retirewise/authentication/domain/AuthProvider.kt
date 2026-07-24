package com.retirewise.authentication.domain

/**
 * A sign-in method offered on [com.retirewise.authentication.presentation.CreateAccountScreen],
 * per docs/PRD.md section 7.1 (account creation, Sign in with Apple, Google
 * sign in, email sign in).
 */
enum class AuthProvider(val buttonLabel: String) {
    Apple(buttonLabel = "Continue with Apple"),
    Google(buttonLabel = "Continue with Google"),
    Email(buttonLabel = "Continue with email"),
}
