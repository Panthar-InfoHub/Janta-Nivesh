package org.velvetinvesting.jantanivesh.app.features.fd.domain.utils

import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.*

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

fun createInitialFDFilters(): InvestmentFilter {

    return InvestmentFilter(
        groups = listOf(

            FilterGroup(
                id = FDFilterIds.TENURE,
                title = "Tenure",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption(FDFilterIds.TENURE_1Y, "1 Year"),
                    FilterOption(FDFilterIds.TENURE_2Y, "2 Years"),
                    FilterOption(FDFilterIds.TENURE_3Y, "3 Years"),
                    FilterOption(FDFilterIds.TENURE_5Y, "5 Years")
                )
            ),

            FilterGroup(
                id = FDFilterIds.PAYOUT_FREQUENCY,
                title = "Payout Frequency",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption(FDFilterIds.PAYOUT_CUMULATIVE, "Cumulative"),
                    FilterOption(FDFilterIds.PAYOUT_MONTHLY, "Monthly"),
                    FilterOption(FDFilterIds.PAYOUT_QUARTERLY, "Quarterly"),
                    FilterOption(FDFilterIds.PAYOUT_HALF_YEARLY, "Half-Yearly"),
                    FilterOption(FDFilterIds.PAYOUT_YEARLY, "Yearly"),
                    FilterOption(FDFilterIds.PAYOUT_ON_MATURITY, "On Maturity")
                )
            )
        )
    )
}

fun InvestmentFilter.getActiveFilterLabel(): String {

    val tenure = groups
        .find { it.id == FDFilterIds.TENURE }
        ?.options
        ?.firstOrNull { it.isSelected }

    val payout = groups
        .find { it.id == FDFilterIds.PAYOUT_FREQUENCY }
        ?.options
        ?.firstOrNull { it.isSelected }

    val parts = mutableListOf<String>()

    tenure?.let {
        parts.add(it.id.uppercase()) // "3y" → "3Y"
    }

    payout?.let {
        parts.add(it.title) // already user-friendly
    }

    return if (parts.isEmpty()) {
        "All FDs"
    } else {
        parts.joinToString(" • ")
    }
}
