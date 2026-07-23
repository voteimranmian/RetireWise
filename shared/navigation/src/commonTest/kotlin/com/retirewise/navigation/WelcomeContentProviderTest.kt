package com.retirewise.navigation

import kotlin.test.Test
import kotlin.test.assertTrue

class WelcomeContentProviderTest {
    @Test
    fun contentHasNonBlankCopyForAllFields() {
        val content = WelcomeContentProvider().content()

        assertTrue(content.badgeLabel.isNotBlank())
        assertTrue(content.headline.isNotBlank())
        assertTrue(content.supportingMessage.isNotBlank())
        assertTrue(content.primaryActionLabel.isNotBlank())
        assertTrue(content.secondaryActionLabel.isNotBlank())
    }
}
