package com.retirewise.authentication.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class AuthProviderTest {
    @Test
    fun everyProviderHasAButtonLabel() {
        assertEquals(
            listOf("Continue with Apple", "Continue with Google", "Continue with email"),
            AuthProvider.entries.map { it.buttonLabel },
        )
    }
}
