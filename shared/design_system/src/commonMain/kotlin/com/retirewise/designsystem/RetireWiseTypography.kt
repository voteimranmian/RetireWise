package com.retirewise.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * Typography tokens per docs/DESIGN_SYSTEM.md section 18.4. These specify
 * size, line height, letter spacing, and weight only; the platform default
 * system font family is used (no custom webfont is bundled in Phase 0).
 */
@Immutable
data class RetireWiseTypography(
    val displayLarge: TextStyle,
    val headlineLarge: TextStyle,
    val headlineMedium: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val labelLarge: TextStyle,
    val labelSmall: TextStyle,
    val financialLarge: TextStyle,
)

val LightRetireWiseTypography =
    RetireWiseTypography(
        displayLarge =
            TextStyle(
                fontSize = 48.sp,
                lineHeight = 56.sp,
                letterSpacing = (-0.02).em,
                fontWeight = FontWeight.Bold,
            ),
        headlineLarge =
            TextStyle(
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = (-0.01).em,
                fontWeight = FontWeight.SemiBold,
            ),
        headlineMedium =
            TextStyle(
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        bodyLarge =
            TextStyle(
                fontSize = 18.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Normal,
            ),
        bodyMedium =
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Normal,
            ),
        labelLarge =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.01.em,
                fontWeight = FontWeight.SemiBold,
            ),
        labelSmall =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.02.em,
                fontWeight = FontWeight.Medium,
            ),
        financialLarge =
            TextStyle(
                fontSize = 40.sp,
                lineHeight = 48.sp,
                letterSpacing = (-0.03).em,
                fontWeight = FontWeight.Medium,
            ),
    )

val LocalRetireWiseTypography = staticCompositionLocalOf { LightRetireWiseTypography }
