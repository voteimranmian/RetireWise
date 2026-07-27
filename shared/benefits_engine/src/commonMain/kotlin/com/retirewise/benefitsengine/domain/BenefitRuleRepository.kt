package com.retirewise.benefitsengine.domain

import com.retirewise.core.value.Money
import com.retirewise.core.value.Rate
import kotlinx.datetime.LocalDate

/**
 * Source of [BenefitRuleSet]s. An interface (rather than a bare object)
 * so a specific historical rule-set version can be supplied to
 * `RetirementProjectionEngine.project()` later, keeping old reports
 * reproducible (docs/FINANCIAL_RULES.md section 11.5).
 */
interface BenefitRuleRepository {
    fun current(): BenefitRuleSet
}

/**
 * The one rule set implemented so far. CPP/OAS monthly actuarial-adjustment
 * percentages are long-standing, structural Service Canada rules. The GIS
 * maximum amount and income cutoff are quarterly-indexed dollar figures;
 * direct `canada.ca` retrieval returned HTTP 403 in this environment, so
 * these two figures are **secondary-sourced** (aggregator sites quoting the
 * same canada.ca statistics) and are pending official re-verification
 * before this rule set should be treated as authoritative for production
 * use. See docs/ADR/0007-government-benefit-scope-and-sourcing-for-phase-6.md.
 */
object CanadaBenefitRuleRepositoryV1 : BenefitRuleRepository {
    private val ruleSet =
        BenefitRuleSet(
            ruleVersion = "CANADA_BENEFITS_V1",
            sourceDescription =
                "CPP/OAS monthly adjustment rates: Service Canada statutory " +
                    "rules (structural, not indexed). GIS max amount and income " +
                    "cutoff: secondary-sourced from aggregator sites quoting " +
                    "canada.ca 2026 figures (direct canada.ca retrieval returned " +
                    "HTTP 403 in this environment) — pending official " +
                    "re-verification against canada.ca before production use.",
            sourceVerifiedDate = LocalDate(2026, 7, 27),
            cppEarlyReductionPerMonth = Rate.ofPercent(0.6),
            cppLateIncreasePerMonth = Rate.ofPercent(0.7),
            oasLateIncreasePerMonth = Rate.ofPercent(0.6),
            gisMaxAnnualAmountSingle = Money.ofDollars(1109.85 * 12),
            gisIncomeCutoffSingle = Money.ofDollars(22512.0),
        )

    override fun current(): BenefitRuleSet = ruleSet
}
