package com.retirewise.core

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformTest {
    @Test
    fun platformNameIsNotBlank() {
        assertTrue(currentPlatform().name.isNotBlank())
    }
}
