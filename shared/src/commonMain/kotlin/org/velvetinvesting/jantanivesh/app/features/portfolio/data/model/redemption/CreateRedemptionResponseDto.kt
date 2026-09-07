package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.redemption

import kotlinx.serialization.Serializable

/**
 * `POST /mf/redemption/`. The `fp_id` in here is the handle for every later step — the status
 * poll, the OTP request and the OTP verification are all keyed on it.
 */
@Serializable
data class CreateRedemptionResponseDto(
    val success: Boolean = false,
    val message: String = "",
    val `data`: CreateRedemptionDataDto? = null
)

@Serializable
data class CreateRedemptionDataDto(
    val fp_id: String? = null,
    val user_id: String? = null,
    val plan_type: String? = null,
    val systematic: Boolean? = null,
    val mf_investment_account: String? = null,
    val scheme: String? = null,
    val folio_number: String? = null,
    /** A decimal string, or null when the redemption was placed by units. */
    val amount: String? = null,
    val units: String? = null,
    val state: String? = null,
    val fp_created_at: String? = null
)
