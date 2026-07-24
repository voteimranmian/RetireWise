package com.retirewise.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * Root theme wrapper. Feature screens read colours via
 * `RetireWiseTheme.colors` (through [LocalRetireWiseColors]) rather than
 * hardcoding colour values, per docs/DESIGN_SYSTEM.md section 18.3.
 *
 * Dark theme tokens are not yet defined (Phase 1) — only the light palette
 * exists so far.
 */
@Composable
fun RetireWiseTheme(content: @Composable () -> Unit) {
    val colors = LightRetireWiseColors
    val typography = rememberRetireWiseTypography()

    val materialColorScheme =
        lightColorScheme(
            primary = colors.primary,
            secondary = colors.secondary,
            tertiary = colors.tertiary,
            background = colors.background,
            surface = colors.surface,
            error = colors.critical,
        )

    val materialTypography =
        Typography(
            displayLarge = typography.displayLarge,
            headlineLarge = typography.headlineLarge,
            headlineMedium = typography.headlineMedium,
            bodyLarge = typography.bodyLarge,
            bodyMedium = typography.bodyMedium,
            labelLarge = typography.labelLarge,
            labelSmall = typography.labelSmall,
        )

    CompositionLocalProvider(
        LocalRetireWiseColors provides colors,
        LocalRetireWiseTypography provides typography,
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = materialTypography,
            content = content,
        )
    }
}

object RetireWiseTheme {
    val colors: RetireWiseColors
        @Composable
        get() = LocalRetireWiseColors.current

    val typography: RetireWiseTypography
        @Composable
        get() = LocalRetireWiseTypography.current

    val spacing get() = RetireWiseSpacing
}
