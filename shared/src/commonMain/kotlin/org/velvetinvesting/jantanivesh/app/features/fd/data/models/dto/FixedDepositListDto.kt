package org.velvetinvesting.jantanivesh.app.features.fd.data.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class FixedDepositListDto(
    val `data`: FdListData,
    val message: String,
    val success: Boolean
)

@Serializable
data class FdListData(
    val fd_products: List<FdProductDto>,
    val pagination: PaginationDto
)

@Serializable
data class FdProductDto(
    val id: String = "",
    val issuer: IssuerDto = IssuerDto(),
    val interest_rates: List<InterestRateDto> = emptyList(),
    val min_deposit: String = "0",
    val tags: List<TagDto> = emptyList()
)

@Serializable
data class IssuerDto(
    val id: String = "",
    val full_name: String = "",
    val display_name: String = "",
    val logo_url: String = "",
    val rating_text: String? = null,
    val about_description: String = ""
)

@Serializable
data class InterestRateDto(
    val id: String = "",
    val tenure_label: String = "",
    val tenure_days: Int = 0,
    val interest_rate: String = "0",
    val annualized_yield: String = "",
    val is_default_selection: Boolean = false,
    val payout_frequency: String = "",
    val customer_type: String = ""
)

@Serializable
data class TagDto(
    val text: String,
    val color: String? = null
)

@Serializable
data class PaginationDto(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int
)
