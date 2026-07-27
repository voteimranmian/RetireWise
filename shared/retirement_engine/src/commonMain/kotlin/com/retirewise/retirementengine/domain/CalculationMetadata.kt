package com.retirewise.retirementengine.domain

import kotlinx.datetime.LocalDate

/** Sentinel used for rule versions that don't exist yet — never null, never silently omitted. */
const val RULE_VERSION_NOT_IMPLEMENTED = "NOT_IMPLEMENTED"

/**
 * Formula-versioning metadata required by docs/FINANCIAL_RULES.md section
 * 11.5, so old reports can be reproduced and audited. Any change to formula
 * *behavior* must add a new entry to [formulaIdentifiers] and/or bump
 * [engineVersion] rather than silently editing a formula in place — this is
 * the mechanism that operationalizes CLAUDE.md's "never silently change
 * financial formulas."
 */
data class CalculationMetadata(
    val engineVersion: String,
    val assumptionSetVersion: String,
    val calculationDate: LocalDate,
    val formulaIdentifiers: Set<String>,
    val taxRuleVersion: String = RULE_VERSION_NOT_IMPLEMENTED,
    val governmentBenefitRuleVersion: String = RULE_VERSION_NOT_IMPLEMENTED,
)
