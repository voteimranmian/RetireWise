package com.retirewise.retirementengine.domain

/**
 * Marital/planning status for a projection. Only [SINGLE] is supported this
 * phase — modeling a married/common-law couple's combined finances is
 * explicitly "Couples planning" (docs/RELEASE_PLAN.md, Release two), so this
 * type exists to satisfy docs/FINANCIAL_RULES.md section 11.2's input list
 * honestly without pretending to support it yet.
 */
enum class MaritalStatus {
    SINGLE,
}
