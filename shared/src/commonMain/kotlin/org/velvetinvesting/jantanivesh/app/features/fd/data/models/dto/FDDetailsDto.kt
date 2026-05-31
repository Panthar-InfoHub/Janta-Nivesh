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
    val faqs: List<FaqDto>,
    val id: String,
    val interest_rates: List<InterestRateDto>,
    val is_vkyc_required: Boolean,
    val issuer: IssuerDto,
    val issuer_id: String,
    val lock_in_period_days: Int,
    val max_deposit: String,
    val max_tenure_days: Int,
    val min_amount_for_vkyc: String,
    val min_deposit: String,
    val min_tenure_days: Int,
    val premature_penalty_percent: Int,
    val tags: List<TagDto>,
    val type: String,
    val usps: List<UspDto>,
    val withdrawal_message: String
)

@Serializable
data class FaqDto(
    val title: String,
    val description: String
)

@Serializable
data class UspDto(
    val title: String,
    val description: String,
    val icon_url: String? = null
)
