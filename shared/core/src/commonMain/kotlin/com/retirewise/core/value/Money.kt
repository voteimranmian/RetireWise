package com.retirewise.core.value

import kotlin.jvm.JvmInline
import kotlin.math.abs
import kotlin.math.roundToLong

/**
 * A Canadian-dollar amount (docs/FINANCIAL_RULES.md section 11.4), stored as
 * an exact fixed-point [Long] of minor units where 1 minor unit = 1/10,000 of
 * a dollar (four implied decimal digits, not the usual two for cents).
 *
 * KMP has no `BigDecimal` equivalent available on non-JVM targets (iOS
 * native), and floating point currency accumulates visible rounding drift
 * over a 40+ year annual projection. A fixed-point [Long] is exact on every
 * platform and needs no external dependency. See
 * docs/ADR/0006-money-and-decimal-representation-for-retirement-engine.md.
 *
 * The extra two digits of precision beyond cents exist only to absorb
 * compounding rounding error internally; [roundedToCents] is the single
 * point where a value is rounded down to display/report precision, using
 * HALF_UP (round half away from zero).
 *
 * CAD-only for now — no [Currency] type exists yet, since nothing in this
 * engine requires another currency.
 */
@JvmInline
value class Money private constructor(val minorUnits: Long) : Comparable<Money> {
    operator fun plus(other: Money): Money = Money(minorUnits + other.minorUnits)

    operator fun minus(other: Money): Money = Money(minorUnits - other.minorUnits)

    operator fun unaryMinus(): Money = Money(-minorUnits)

    operator fun times(rate: Rate): Money = Money((minorUnits * rate.fraction).roundToLong())

    override fun compareTo(other: Money): Int = minorUnits.compareTo(other.minorUnits)

    fun coerceAtLeast(minimum: Money): Money = Money(minorUnits.coerceAtLeast(minimum.minorUnits))

    /** Rounds to whole cents using HALF_UP (round half away from zero). */
    fun roundedToCents(): Long {
        val quotient = minorUnits / MINOR_UNITS_PER_CENT
        val remainder = minorUnits % MINOR_UNITS_PER_CENT
        val roundsUp = abs(remainder) * 2 >= MINOR_UNITS_PER_CENT
        return if (roundsUp) quotient + if (minorUnits >= 0) 1 else -1 else quotient
    }

    /** For interop with legacy `Double` fields (e.g. [com.retirewise.profile.domain.Profile]) and tests. */
    fun toDollarDouble(): Double = minorUnits.toDouble() / MINOR_UNITS_PER_DOLLAR

    companion object {
        private const val MINOR_UNITS_PER_DOLLAR = 10_000L
        private const val MINOR_UNITS_PER_CENT = 100L

        val ZERO = Money(0L)

        fun ofDollars(dollars: Double): Money = Money((dollars * MINOR_UNITS_PER_DOLLAR).roundToLong())

        fun ofMinorUnits(minorUnits: Long): Money = Money(minorUnits)
    }
}
