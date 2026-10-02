package org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model

import kotlinx.serialization.Serializable

/** `GET /bundles` — every bundle with its allocation and category slots, without funds. */
@Serializable
data class AllBundlesDto(
    val success: Boolean,
    val message: String,
    val data: AllBundlesDataDto
)

@Serializable
data class AllBundlesDataDto(
    val bundles: List<BundleSummaryDto> = emptyList(),
    val pagination: BundlesPaginationDto? = null
)

@Serializable
data class BundleSummaryDto(
    val id: String,
    val bundle_name: String,
    val bundle_description: String? = null,
    val equity_percentage: Double = 0.0,
    val commodity_percentage: Double = 0.0,
    val debt_percentage: Double = 0.0,
    val hybrid_percentage: Double = 0.0,
    val img_url: String? = null,
    // NEW: meta_data now also carries daily_start_amount and monthly_start_amount (see BundleMetaDataDto).
    val meta_data: BundleMetaDataDto? = null,
    val categories: List<BundleSummaryCategoryDto> = emptyList()
)

@Serializable
data class BundleSummaryCategoryDto(
    val id: String,
    val bundle_id: String = "",
    val category_name: String,
    val display_name: String,
    val total_percentage: Double = 0.0,
    val slots: List<BundleSlotDto> = emptyList()
)

@Serializable
data class BundlesPaginationDto(
    val total: Int = 0,
    val page: Int = 1,
    val limit: Int = 0,
    val totalPages: Int = 0
)
