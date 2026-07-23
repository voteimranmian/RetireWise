package com.retirewise.navigation

/**
 * Welcome screen copy, per docs/PRD.md section 19.1. Provided through Koin
 * (see [com.retirewise.navigation.di.appModule]) rather than hardcoded
 * directly in the composable, so localized/remote copy can replace it later
 * without changing the screen.
 */
data class WelcomeContent(
    val badgeLabel: String,
    val headline: String,
    val supportingMessage: String,
    val primaryActionLabel: String,
    val secondaryActionLabel: String,
)

class WelcomeContentProvider {
    fun content(): WelcomeContent =
        WelcomeContent(
            badgeLabel = "Built for Canadians",
            headline = "Plan your retirement with confidence",
            supportingMessage =
                "Get clear answers, personalized projections, and " +
                    "practical actions based on Canadian retirement programs.",
            primaryActionLabel = "Start my plan",
            secondaryActionLabel = "Ask a question",
        )
}
