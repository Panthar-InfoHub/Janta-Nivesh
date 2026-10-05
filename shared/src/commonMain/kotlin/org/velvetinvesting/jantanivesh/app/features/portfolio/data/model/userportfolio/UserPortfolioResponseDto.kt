package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.userportfolio

import kotlinx.serialization.Serializable

/**
 * `GET /user/portfolio` response.
 *
 * Every field is nullable: the server sends explicit `null`s, which a non-null field with a
 * default still fails on. The mapper supplies the fallbacks.
 */
@Serializable
data class UserPortfolioResponseDto(
    val code: Int? = null,
    val message: String? = null,
    val `data`: UserPortfolioDataDto? = null
)

@Serializable
data class UserPortfolioDataDto(
    val total_investments: PortfolioTotalInvestmentsDto? = null,
    val invested_amount_breakdown: PortfolioInvestedBreakdownDto? = null,
    val mf_summary: PortfolioMfSummaryDto? = null,
    val mutual_funds: List<PortfolioMutualFundDto>? = null,
    val fixed_deposits: List<PortfolioFixedDepositDto>? = null
)

@Serializable
data class PortfolioTotalInvestmentsDto(
    val current_value: Double? = null,
    val total_returns: Double? = null,
    val return_percent: Double? = null,
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
    val value: Double? = null,
    val percent: Double? = null,
    val invested_amount: Double? = null,
    val total_returns: Double? = null,
    val return_percent: Double? = null,
    val one_day_return: Double? = null,
    val one_day_return_percent: Double? = null,
    val xirr: Double? = null
)

@Serializable
data class PortfolioFdAllocationDto(
    val value: Double? = null,
    val percent: Double? = null
)

@Serializable
data class PortfolioInvestedBreakdownDto(
    val invested_amount: Double? = null,
    val invested_items_count: Int? = null,
    val returns_amount: Double? = null,
    val returns_percent: Double? = null
)

/** The mutual-fund totals, reported by the server rather than summed from the holdings. */
@Serializable
data class PortfolioMfSummaryDto(
    val current_value: Double? = null,
    val invested_amount: Double? = null,
    val total_returns: Double? = null,
    val return_percent: Double? = null,
    val one_day_return: Double? = null,
    val one_day_return_percent: Double? = null,
    val xirr: Double? = null
)
