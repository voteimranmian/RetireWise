package com.retirewise.scenariocomparison.domain

import com.retirewise.core.value.Money
import com.retirewise.scenarioengine.domain.ScenarioChangeSet

/** Which raw value type a [ScenarioLever]'s input form should collect. */
enum class LeverInputKind {
    AGE,
    MONEY,
}

/**
 * The 9 scenario types from docs/PRD.md section 8.3, in the same order
 * `ExploreScreen.kt`'s original `exploreScenarioTypes()` listed them. Each
 * entry carries whether it's supported this phase (non-null [inputKind])
 * and, if so, the raw input kind its picker form should collect. A `null`
 * [inputKind] marks one of the 3 scenario types genuinely deferred to
 * Release two (docs/ADR/0008) — "pay off mortgage", "downsize home", and
 * "work part time" all need debt-payoff, housing-equity, or partial-income
 * modeling that [com.retirewise.retirementengine.domain.ProjectionRequest.Ready]
 * does not have fields for.
 */
enum class ScenarioLever(val displayLabel: String, val inputKind: LeverInputKind?) {
    RETIRE_EARLIER("Retire earlier", LeverInputKind.AGE),
    DELAY_RETIREMENT("Delay retirement", LeverInputKind.AGE),
    DELAY_CPP("Delay CPP", LeverInputKind.AGE),
    DELAY_OAS("Delay OAS", LeverInputKind.AGE),
    INCREASE_SAVINGS("Increase savings", LeverInputKind.MONEY),
    PAY_OFF_MORTGAGE("Pay off mortgage", null),
    DOWNSIZE_HOME("Downsize home", null),
    WORK_PART_TIME("Work part time", null),
    CHANGE_RETIREMENT_SPENDING("Change retirement spending", LeverInputKind.MONEY),
}

/** True for the 6 of 9 levers this phase can actually build a [ScenarioChangeSet] for. */
fun ScenarioLever.isSupported(): Boolean = inputKind != null

/**
 * A raw value collected from a [LeverInputKind]-appropriate input control —
 * an age (an `Int`) or a dollar amount (a [Money]).
 */
sealed interface LeverInputValue {
    data class Age(val value: Int) : LeverInputValue

    data class Amount(val value: Money) : LeverInputValue
}

/**
 * Maps a supported [lever] and its raw [input] onto the single
 * [ScenarioChangeSet] field it represents, leaving every other field null.
 * Throws [IllegalArgumentException] for an unsupported lever or an [input]
 * kind that doesn't match the lever's [ScenarioLever.inputKind] — both are
 * caller bugs, not runtime user input we need to recover from gracefully.
 */
fun buildChangeSet(
    lever: ScenarioLever,
    input: LeverInputValue,
): ScenarioChangeSet {
    require(lever.isSupported()) { "${lever.name} is not a supported scenario lever this phase." }
    return when (lever) {
        ScenarioLever.RETIRE_EARLIER, ScenarioLever.DELAY_RETIREMENT ->
            ScenarioChangeSet(retirementAge = input.requireAge(lever))
        ScenarioLever.DELAY_CPP ->
            ScenarioChangeSet(cppStartAge = input.requireAge(lever))
        ScenarioLever.DELAY_OAS ->
            ScenarioChangeSet(oasStartAge = input.requireAge(lever))
        ScenarioLever.INCREASE_SAVINGS ->
            ScenarioChangeSet(employeeAnnualContribution = input.requireAmount(lever))
        ScenarioLever.CHANGE_RETIREMENT_SPENDING ->
            ScenarioChangeSet(targetAnnualSpending = input.requireAmount(lever))
        ScenarioLever.PAY_OFF_MORTGAGE, ScenarioLever.DOWNSIZE_HOME, ScenarioLever.WORK_PART_TIME ->
            error("Unreachable: ${lever.name} already failed the isSupported() check above.")
    }
}

private fun LeverInputValue.requireAge(lever: ScenarioLever): Int {
    require(this is LeverInputValue.Age) { "${lever.name} requires an age input, got $this." }
    return value
}

private fun LeverInputValue.requireAmount(lever: ScenarioLever): Money {
    require(this is LeverInputValue.Amount) { "${lever.name} requires a money input, got $this." }
    return value
}
