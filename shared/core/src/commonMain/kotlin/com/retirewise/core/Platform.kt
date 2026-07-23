package com.retirewise.core

/**
 * Marker for the running platform. Used only for non-financial, non-personal
 * diagnostic purposes (e.g. platform-specific bug reports), never for
 * calculations or analytics containing user data.
 */
expect class Platform {
    val name: String
}

expect fun currentPlatform(): Platform
