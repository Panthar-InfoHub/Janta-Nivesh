package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.cartpurchase

import kotlinx.serialization.Serializable

@Serializable
data class CartPurchaseSIPDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)