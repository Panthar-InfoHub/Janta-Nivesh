package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.sipcheckout

import kotlinx.serialization.Serializable

/** `POST /mf/cart/checkout/sip` body that places the cart's SIPs against an approved mandate. */
@Serializable
data class SipCheckoutRequest(
    /** The server's mandate record id, as the single-fund SIP purchase sends it. */
    val mandate_id: String
)

@Serializable
data class SipCheckoutResponseDto(
    val success: Boolean,
    val message: String,
    val `data`: SipCheckoutData
)

@Serializable
data class SipCheckoutData(
    val batch_id: String,
    val plans_count: Int = 0,
    val total_amount: Double = 0.0
)

/** `POST /mf/cart/checkout/sip` body that confirms a checkout batch with the OTP it sent. */
@Serializable
data class VerifySipCheckoutOtpRequest(
    val batch_id: String,
    val otp: String
)
