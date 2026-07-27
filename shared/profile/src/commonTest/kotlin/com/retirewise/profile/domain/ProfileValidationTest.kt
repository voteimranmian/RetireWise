package com.retirewise.profile.domain

import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ProfileValidationTest {
    @Test
    fun ageWithinRangeIsValid() {
        assertIs<ValidationResult.Valid>(validateAge(18))
        assertIs<ValidationResult.Valid>(validateAge(45))
        assertIs<ValidationResult.Valid>(validateAge(100))
    }

    @Test
    fun ageBelowMinimumIsInvalid() {
        assertIs<ValidationResult.Invalid>(validateAge(17))
    }

    @Test
    fun ageAboveMaximumIsInvalid() {
        assertIs<ValidationResult.Invalid>(validateAge(101))
    }

    @Test
    fun retirementAgeAtOrAfterCurrentAgeIsValid() {
        assertIs<ValidationResult.Valid>(validateRetirementAge(retirementAge = 65, currentAge = 40))
        assertIs<ValidationResult.Valid>(validateRetirementAge(retirementAge = 40, currentAge = 40))
    }

    @Test
    fun retirementAgeBeforeCurrentAgeIsInvalid() {
        val result = validateRetirementAge(retirementAge = 39, currentAge = 40)

        assertIs<ValidationResult.Invalid>(result)
    }

    @Test
    fun retirementAgeAboveMaximumIsInvalid() {
        assertIs<ValidationResult.Invalid>(validateRetirementAge(retirementAge = 101, currentAge = 40))
    }

    @Test
    fun nonNegativeAmountIsValid() {
        assertIs<ValidationResult.Valid>(validateNonNegativeAmount(0.0))
        assertIs<ValidationResult.Valid>(validateNonNegativeAmount(1000.0))
    }

    @Test
    fun negativeAmountIsInvalid() {
        val result = validateNonNegativeAmount(-0.01)

        assertIs<ValidationResult.Invalid>(result)
        assertTrue((result as ValidationResult.Invalid).message.isNotBlank())
    }
}
