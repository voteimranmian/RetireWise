package com.retirewise.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class PlanScreenTest {
    @Test
    fun containsAllPlanCategoriesFromPrd() {
        assertEquals(
            listOf(
                "Goals",
                "Income",
                "Expenses",
                "Savings",
                "Investments",
                "Pensions",
                "Government benefits",
                "Property",
                "Debt",
                "Assumptions",
            ),
            planCategories(),
        )
    }
}
