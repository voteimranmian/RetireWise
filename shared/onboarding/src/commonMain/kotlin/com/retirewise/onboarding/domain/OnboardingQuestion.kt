package com.retirewise.onboarding.domain

/**
 * The 12-question initial assessment (docs/PRD.md section 9.1). Each
 * question carries its own prompt and an explanation shown behind an
 * "Explain why you need this" affordance.
 */
sealed interface OnboardingQuestion {
    val prompt: String
    val explanation: String

    data object Age : OnboardingQuestion {
        override val prompt = "What is your age?"
        override val explanation =
            "Your age helps us figure out how many years you have until retirement and " +
                "which government benefits may apply to you."
    }

    data object Province : OnboardingQuestion {
        override val prompt = "Which province or territory do you live in?"
        override val explanation =
            "Government retirement benefits and tax rules vary by province or territory."
    }

    data object TargetRetirementAge : OnboardingQuestion {
        override val prompt = "At what age would you like to retire?"
        override val explanation =
            "Your target retirement age is the starting point for your retirement plan."
    }

    data object AnnualIncome : OnboardingQuestion {
        override val prompt = "What is your approximate annual income?"
        override val explanation =
            "Your income helps us estimate how much you may be able to save and what " +
                "benefits you may qualify for."
    }

    data object RetirementSavings : OnboardingQuestion {
        override val prompt = "How much do you currently have saved for retirement?"
        override val explanation =
            "Your current savings is the starting balance for your retirement projection."
    }

    data object WorkplacePension : OnboardingQuestion {
        override val prompt = "Do you have a workplace pension?"
        override val explanation =
            "A workplace pension can significantly change how much you need to save on your own."
    }

    data object MonthlyContribution : OnboardingQuestion {
        override val prompt = "How much do you contribute toward retirement each month?"
        override val explanation =
            "Your monthly contribution helps us project how your savings may grow over time."
    }

    data object HomeOwnership : OnboardingQuestion {
        override val prompt = "Do you own your home?"
        override val explanation =
            "Home ownership can affect your expenses and options in retirement."
    }

    data object ExpectedDebt : OnboardingQuestion {
        override val prompt = "About how much debt do you expect to have when you retire?"
        override val explanation =
            "Debt at retirement affects how much income you will need to cover your expenses."
    }

    data object TargetMonthlySpending : OnboardingQuestion {
        override val prompt = "How much do you expect to spend per month in retirement?"
        override val explanation =
            "Your target spending is the income goal your retirement plan works toward."
    }

    data object PlanningMode : OnboardingQuestion {
        override val prompt = "Are you planning alone or with a spouse or partner?"
        override val explanation =
            "Planning with a partner may change the benefits and strategies available to you."
    }

    data object RetirementPriorities : OnboardingQuestion {
        override val prompt = "What matters most to you in retirement?"
        override val explanation =
            "Knowing your priorities helps us personalize the guidance we give you."
    }

    companion object {
        val entries: List<OnboardingQuestion> =
            listOf(
                Age,
                Province,
                TargetRetirementAge,
                AnnualIncome,
                RetirementSavings,
                WorkplacePension,
                MonthlyContribution,
                HomeOwnership,
                ExpectedDebt,
                TargetMonthlySpending,
                PlanningMode,
                RetirementPriorities,
            )
    }
}
