package org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

/** `GET /bundles/{id}` — a bundle's categories, their slots, and the funds each slot can hold. */
@Serializable
data class BundleDetailsDto(
    val success: Boolean,
    val message: String,
    val data: BundleDetailsDataDto
)

@Serializable
data class BundleDetailsDataDto(
    val bundle_name: String,
    val bundle_description: String? = null,
    val equity_percentage: Double = 0.0,
    val commodity_percentage: Double = 0.0,
    val debt_percentage: Double = 0.0,
    val hybrid_percentage: Double = 0.0,
    val meta_data: BundleMetaDataDto? = null,
    val categories: List<BundleCategoryDto> = emptyList()
)

/**
 * Shared by `GET /bundles` and `GET /bundles/{id}`. The three start amounts are the bundle's
 * starting amount with its pre-selected funds, one for each way of investing.
 */
@Serializable
data class BundleMetaDataDto(
    val risk_level: String? = null,
    /** The one-time (lumpsum) start amount. */
    val start_amount: Double? = null,
    // NEW: start amount for a daily SIP.
    val daily_start_amount: Double? = null,
    // NEW: start amount for a monthly SIP.
    val monthly_start_amount: Double? = null,
    val investment_time: String? = null,
    val investment_growth: String? = null
)

@Serializable
data class BundleCategoryDto(
    val id: String,
    val bundle_id: String = "",
    val category_name: String,
    val display_name: String,
    val total_percentage: Double = 0.0,
    val slots: List<BundleSlotDto> = emptyList(),
    val funds: List<BundleFundDto> = emptyList()
)

@Serializable
data class BundleSlotDto(
    val id: String,
    val bundle_category_id: String = "",
    val allocation_percentage: Double = 0.0,
    val default_rank: Int = 0,
    /** The fund the bundle recommends for this slot. */
    val pre_selected_product_id: String? = null,
    val pre_selected_fund: BundlePreselectedFundDto? = null
)

/** A slot's recommended fund — its returns and its three minimums, in a shape of its own. */
@Serializable
data class BundlePreselectedFundDto(
    val id: String,
    val name: String? = null,
    val isin: String? = null,
    val img_url: String? = null,
    /** May arrive as a number or a string; read either way. */
    val latest_nav: JsonPrimitive? = null,
    val latest_nav_date: String? = null,
    val returns: BundleFundMetricsDto? = null,
    val min_investment: BundleMinInvestmentDto? = null
)

/** A pre-selected fund's own minimum for each way of investing, sent as numbers. */
@Serializable
data class BundleMinInvestmentDto(
    val lumpsum_min: Double? = null,
    val sip_monthly_min: Double? = null,
    // NEW: minimum for a daily SIP.
    val sip_daily_min: Double? = null
)

@Serializable
data class BundleFundDto(
    val id: String,
    val name: String? = null,
    val isin: String? = null,
    val img_url: String? = null,
    /** Sent as a string here ("451.8827"), a number on a pre-selected fund; read either way. */
    val latest_nav: JsonPrimitive? = null,
    val latest_nav_date: String? = null,
    val metrics: BundleFundMetricsDto? = null,
    // NEW: the fund's minimums for all three ways of investing, so picking it in place of a
    // pre-selected fund can update the bundle minimum.
    val scheme_plan: BundleFundSchemePlanDto? = null
)

/** A category fund's own minimum for each way of investing. The amounts arrive as strings. */
@Serializable
data class BundleFundSchemePlanDto(
    val lumpsum_amount_min: String? = null,
    val sip_monthly_amount_min: String? = null,
    val sip_daily_amount_min: String? = null
)

@Serializable
data class BundleFundMetricsDto(
    val return_30d: Double? = null,
    val return_90d: Double? = null,
    val return_6m: Double? = null,
    val return_1y: Double? = null,
    val return_3y: Double? = null,
    val return_5y: Double? = null,
    val nav_change_pct: Double? = null
)
