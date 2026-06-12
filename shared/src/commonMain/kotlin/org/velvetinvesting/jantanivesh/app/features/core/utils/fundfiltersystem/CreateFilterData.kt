package org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem

fun createInitialInvestmentFilter(): InvestmentFilter {

    return InvestmentFilter(
        groups = listOf(
            // Risk
            FilterGroup(
                id = "risk",
                title = "Risk",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption(
                        "1",
                        "Low"
                    ),
                    FilterOption(
                        "2",
                        "Low to Moderate"
                    ),
                    FilterOption(
                        "3",
                        "Moderate"
                    ),
                    FilterOption(
                        "4",
                        "Moderately High"
                    ),
                    FilterOption(
                        "5",
                        "High"
                    ),
                    FilterOption(
                        "6",
                        "Very High"
                    )
                )
            ),
            // Category
            FilterGroup(
                id = "category",
                title = "Category",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption(
                        "1",
                        "Equity"
                    ),
                    FilterOption(
                        "2",
                        "Debt"
                    ),
                    FilterOption(
                        "3",
                        "Hybrid"
                    ),
                    FilterOption(
                        "4",
                        "Precious Metal"
                    ),
                    FilterOption(
                        "5",
                        "Others - Commodities"
                    ),
                    FilterOption(
                        "6",
                        "Currency"
                    ),
                    FilterOption(
                        "7",
                        "Liquid"
                    ),
                    FilterOption(
                        "8",
                        "Others - Mutual Funds"
                    ),
                    FilterOption(
                        "9",
                        "Solution Oriented"
                    )
                )
            ),
            // Fund Category
            FilterGroup(
                id = "fund_category",
                title = "Fund Category",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption(
                        "flexi_cap",
                        "Flexi Cap"
                    ),
                    FilterOption(
                        "large_Mid_cap",
                        "Large & Mid Cap"
                    ),
                    FilterOption(
                        "large_cap",
                        "Large Cap"
                    ),
                    FilterOption(
                        "mid_cap",
                        "Mid Cap"
                    ),
                    FilterOption(
                        "small_cap",
                        "Small Cap"
                    ),
                    FilterOption(
                        "index",
                        "Index"
                    ),
                    FilterOption(
                        "global_others",
                        "Global / Others"
                    )
                )
            )
        )
    )
}

fun createInitialFDFilters(): InvestmentFilter {

    return InvestmentFilter(
        groups = listOf(

            FilterGroup(
                id = FDFilterIds.TENURE,
                title = "Tenure",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption(
                        FDFilterIds.TENURE_1Y,
                        "1 Year"
                    ),
                    FilterOption(
                        FDFilterIds.TENURE_2Y,
                        "2 Years"
                    ),
                    FilterOption(
                        FDFilterIds.TENURE_3Y,
                        "3 Years"
                    ),
                    FilterOption(
                        FDFilterIds.TENURE_5Y,
                        "5 Years"
                    )
                )
            ),

            FilterGroup(
                id = FDFilterIds.PAYOUT_FREQUENCY,
                title = "Payout Frequency",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption(
                        FDFilterIds.PAYOUT_CUMULATIVE,
                        "Cumulative"
                    ),
                    FilterOption(
                        FDFilterIds.PAYOUT_MONTHLY,
                        "Monthly"
                    ),
                    FilterOption(
                        FDFilterIds.PAYOUT_QUARTERLY,
                        "Quarterly"
                    ),
                    FilterOption(
                        FDFilterIds.PAYOUT_HALF_YEARLY,
                        "Half-Yearly"
                    ),
                    FilterOption(
                        FDFilterIds.PAYOUT_YEARLY,
                        "Yearly"
                    ),
                    FilterOption(
                        FDFilterIds.PAYOUT_ON_MATURITY,
                        "On Maturity"
                    )
                )
            )
        )
    )
}


object FDFilterIds {

    // Groups
    const val TENURE = "tenure"
    const val PAYOUT_FREQUENCY = "payout_frequency"

    // Tenure Options
    const val TENURE_1Y = "1y"
    const val TENURE_2Y = "2y"
    const val TENURE_3Y = "3y"
    const val TENURE_5Y = "5y"

    // Payout Frequency Options
    const val PAYOUT_CUMULATIVE = "CUMULATIVE"
    const val PAYOUT_MONTHLY = "MONTHLY"
    const val PAYOUT_QUARTERLY = "QUARTERLY"
    const val PAYOUT_HALF_YEARLY = "HALF_YEARLY"
    const val PAYOUT_YEARLY = "YEARLY"
    const val PAYOUT_ON_MATURITY = "ON_MATURITY"

    const val CUSTOM = "Custom"
}