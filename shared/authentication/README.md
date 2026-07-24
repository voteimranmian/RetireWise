# shared/authentication

Phase 3 (Authentication and consent) client-side scaffold, per docs/RELEASE_PLAN.md.

A real Gradle module with domain, data, presentation, di, and test layers (docs/ARCHITECTURE.md section 15). It is wired into settings.gradle.kts and into `shared/navigation`'s Create Account → Privacy Consent → AI Consent flow.

This is a scaffold, not a finished feature: there is no backend account service and no real Apple/Google/Firebase OAuth integration yet.

- `NotConfiguredAuthRepository` (data layer) honestly reports `AuthResult.NotConfigured` for every `AuthProvider` instead of faking a successful sign-in.
- `InMemoryConsentRepository` (data layer) records consent only in memory; it does not persist across app restarts.

Both are meant to be replaced once real backend/OAuth infrastructure exists, without changing the `domain` interfaces (`AuthRepository`, `ConsentRepository`) or the presentation layer that depends on them.
