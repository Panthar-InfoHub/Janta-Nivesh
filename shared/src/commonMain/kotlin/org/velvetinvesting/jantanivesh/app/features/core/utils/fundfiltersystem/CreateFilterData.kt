package org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem

/**
 * The mutual-fund filter tray, mirroring the query parameters of `GET /mf/funds`.
 *
 * Group ids are the parameter names and option ids are the values the endpoint accepts, so the
 * view model can hand a selection straight to the request without a translation table.
 */
fun createInitialInvestmentFilter(): InvestmentFilter {

    return InvestmentFilter(
        groups = listOf(
            // tag — the fund sub-category section
            FilterGroup(
                id = MfFilterIds.TAG,
                title = "Fund Type",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption(MfFilterIds.TAG_POPULAR, "Popular"),
                    FilterOption(MfFilterIds.TAG_LARGE_CAP, "Large Cap"),
                    FilterOption(MfFilterIds.TAG_MID_CAP, "Mid Cap"),
                    FilterOption(MfFilterIds.TAG_SMALL_CAP, "Small Cap"),
                    FilterOption(MfFilterIds.TAG_FLEXI_CAP, "Flexi Cap"),
                    FilterOption(MfFilterIds.TAG_MULTI_CAP, "Multi Cap"),
                    FilterOption(MfFilterIds.TAG_DEBT, "Debt"),
                    FilterOption(MfFilterIds.TAG_OTHERS, "Others")
                )
            ),
            // category — asset class
            FilterGroup(
                id = MfFilterIds.CATEGORY,
                title = "Category",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption(MfFilterIds.CATEGORY_ALL, "All"),
                    FilterOption(MfFilterIds.CATEGORY_EQUITY, "Equity"),
                    FilterOption(MfFilterIds.CATEGORY_DEBT, "Debt"),
                    FilterOption(MfFilterIds.CATEGORY_LIQUID, "Liquid")
                )
            ),
            // amount_type — minimum SIP installment
            FilterGroup(
                id = MfFilterIds.AMOUNT_TYPE,
                title = "Min Investment",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption(MfFilterIds.AMOUNT_DAILY_10, "Daily \u20B910"),
                    FilterOption(MfFilterIds.AMOUNT_MONTHLY_100, "Monthly \u20B9100")
                )
            )
        )
    )
}

/** Query-parameter names and values of `GET /mf/funds`, shared by the tray and the chips. */
object MfFilterIds {

    // Groups
    const val TAG = "tag"
    const val CATEGORY = "category"
    const val AMOUNT_TYPE = "amount_type"

    // tag
    const val TAG_POPULAR = "popular"
    const val TAG_LARGE_CAP = "large_cap"
    const val TAG_MID_CAP = "mid_cap"
    const val TAG_SMALL_CAP = "small_cap"
    const val TAG_FLEXI_CAP = "flexi_cap"
    const val TAG_MULTI_CAP = "multi_cap"
    const val TAG_OTHERS = "others"
    const val TAG_DEBT = "debt"

    // category
    const val CATEGORY_ALL = "all"
    const val CATEGORY_EQUITY = "equity"
    const val CATEGORY_DEBT = "debt"
    const val CATEGORY_LIQUID = "liquid"

    // amount_type
    const val AMOUNT_DAILY_10 = "daily_10"
    const val AMOUNT_MONTHLY_100 = "monthly_100"
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