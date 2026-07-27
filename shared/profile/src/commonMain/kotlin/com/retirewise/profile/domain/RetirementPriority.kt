package com.retirewise.profile.domain

/** What matters most to the user in retirement. */
enum class RetirementPriority(val displayLabel: String) {
    TRAVEL("Travel"),
    FAMILY("Family"),
    HEALTH("Health"),
    HOME("Staying in my home"),
    HOBBIES("Hobbies and leisure"),
    FINANCIAL_SECURITY("Financial security"),
    HELPING_OTHERS("Helping family or others financially"),
}
