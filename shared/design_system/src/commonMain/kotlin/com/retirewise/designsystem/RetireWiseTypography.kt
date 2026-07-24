package com.retirewise.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.retirewise.designsystem.generated.resources.Res
import com.retirewise.designsystem.generated.resources.inter_bold
import com.retirewise.designsystem.generated.resources.inter_medium
import com.retirewise.designsystem.generated.resources.inter_regular
import com.retirewise.designsystem.generated.resources.inter_semibold
import org.jetbrains.compose.resources.Font

/**
 * Typography tokens per docs/DESIGN_SYSTEM.md section 18.4: size, line
 * height, letter spacing, weight, and (as of design system v2) the Inter
 * typeface, bundled via Compose Resources
 * (see `shared/design_system/src/commonMain/composeResources/font`,
 * licensed under SIL OFL 1.1 — see `THIRD_PARTY_NOTICES.md`).
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

@Composable
private fun interFontFamily(): FontFamily =
    FontFamily(
        Font(Res.font.inter_regular, weight = FontWeight.Normal),
        Font(Res.font.inter_medium, weight = FontWeight.Medium),
        Font(Res.font.inter_semibold, weight = FontWeight.SemiBold),
        Font(Res.font.inter_bold, weight = FontWeight.Bold),
    )

@Composable
fun rememberRetireWiseTypography(): RetireWiseTypography {
    val inter = interFontFamily()
    return RetireWiseTypography(
        displayLarge =
            TextStyle(
                fontFamily = inter,
                // Mobile display size (the mockups' desktop-only 48sp variant is out of
                // scope — this app's primary surface is a phone, not a responsive website).
                fontSize = 36.sp,
                lineHeight = 44.sp,
                letterSpacing = (-0.02).em,
                fontWeight = FontWeight.Bold,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = inter,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = (-0.01).em,
                fontWeight = FontWeight.SemiBold,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = inter,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = inter,
                fontSize = 18.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Normal,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = inter,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Normal,
            ),
        labelLarge =
            TextStyle(
                fontFamily = inter,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.01.em,
                fontWeight = FontWeight.SemiBold,
            ),
        labelSmall =
            TextStyle(
                fontFamily = inter,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.02.em,
                fontWeight = FontWeight.Medium,
            ),
        financialLarge =
            TextStyle(
                fontFamily = inter,
                fontSize = 40.sp,
                lineHeight = 48.sp,
                letterSpacing = (-0.03).em,
                fontWeight = FontWeight.Medium,
            ),
    )
}

val LocalRetireWiseTypography =
    staticCompositionLocalOf<RetireWiseTypography> {
        error("RetireWiseTypography not provided — wrap content in RetireWiseTheme")
    }
