package com.retirewise.profile.domain

/** Result of validating a single onboarding answer. */
sealed interface ValidationResult {
    data object Valid : ValidationResult

    data class Invalid(val message: String) : ValidationResult
}

private const val MIN_AGE = 18
private const val MAX_AGE = 100

fun validateAge(age: Int): ValidationResult =
    if (age in MIN_AGE..MAX_AGE) {
        ValidationResult.Valid
    } else {
        ValidationResult.Invalid("Age must be between $MIN_AGE and $MAX_AGE.")
    }

fun validateRetirementAge(
    retirementAge: Int,
    currentAge: Int,
): ValidationResult =
    when {
        retirementAge < currentAge ->
            ValidationResult.Invalid("Target retirement age must be at or after your current age.")
        retirementAge > MAX_AGE ->
            ValidationResult.Invalid("Target retirement age must be $MAX_AGE or less.")
        else -> ValidationResult.Valid
    }

fun validateNonNegativeAmount(amount: Double): ValidationResult =
    if (amount >= 0.0) {
        ValidationResult.Valid
    } else {
        ValidationResult.Invalid("Amount cannot be negative.")
    }
