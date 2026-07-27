package com.retirewise.profile.domain

/**
 * Canadian provinces and territories, used to select province-specific
 * government benefit rules in later phases (docs/FINANCIAL_RULES.md).
 */
enum class CanadianProvince(val displayLabel: String) {
    ALBERTA("Alberta"),
    BRITISH_COLUMBIA("British Columbia"),
    MANITOBA("Manitoba"),
    NEW_BRUNSWICK("New Brunswick"),
    NEWFOUNDLAND_AND_LABRADOR("Newfoundland and Labrador"),
    NORTHWEST_TERRITORIES("Northwest Territories"),
    NOVA_SCOTIA("Nova Scotia"),
    NUNAVUT("Nunavut"),
    ONTARIO("Ontario"),
    PRINCE_EDWARD_ISLAND("Prince Edward Island"),
    QUEBEC("Quebec"),
    SASKATCHEWAN("Saskatchewan"),
    YUKON("Yukon"),
}
