# ADR 0001: Kotlin Multiplatform for mobile

## Context

RetireWise ships to both iOS and Android with a large amount of shared, safety-critical logic (financial calculations, tax rules, government benefit rules). This logic must be identical on both platforms — divergence would produce different retirement numbers for the same user depending on device.

## Decision

Use Kotlin Multiplatform (KMP) as the mobile architecture, with shared modules for domain, data, and presentation logic, and thin platform-specific entry points (`apps/androidApp`, `apps/iosApp`).

## Alternatives considered

- **Fully native (Swift + Kotlin/Java separately)**: best per-platform polish and API access, but duplicates every financial formula in two languages/two codebases, doubling the risk of calculation divergence and the test burden.
- **React Native / Flutter**: cross-platform UI, but financial engine would need to run in JS/Dart, or be duplicated natively and bridged — extra complexity for the calculation-authority requirement, and weaker fit with a Kotlin-based backend (Ktor) for sharing code between client and server.
- **Kotlin Multiplatform (chosen)**: one shared calculation engine, one shared domain layer, native performance via Kotlin/Native on iOS, and the same language as the planned Ktor backend, allowing some shared code (e.g. serialization models, validation rules) between client and server.

## Advantages

1. Single source of truth for financial formulas across iOS and Android.
2. Shared test suite (golden financial test cases) runs identically on both platforms.
3. Same language as the backend (Kotlin/Ktor), reducing context-switching and enabling shared DTOs.
4. Mature `expect`/`actual` mechanism for the few platform-specific needs (biometrics, secure storage).

## Risks

1. Smaller community/tooling maturity than fully native or React Native.
2. iOS build requires Xcode and a Mac; CI must provision both toolchains.
3. Compose Multiplatform for iOS is comparatively newer than UIKit/SwiftUI — visual fidelity and platform idioms need explicit attention (see ADR 0002).

## Consequences

All feature modules follow the `domain / data / presentation / di / test` structure in `docs/ARCHITECTURE.md`. Financial rule modules (`retirement_engine`, `tax_engine`, `benefits_engine`) must have zero dependency on Compose or platform UI, so they can be tested headlessly in `commonTest` and reused unmodified by the backend if needed.

## Review date

Revisit after Phase 5 (retirement engine) ships, once real-world build/test performance and iOS UI fidelity can be assessed.
