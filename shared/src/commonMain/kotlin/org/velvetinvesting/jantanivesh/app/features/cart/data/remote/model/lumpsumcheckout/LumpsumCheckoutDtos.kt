package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.lumpsumcheckout

import kotlinx.serialization.Serializable

/** `POST /mf/cart/checkout/lumpsum` — batches the cart's lumpsum orders and sends the OTP. */
@Serializable
data class LumpsumCheckoutResponseDto(
    val success: Boolean,
    val message: String,
    val `data`: LumpsumCheckoutData
)

@Serializable
data class LumpsumCheckoutData(
    val batch_id: String,
    val orders_count: Int = 0,
    val total_amount: Double = 0.0
)

/** `POST /mf/cart/checkout/lumpsum/confirm` body. */
@Serializable
data class ConfirmLumpsumCheckoutRequest(
    val batch_id: String,
    val otp: String,
    /** Where the payment page lands when it is done, which is also the webview's exit URL. */
    val payment_postback_url: String
)

@Serializable
data class ConfirmLumpsumCheckoutResponseDto(
    val success: Boolean,
    val message: String,
    val `data`: ConfirmLumpsumCheckoutData
)

@Serializable
data class ConfirmLumpsumCheckoutData(
    val payment_url: String,
    val payment_id: String,
    val payment_status: String? = null,
    val orders_count: Int = 0
)
