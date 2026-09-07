package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.redemption

import kotlinx.serialization.Serializable

/**
 * `POST /mf/redemption/` takes either an amount or a number of units, never both — hence two
 * bodies rather than one with two nullable fields, so an impossible request cannot be built.
 */
@Serializable
data class CreateRedemptionByAmountBody(
    val mf_holding_id: String,
    val amount: Double
)

@Serializable
data class CreateRedemptionByUnitsBody(
    val mf_holding_id: String,
    val units: Double
)

/** `POST /mf/redemption/{id}/confirm/verify-otp`. */
@Serializable
data class VerifyRedemptionOtpBody(
    val otp: String
)
