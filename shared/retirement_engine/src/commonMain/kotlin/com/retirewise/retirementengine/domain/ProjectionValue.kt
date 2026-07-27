package com.retirewise.retirementengine.domain

import com.retirewise.core.value.Money

/**
 * A projection output field that may not be computable yet. Used only for
 * the four docs/FINANCIAL_RULES.md section 11.3 rows this engine cannot
 * honestly calculate: government benefits, gross income, estimated taxes,
 * and after-tax income (CPP/OAS/GIS is Phase 6; income tax is Release two).
 *
 * A bare [Money.ZERO] would be indistinguishable from "we calculated this
 * and it is zero" — a false claim for, say, a retiree who will actually
 * receive CPP/OAS. [NotYetModeled] keeps that distinction explicit rather
 * than fabricating completeness (same honesty precedent as
 * `AuthResult.NotConfigured` in shared/authentication).
 */
sealed interface ProjectionValue {
    data class Known(val amount: Money) : ProjectionValue

    data class NotYetModeled(val reason: String) : ProjectionValue
}
