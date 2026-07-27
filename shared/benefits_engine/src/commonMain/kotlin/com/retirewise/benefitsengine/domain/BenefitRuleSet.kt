package com.retirewise.benefitsengine.domain

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import kotlinx.datetime.LocalDate

/**
 * A versioned, sourced set of government benefit program rules
 * (docs/FINANCIAL_RULES.md section 13.2's "government program record
 * structure"). Every dollar amount and rate here must trace back to
 * [sourceDescription] rather than being invented — see
 * docs/ADR/0007-government-benefit-scope-and-sourcing-for-phase-6.md.
 *
 * Covers only the statutory timing adjustments and the CPP/OAS/GIS
 * mechanics this engine models (docs/FINANCIAL_RULES.md section 11.6):
 * full earnings-history-based CPP calculation and residency-based OAS/GIS
 * eligibility are out of scope (Release two).
 */
data class BenefitRuleSet(
    val ruleVersion: String,
    val sourceDescription: String,
    val sourceVerifiedDate: LocalDate,
    // CPP: monthly actuarial adjustment relative to the standard age (65).
    val cppStandardAge: Int = 65,
    val cppMinStartAge: Int = 60,
    val cppMaxStartAge: Int = 70,
    val cppEarlyReductionPerMonth: Rate,
    val cppLateIncreasePerMonth: Rate,
    // OAS: deferral-only increase. No early-start option exists in reality.
    val oasStandardAge: Int = 65,
    val oasMinStartAge: Int = 65,
    val oasMaxStartAge: Int = 70,
    val oasLateIncreasePerMonth: Rate,
    // GIS: income-tested supplement, single-pensioner rates only (couples
    // rates are Release-two scope alongside couples planning generally).
    val gisMaxAnnualAmountSingle: Money,
    val gisIncomeCutoffSingle: Money,
)
