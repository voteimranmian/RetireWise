package com.retirewise.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Semantic colour tokens. Feature screens must reference these tokens via
 * [RetireWiseTheme] and must never hardcode colours directly
 * (see docs/DESIGN_SYSTEM.md section 18.3).
 */
@Immutable
data class RetireWiseColors(
    val primary: Color,
    val primaryContainer: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val surfaceRaised: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val success: Color,
    val caution: Color,
    val critical: Color,
    val information: Color,
    val divider: Color,
    val disabled: Color,
    val chartPositive: Color,
    val chartNeutral: Color,
    val chartNegative: Color,
)

val LightRetireWiseColors =
    RetireWiseColors(
        primary = Color(0xFF00261A),
        primaryContainer = Color(0xFFBEEDD7),
        secondary = Color(0xFF426464),
        background = Color(0xFFFBF9F4),
        surface = Color(0xFFFFFFFF),
        surfaceRaised = Color(0xFFF5F3EE),
        textPrimary = Color(0xFF1B1C19),
        textSecondary = Color(0xFF414944),
        success = Color(0xFF2E7D32),
        caution = Color(0xFFB8860B),
        critical = Color(0xFFB3261E),
        information = Color(0xFF35618E),
        divider = Color(0xFFC0C8C3),
        disabled = Color(0xFF717974),
        chartPositive = Color(0xFF2E7D32),
        chartNeutral = Color(0xFF8A8680),
        chartNegative = Color(0xFFB3261E),
    )

val LocalRetireWiseColors = staticCompositionLocalOf { LightRetireWiseColors }
