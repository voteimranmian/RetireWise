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
    val primaryFixed: Color,
    val onPrimaryFixed: Color,
    val primaryFixedDim: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val tertiaryContainer: Color,
    val tertiaryFixed: Color,
    val onTertiaryFixed: Color,
    val tertiaryFixedDim: Color,
    val background: Color,
    val surface: Color,
    val surfaceRaised: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val outline: Color,
    val success: Color,
    // Lighter, decorative-only accent green (icon fills, chart bars, progress rings).
    // Contrast against [background]/[surface] is not text-safe — never use for body text
    // or labels; use [success] there instead (see docs/DESIGN_SYSTEM.md section 18.5).
    val successSoft: Color,
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
        primaryContainer = Color(0xFF0F3D2E),
        primaryFixed = Color(0xFFBEEDD7),
        onPrimaryFixed = Color(0xFF002116),
        primaryFixedDim = Color(0xFFA2D1BB),
        onPrimaryContainer = Color(0xFF7BA894),
        secondary = Color(0xFF426464),
        secondaryContainer = Color(0xFFC2E7E6),
        onSecondaryContainer = Color(0xFF466969),
        tertiary = Color(0xFF735C00),
        tertiaryContainer = Color(0xFFCBA72F),
        tertiaryFixed = Color(0xFFFFE088),
        onTertiaryFixed = Color(0xFF241A00),
        tertiaryFixedDim = Color(0xFFE9C349),
        background = Color(0xFFFBF9F4),
        surface = Color(0xFFFFFFFF),
        surfaceRaised = Color(0xFFF5F3EE),
        surfaceContainer = Color(0xFFF0EEE9),
        surfaceContainerHigh = Color(0xFFEAE8E3),
        textPrimary = Color(0xFF1B1C19),
        textSecondary = Color(0xFF414944),
        outline = Color(0xFF717974),
        success = Color(0xFF2E7D32),
        successSoft = Color(0xFF88B04B),
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
