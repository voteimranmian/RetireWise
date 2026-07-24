# ADR 0005: Design system v2 — full colour ramp, Inter typeface, Material icons, soft-depth cards

## Context

Phase 0/1 shipped `shared/design_system` as a deliberately minimal placeholder: 17 flat colour tokens, no custom typeface (platform default only), no icon library, and plain bordered cards with no shadow (`docs/DESIGN_SYSTEM.md` sections 18.3/18.4, Phase 0/1 status notes). Separately, three HTML/Tailwind mockups (Welcome, Goal Selection/Explore, Today Dashboard) were produced defining a richer, already-thought-through visual language: a Material-3-style colour ramp (evergreen primary, warm-gold tertiary, layered surface-container tones), the Inter typeface, Material Symbols-style icons, and soft-shadow cards/bento grids. That mockup code was static HTML only — never implemented in Kotlin/Compose.

This ADR covers bringing the mockups' *visual language* (tokens, typography, icons, card styling) into the real design system and applying it to the three screens that already exist (Welcome, Explore, Today) — not a re-creation of the mockups' literal content. Two things are deliberately not carried over: Today Dashboard's fabricated financial figures (blocked on Phase 4/5's profile and retirement engine, per CLAUDE.md rule 15/non-negotiables) and Welcome's fabricated social-proof copy/avatars.

## Decision

1. Correct and extend `RetireWiseColors` with the token set the three mockups actually use (fixed/container/on-* variants for primary/secondary/tertiary, additional surface-container tones, `outline`, `successSoft`), replacing a token error where `primaryContainer` held the mockups' `primary-fixed` value.
2. Bundle the Inter typeface (Regular/Medium/SemiBold/Bold, OFL-1.1) via Compose Multiplatform Resources (`org.jetbrains.compose.resources`, part of the already-used Compose Multiplatform 1.9.3 plugin) and wire it into every `RetireWiseTypography` style, replacing the platform default font.
3. Add `org.jetbrains.compose.material:material-icons-extended` and use `Icons.Filled.*` directly at call sites for the ~20 glyphs actually needed across Welcome/Explore/Today.
4. Add a soft drop shadow to `RetireWiseCard` (2dp elevation, primary-tinted ambient/spot colour) approximating the mockups' `soft-depth`/`active-elevation` treatment.
5. Fix `displayLarge` to the mockups' mobile value (36sp/44sp/-0.02em/700) rather than their desktop value (48sp), since this app's primary surface is a phone.

## Alternatives considered

- **Webfont-based icons (the mockups' actual approach, Material Symbols as a web font)**: doesn't fit Compose's `ImageVector` model; would require a custom font-glyph rendering shim with no first-party support.
- **No custom typeface (keep platform default)**: simplest, but abandons a deliberate, already-validated part of the mockups' visual identity (Inter is a widely used, highly legible UI typeface) for no real benefit — platform-default fonts differ across Android/iOS, which the mockups' consistent look was specifically trying to avoid.
- **Full raw Material-3 token dump (every token Material3's `ColorScheme` defines)**: rejected as speculative — only tokens with an actual current use in the three mockups are added, consistent with "no speculative abstractions."
- **Hand-rolled vector icon assets**: avoids the new dependency, but not worth the effort/maintenance for ~20 icons when a maintained, same-publisher (JetBrains) Compose Multiplatform artifact already covers them.

## Advantages

1. Closes the gap between the (already-approved-in-spirit) mockup visual language and the real, shipped Compose UI.
2. Inter and the icon set are both officially maintained multiplatform-safe artifacts (Compose Resources embeds fonts into both the Android APK and the iOS resource bundle automatically; `material-icons-extended` is JetBrains-published, same as `compose.material3`/`compose.ui` already in use).
3. Colour token corrections (`primaryContainer` fix, new fixed/container variants) remove a real bug (`primaryContainer` was previously the wrong mockup value) rather than just adding new tokens.
4. No behavioural or financial-data changes — this is presentation-layer only, keeping the deterministic retirement engine and empty-state gating (Today Dashboard) untouched.

## Risks

1. Bundling a font and an extended icon set increases binary size on both Android and iOS — acceptable for a consumer-facing retirement app where a polished, non-generic visual identity is a stated design goal (`docs/DESIGN_SYSTEM.md` section 18.1: "premium without feeling exclusive").
2. Inter's OFL-1.1 licence must be tracked (`THIRD_PARTY_NOTICES.md` / composeResources `font/NOTICE.md`) — this is the first bundled third-party asset in the repo, so licence-tracking precedent is being established here.
3. Material Symbols icon naming in Compose (`Icons.Filled.*`) could drift from Google's Material Symbols/Icons catalogue over time; low risk since only a fixed, small glyph set is referenced directly (no dynamic/string-based icon lookup).
4. No dark theme exists yet (`RetireWiseTheme` still has only a light `ColorScheme`) — new tokens are defined for light only, matching existing scope.

## Consequences

`shared/design_system` gains a dependency on `compose.components.resources` and `compose.materialIconsExtended`, and a `composeResources/font/` directory with four Inter `.ttf` files plus a licence notice. `RetireWiseColors`, `RetireWiseTypography`, `RetireWiseTheme`, `RetireWiseCard`, `RetireWiseButton` (new optional `trailingIcon`), and `RetireWiseEmptyState` (new optional `icon`) all change. `WelcomeScreen`, `ExploreScreen`, and `TodayScreen` are restyled with the new tokens/typography/icons; `ExploreScreen` additionally becomes a two-column (compact: one-column) icon-fronted card grid, reusing the existing `usesNavigationRail` breakpoint. `docs/DESIGN_SYSTEM.md` sections 18.3/18.4 and the Phase 1/2 status notes are updated to describe the finalized tokens/typography instead of "recommended direction" placeholder language.

## Review date

Revisit when a dark theme is introduced (new token values will be needed for every fixed/container token added here), or when Phase 4/5 gives Today Dashboard real data and its layout is built out beyond the empty state.
