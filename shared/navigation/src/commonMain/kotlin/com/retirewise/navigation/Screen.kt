package com.retirewise.navigation

/**
 * Top-level, pre-application-shell navigation model: which of the app's
 * "outer" screens is showing. Once inside [MainApp], navigation between the
 * five primary destinations is handled by [MainAppScaffold]'s NavHost
 * (see ADR 0003), not by this sealed interface.
 */
sealed interface Screen {
    data object Welcome : Screen

    /**
     * Phase 3 account creation (docs/RELEASE_PLAN.md), reached from either of
     * Welcome's two entry actions. [startDestination] is threaded through the
     * whole Create Account → Privacy Consent → AI Consent flow so the
     * originally requested tab is honoured once [MainApp] is finally reached.
     */
    data class CreateAccount(val startDestination: Destination) : Screen

    /** Required privacy consent, reached after [CreateAccount]. */
    data class PrivacyConsent(val startDestination: Destination) : Screen

    /** Optional AI data consent, reached after [PrivacyConsent]. */
    data class AiConsent(val startDestination: Destination) : Screen

    /**
     * The Phase 2 application shell (docs/RELEASE_PLAN.md), reached once the
     * account/consent flow above completes.
     */
    data class MainApp(val startDestination: Destination = Destination.Today) : Screen

    /**
     * Phase 1 exit criteria: a screen exercising every shared/design_system
     * component, reachable from Welcome. This is scaffolding for design
     * system verification, not a real product screen — it should be
     * removed or moved behind a proper debug menu once a real settings/debug
     * surface exists.
     */
    data object DesignSystemShowcase : Screen
}
