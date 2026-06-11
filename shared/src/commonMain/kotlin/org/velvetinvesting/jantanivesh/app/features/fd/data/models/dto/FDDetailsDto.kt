package org.velvetinvesting.jantanivesh.app.features.fd.data.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class FDDetailsDto(
    val `data`: FDDetailsDataDto,
    val message: String,
    val success: Boolean
)

@Serializable
data class FDDetailsDataDto(
    val faqs: List<FaqDto> = emptyList(),
    val id: String = "",
    val interest_rates: List<InterestRateDto> = emptyList(),
    val is_vkyc_required: Boolean = false,
    val issuer: IssuerDto = IssuerDto(),
    val issuer_id: String = "",
    val lock_in_period_days: Int = 0,
    val max_deposit: String = "0",
    val max_tenure_days: Int = 0,
    val min_amount_for_vkyc: String = "0",
    val min_deposit: String = "0",
    val min_tenure_days: Int = 0,
    val premature_penalty_percent: Int = 0,
    val tags: List<TagDto> = emptyList(),
    val type: String = "",
    val usps: List<UspDto> = emptyList(),
    val withdrawal_message: String = ""
)

@Serializable
data class FaqDto(
    val title: String = "",
    val description: String = ""
)

@Serializable
data class UspDto(
    val title: String = "",
    val description: String = "",
    val icon_url: String? = null
)
