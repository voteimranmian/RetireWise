package com.retirewise.retirementengine.domain.formula

import com.retirewise.core.value.Money
import com.retirewise.retirementengine.domain.AccountBalances
import kotlin.test.Test
import kotlin.test.assertEquals

class ContributionFormulasTest {
    @Test
    fun workingYearAddsEmployeeAndEmployerContributionsToRrspOrRrif() {
        val balances = AccountBalances(rrspOrRrif = Money.ofDollars(1000.0))

        val result =
            applyContributions(
                balances = balances,
                employeeContribution = Money.ofDollars(500.0),
                employerContribution = Money.ofDollars(200.0),
                isWorkingYear = true,
            )

        assertEquals(Money.ofDollars(1700.0), result.rrspOrRrif)
    }

    @Test
    fun nonWorkingYearLeavesBalancesUnchanged() {
        val balances = AccountBalances(rrspOrRrif = Money.ofDollars(1000.0), tfsa = Money.ofDollars(300.0))

        val result =
            applyContributions(
                balances = balances,
                employeeContribution = Money.ofDollars(500.0),
                employerContribution = Money.ofDollars(200.0),
                isWorkingYear = false,
            )

        assertEquals(balances, result)
    }

    @Test
    fun contributionsLeaveTfsaAndNonRegisteredUntouched() {
        val balances =
            AccountBalances(
                rrspOrRrif = Money.ZERO,
                tfsa = Money.ofDollars(100.0),
                nonRegistered = Money.ofDollars(50.0),
            )

        val result =
            applyContributions(
                balances = balances,
                employeeContribution = Money.ofDollars(10.0),
                employerContribution = Money.ZERO,
                isWorkingYear = true,
            )

        assertEquals(Money.ofDollars(100.0), result.tfsa)
        assertEquals(Money.ofDollars(50.0), result.nonRegistered)
    }
}
