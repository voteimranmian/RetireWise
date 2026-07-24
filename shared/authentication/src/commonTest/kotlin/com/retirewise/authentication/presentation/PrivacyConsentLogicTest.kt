package com.retirewise.authentication.presentation

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PrivacyConsentLogicTest {
    @Test
    fun continueIsDisabledUntilAccepted() {
        assertFalse(privacyConsentContinueEnabled(accepted = false))
    }

    @Test
    fun continueIsEnabledOnceAccepted() {
        assertTrue(privacyConsentContinueEnabled(accepted = true))
    }
}
