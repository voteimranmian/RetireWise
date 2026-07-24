# ADR 0003: Navigation Compose Multiplatform for app navigation

## Context

`docs/ARCHITECTURE.md` section 14.1 named "Voyager (or an approved Compose Multiplatform navigation solution)" as the mobile navigation library, and a Phase 0 status note claimed it was "wired in" alongside Koin. In practice no navigation library was ever added as a Gradle dependency — Phase 0 and Phase 1 shipped with a minimal hand-written `Screen` sealed interface (see `shared/navigation/src/commonMain/kotlin/com/retirewise/navigation/Screen.kt`), which was explicitly documented as a placeholder "until a real navigation solution is introduced in Phase 2 ... once the five primary destinations exist."

Phase 2 (Application shell) now introduces exactly that: five primary destinations (Today, Plan, Explore, Learn, Ask AI) with tab-style navigation, and later phases (Explore's scenario detail, Learn's article detail, Plan's category detail) will need push/back-stack navigation within those tabs. This is the point at which a real navigation library is needed, and the choice must be made deliberately rather than defaulting to what an earlier doc note assumed.

## Decision

Use `org.jetbrains.androidx.navigation:navigation-compose` (the official JetBrains/Google-maintained Compose Multiplatform port of AndroidX Navigation), currently at stable release 2.9.2, for all in-app navigation (tab-level and push/back-stack navigation).

## Alternatives considered

- **Voyager (as originally documented)**: purpose-built for Compose Multiplatform and lightweight, but its multiplatform releases have remained in beta (`1.1.0-beta03` as of the most recent release) for a long time with no stable 1.x release. For an app whose core structure depends on navigation working correctly for years, betting on a community-maintained library that hasn't reached stability is a risk we'd rather not carry.
- **Custom sealed-interface navigation (the current Phase 0/1 placeholder)**: adequate for a single screen and a couple of placeholders, but does not scale to five tabs each with their own back stack, deep linking, or state restoration without reimplementing a large portion of what a navigation library already solves.
- **androidx.navigation-compose (chosen)**: officially stable, maintained jointly by JetBrains and Google specifically for Compose Multiplatform, and already aligned with `androidx-lifecycle-viewmodel`/`androidx-lifecycle-runtimeCompose`, which are already declared in `gradle/libs.versions.toml` for future ViewModel-based presentation layers (per `docs/ARCHITECTURE.md` section 15).

## Advantages

1. Stable, first-party-supported release — not a long-running beta.
2. Consistent with the `domain/data/presentation/di/test` module structure already planned: `NavController` composes naturally with per-screen ViewModels via `androidx.lifecycle-viewmodel`.
3. Single navigation model can grow from today's flat tab navigation into nested back stacks (Explore, Learn, Plan detail screens) without a library migration later.
4. Well documented migration path to Navigation 3 (`org.jetbrains.androidx.navigation3`) if the project later wants type-safe, state-driven navigation — not required now, avoiding premature complexity.

## Risks

1. Compose Multiplatform's officially supported navigation-compose artifact is newer to iOS than Android; accessibility and back-gesture parity should be explicitly verified on iOS as more push navigation is added in later phases.
2. String-route based navigation (used for Phase 2, since destinations take no arguments yet) will need to move to type-safe routes (requiring the `kotlinx-serialization` plugin) once destinations start taking arguments (e.g. a specific scenario ID) — tracked as follow-up work for the phase that introduces it, not added speculatively now.

## Consequences

`shared/navigation` depends on `org.jetbrains.androidx.navigation:navigation-compose`. `docs/ARCHITECTURE.md` section 14.1 is updated to reflect this decision instead of Voyager. The Phase 2 navigation shell (`shared/navigation/.../MainAppScaffold.kt`) is the single owner of the `NavHost` and bottom navigation bar for the five primary destinations.

## Review date

Revisit if a Phase 2+ screen needs typed navigation arguments, or after Phase 7 (scenario planning) ships, once real push-navigation usage patterns are known.
