package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.payment

import kotlinx.serialization.Serializable

/** `GET /payment/{id}`; only what the cart's payment poll reads. */
@Serializable
data class PaymentStatusDto(
    val success: Boolean,
    val message: String,
    val `data`: PaymentStatusData
)

@Serializable
data class PaymentStatusData(
    val id: Long? = null,
    val status: String,
    val amount: Double? = null,
    val failed_reason: String? = null
)
