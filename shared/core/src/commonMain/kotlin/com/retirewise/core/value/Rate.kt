package com.retirewise.core.value

/**
 * A fractional rate used for economic assumptions (investment growth,
 * inflation, income growth) — e.g. `Rate(0.06)` for 6%.
 *
 * Backed by [Double], unlike [Money]. Rates are inherently approximate,
 * sourced assumptions rather than exact currency amounts, so ordinary
 * floating point precision is standard actuarial practice here and does not
 * need fixed-point treatment (see
 * docs/ADR/0006-money-and-decimal-representation-for-retirement-engine.md).
 */
data class Rate(val fraction: Double) {
    init {
        require(fraction.isFinite()) { "Rate must be a finite number, was $fraction." }
    }

    companion object {
        val ZERO = Rate(0.0)

        fun ofPercent(percent: Double): Rate = Rate(percent / 100.0)
    }
}
