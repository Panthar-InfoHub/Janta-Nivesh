package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.userportfolio

import kotlinx.serialization.Serializable

/**
 * `GET /user/portfolio` — **the response the app reads today**.
 *
 * This replaces the older shape in `data.model.portfolio`, which is kept only until the screens
 * still referencing it are cleaned up. Every field is optional with a default so a partial or
 * still-changing payload renders rather than failing to parse.
 */
@Serializable
data class UserPortfolioResponseDto(
    val code: Int = 0,
    val message: String = "",
    val `data`: UserPortfolioDataDto? = null
)

@Serializable
data class UserPortfolioDataDto(
    val total_investments: PortfolioTotalInvestmentsDto? = null,
    val invested_amount_breakdown: PortfolioInvestedBreakdownDto? = null,
    val mf_summary: PortfolioMfSummaryDto? = null,
    val mutual_funds: List<PortfolioMutualFundDto> = emptyList(),
    val fixed_deposits: List<PortfolioFixedDepositDto> = emptyList()
)

@Serializable
data class PortfolioTotalInvestmentsDto(
    val current_value: Double = 0.0,
    val total_returns: Double = 0.0,
    val return_percent: Double = 0.0,
    val allocation: PortfolioAllocationDto? = null
)

/**
 * The two allocation slices are not the same shape: the mutual-fund side carries its own
 * returns and XIRR, the fixed-deposit side is value and percent only.
 */
@Serializable
data class PortfolioAllocationDto(
    val mutual_funds: PortfolioMfAllocationDto? = null,
    val fixed_deposits: PortfolioFdAllocationDto? = null
)

@Serializable
data class PortfolioMfAllocationDto(
    val value: Double = 0.0,
    val percent: Double = 0.0,
    val invested_amount: Double = 0.0,
    val total_returns: Double = 0.0,
    val return_percent: Double = 0.0,
    val one_day_return: Double = 0.0,
    val one_day_return_percent: Double = 0.0,
    val xirr: Double? = 0.0
)

@Serializable
data class PortfolioFdAllocationDto(
    val value: Double = 0.0,
    val percent: Double = 0.0
)

@Serializable
data class PortfolioInvestedBreakdownDto(
    val invested_amount: Double = 0.0,
    val invested_items_count: Int = 0,
    val returns_amount: Double = 0.0,
    val returns_percent: Double = 0.0
)

/** The mutual-fund totals, reported by the server rather than summed from the holdings. */
@Serializable
data class PortfolioMfSummaryDto(
    val current_value: Double = 0.0,
    val invested_amount: Double = 0.0,
    val total_returns: Double = 0.0,
    val return_percent: Double = 0.0,
    val one_day_return: Double = 0.0,
    val one_day_return_percent: Double = 0.0,
    val xirr: Double? = 0.0
)
