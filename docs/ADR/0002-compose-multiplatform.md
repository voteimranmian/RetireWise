# ADR 0002: Compose Multiplatform for UI

## Context

Having chosen Kotlin Multiplatform (ADR 0001) for shared logic, we need a UI toolkit that can share as much presentation code as possible across iOS and Android, given the product's need for consistent, calm, accessible UI (see `docs/DESIGN_SYSTEM.md`) and a large number of similar data-entry and chart screens.

## Decision

Use Compose Multiplatform for all shared UI (screens, components, navigation), rendered natively on both Android (via Jetpack Compose) and iOS (via Compose's Skia-based renderer embedded in a `UIViewController`).

## Alternatives considered

- **SwiftUI + Jetpack Compose (two separate UI codebases)**: best native idiom fidelity per platform, but every screen, chart, and form must be built and kept in sync twice — high cost given the product's dozens of similar planning screens and its youth as a product (fast iteration expected in early phases).
- **Compose Multiplatform (chosen)**: one shared UI codebase, one design system implementation, faster iteration across both platforms, at the cost of iOS UI feeling slightly less "native SwiftUI" by default — mitigated by the design system's restrained, custom visual language (docs/DESIGN_SYSTEM.md) rather than leaning on default OS chrome.

## Advantages

1. One implementation of every screen, form, and chart component.
2. Design system tokens (colour, type, spacing) defined once and guaranteed consistent across platforms.
3. Faster vertical-slice delivery, matching the phased build sequence in `docs/RELEASE_PLAN.md`.

## Risks

1. Compose Multiplatform's iOS target is newer than Android Compose; some platform capabilities (accessibility APIs, text input edge cases) may need `expect`/`actual` workarounds.
2. Requires Xcode to build/run the iOS target, adding toolchain weight.

## Consequences

`shared/design_system` is the single implementation of semantic colour tokens, typography, and spacing referenced in `docs/DESIGN_SYSTEM.md`. Feature UI must not hardcode colours/fonts and must consume this module. Accessibility (VoiceOver/TalkBack parity) must be explicitly tested on both platforms since it is not free with a shared toolkit.

## Review date

Revisit after Phase 1 (design system showcase) ships on both platforms.
