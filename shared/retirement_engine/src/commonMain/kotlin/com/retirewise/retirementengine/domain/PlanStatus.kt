package com.retirewise.retirementengine.domain

/**
 * Whether a projection year's known income sources cover expenses without
 * exhausting all tracked accounts. See
 * [com.retirewise.retirementengine.domain.formula.withdrawToCoverDeficit].
 */
enum class PlanStatus {
    FUNDED,
    DEPLETED,
}
