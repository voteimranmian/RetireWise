package com.retirewise.scenarioengine.domain

import com.retirewise.core.value.Money

/**
 * A nullable-field patch over
 * [com.retirewise.retirementengine.domain.ProjectionRequest.Ready] (and, for
 * the two CPP/OAS fields, its nested `Assumptions`). Each non-null field
 * represents one applied lever; multiple non-null fields let a single
 * scenario combine levers (e.g. "delay retirement" + "increase savings")
 * without needing a `List` wrapper — chosen over a sealed-per-lever
 * `ScenarioChange` type for this reason (see
 * docs/ADR/0008-scenario-planning-scope-and-comparison-metrics-for-phase-7.md).
 *
 * Maps 6 of docs/PRD.md section 8.3's 9 scenario types onto existing engine
 * inputs. "Pay off mortgage", "downsize home", and "work part time" have no
 * field here — they need debt-payoff, housing-equity, and partial-income
 * modeling not present in `ProjectionRequest.Ready`. This is a documented
 * gap, not an oversight.
 */
data class ScenarioChangeSet(
    /** "Retire earlier" (lower value) / "Delay retirement" (higher value). */
    val retirementAge: Int? = null,
    /** "Delay CPP". */
    val cppStartAge: Int? = null,
    /** "Delay OAS". */
    val oasStartAge: Int? = null,
    /** "Increase savings". */
    val employeeAnnualContribution: Money? = null,
    /** "Change retirement spending". */
    val targetAnnualSpending: Money? = null,
) {
    /** True when every field is null — applying this to a base plan is a no-op. */
    fun isIdentity(): Boolean =
        retirementAge == null &&
            cppStartAge == null &&
            oasStartAge == null &&
            employeeAnnualContribution == null &&
            targetAnnualSpending == null
}
