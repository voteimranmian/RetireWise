package com.retirewise.retirementengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.retirementengine.domain.AccountBalances

const val FORMULA_CONTRIBUTION_FLAT_ANNUAL_V1 = "CONTRIBUTION_FLAT_ANNUAL_V1"

/**
 * Adds one year's employee and employer contributions to the RRSP/RRIF
 * bucket. Onboarding does not collect a per-account breakdown, so all
 * contributions and [com.retirewise.profile.domain.Profile.retirementSavings]
 * are modeled against the single registered-savings bucket
 * ([AccountBalances.rrspOrRrif]); TFSA and non-registered start at zero and
 * only grow via [GrowthFormulas] or receive money via withdrawals in
 * reverse. No contributions are added once retired ([isWorkingYear] is
 * false).
 */
fun applyContributions(
    balances: AccountBalances,
    employeeContribution: Money,
    employerContribution: Money,
    isWorkingYear: Boolean,
): AccountBalances {
    if (!isWorkingYear) return balances
    return balances.copy(
        rrspOrRrif = balances.rrspOrRrif + employeeContribution + employerContribution,
    )
}
