# ADR 0006: Fixed-point `Money`/`Rate` value types and `kotlinx-datetime` for the retirement engine

## Context

`docs/FINANCIAL_RULES.md` section 11.4 requires financial precision using a decimal type, not floating point. `shared/retirement_engine` (Phase 5, `docs/RELEASE_PLAN.md`) is the first module to perform real currency arithmetic — compounding contributions, growth, and inflation year over year for up to 60+ years per user. Kotlin Multiplatform has no `java.math.BigDecimal` (or `java.time`) equivalent available on non-JVM targets (iOS/Native); `BigDecimal` is JVM-only, and the existing `Profile`/`RetirementGoal` domain types store money as raw nullable `Double`, which is unsuitable for exact compounding math (floating-point rounding error accumulates across 40+ annual compounding steps).

## Decision

Introduce two new value types in `shared/retirement_engine/domain/value/`:

- **`Money`** — a `@JvmInline value class` wrapping a `Long` in **minor units = 1/10,000 of a dollar** (4 implied decimal places, not the usual 2). All arithmetic (`plus`, `minus`, `unaryMinus`, `times(Rate)`, `compareTo`) operates on the exact `Long`. Rounding to display cents happens only at the output boundary, via `roundedToCents()` (documented HALF_UP), never during intermediate calculation. The extra two decimal places of headroom exist specifically to absorb the residual imprecision introduced by `Rate` (see below) without that imprecision compounding into cent-level drift over decades of annual growth/inflation steps.
- **`Rate`** — a simple `Double`-backed fraction (e.g. `0.05` for 5%). Rates are approximate economic assumptions (return, inflation, income growth), not currency amounts; using `Double` here is standard actuarial/financial-modeling practice and does not need fixed-point treatment.

Also add **`kotlinx-datetime`** (`0.6.1`) to `gradle/libs.versions.toml`, since `CalculationMetadata.calculationDate` needs a `LocalDate` type available on all KMP targets, and `java.time.LocalDate` has the same JVM-only problem as `BigDecimal`.

## Alternatives considered

- **`ionspin/kotlin-multiplatform-bignum`** (arbitrary-precision decimal, KMP-compatible): would give textbook `BigDecimal`-equivalent semantics. Rejected per CLAUDE.md rule 10 (no third-party dependency without documented need) — a fixed-point `Long` fully satisfies FINANCIAL_RULES 11.4's "decimal type, not floating point" requirement for this engine's actual range of values (dollar amounts well within `Long` range even at 4 implied decimals), so the added dependency weight and API surface is unjustified.
- **Raw `Double` for `Money`** (status quo in `Profile`/`RetirementGoal`): rejected outright — floating-point addition/multiplication is not exact, and this engine's entire purpose is repeated annual compounding, which is precisely where floating-point error accumulates worst.
- **2-decimal (cent-precision) fixed point**: rejected in favor of 4 implied decimals, to leave headroom for `Rate * Money` multiplication (which is not exact even with fixed-point `Money`, since `Rate` is a `Double`) without that per-operation rounding visibly drifting the displayed cents over a 60-year projection.
- **`java.time.LocalDate` / JVM-only date handling**: not available on iOS/Native targets at all; not a real option for a `commonMain` type.

## Advantages

1. Exact, deterministic `Long` arithmetic for all money storage and addition/subtraction — no floating-point rounding in the parts of the calculation that must be exact.
2. No new third-party dependency for currency math; `kotlinx-datetime` is a well-established, small, official JetBrains KMP library used only for the one JVM-only type (`LocalDate`) actually needed.
3. Same `Money` type works unmodified across Android and iOS targets.
4. Rounding to cents is a single, explicit, documented operation (`roundedToCents()`) at the display boundary, making "when do we round" auditable rather than implicit.

## Risks

1. **`Money * Rate` is not exact** (`Rate` is a `Double`): each such multiplication (contribution growth, inflation) introduces a small floating-point error before rounding back to `Long` minor units. This is bounded per-operation (well under one hundred-thousandth of a dollar) and further bounded by rounding at each step, so it cannot accumulate into visible cent drift over the engine's supported 60+ year horizon — but it means `Money` is not a strict, byte-exact fixed-point decimal end to end; it is fixed-point storage with a `Double`-derived compounding step, which is a documented, deliberate approximation rather than the theoretical ideal.
2. **CAD-only**: `Money` has no `Currency` field. Multi-currency support (if ever needed) would require a breaking change to this type.
3. **No arbitrary precision**: extremely large balances (far beyond any realistic personal retirement account) could theoretically overflow `Long` minor units; not a practical concern at this product's scale.

## Consequences

All new `shared/retirement_engine` domain and formula code uses `Money`/`Rate` exclusively for currency and rate values; no `Double` dollar amounts appear in calculation code. Future modules that need to interoperate with the retirement engine (e.g. a future `shared/profile` migration away from raw `Double`, or a dashboard/reporting layer) will need to convert through `Money.ofDollars()` / `Money.toDollarDouble()` at their boundary until/unless `Profile`/`RetirementGoal` themselves are migrated to `Money` in a later phase (out of scope for Phase 5).

## Review date

Revisit if the product ever needs multi-currency support, or if a future phase's precision auditing surfaces measurable cent-level drift from the `Money * Rate` approximation described in Risk 1.
