package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundles

import kotlinx.serialization.Serializable

@Serializable
data class BundlesDto(
    val success: Boolean,
    val message: String,
    val data: BundlesDataDto
)

@Serializable
data class BundlesDataDto(
    val bundles: List<BundleDto> = emptyList(),
    val pagination: BundlesPaginationDto? = null
)

@Serializable
data class BundleDto(
    val id: String,
    val bundle_name: String,
    val bundle_description: String? = null,
    val equity_percentage: Int = 0,
    val commodity_percentage: Int = 0,
    val debt_percentage: Int = 0,
    val hybrid_percentage: Int = 0,
    val img_url: String? = null,
    val meta_data: BundleMetaDataDto? = null,
    val categories: List<BundleCategoryDto> = emptyList()
)

@Serializable
data class BundleMetaDataDto(
    val risk_level: String? = null,
    val investment_time: String? = null,
    val investment_growth: String? = null
)

@Serializable
data class BundleCategoryDto(
    val id: String,
    val bundle_id: String,
    val category_name: String,
    val display_name: String,
    val total_percentage: Int = 0,
    val slots: List<BundleSlotDto> = emptyList()
)

@Serializable
data class BundleSlotDto(
    val id: String,
    val bundle_category_id: String,
    val allocation_percentage: Int = 0,
    val default_rank: Int = 0
)

@Serializable
data class BundlesPaginationDto(
    val total: Int = 0,
    val page: Int = 1,
    val limit: Int = 0,
    val totalPages: Int = 0
)
