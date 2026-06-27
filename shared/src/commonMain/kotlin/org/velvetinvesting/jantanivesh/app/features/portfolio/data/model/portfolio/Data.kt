package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val total_investments: TotalInvestments,
    val invested_amount_breakdown: InvestedAmountBreakdown,
    val mutual_funds: List<MutualFundPortfolioDto>,
    val fixed_deposits: List<FdTransaction>
)

@Serializable
data class TotalInvestments(
    val current_value: Double,
    val total_returns: Double,
    val return_percent: Double,
    val allocation: Allocation
)

@Serializable
data class Allocation(
    val mutual_funds: AllocationItem,
    val fixed_deposits: AllocationItem
)

@Serializable
data class AllocationItem(
    val value: Double,
    val percent: Double
)

@Serializable
data class InvestedAmountBreakdown(
    val invested_amount: Double,
    val invested_items_count: Int,
    val returns_amount: Double,
    val returns_percent: Double
)