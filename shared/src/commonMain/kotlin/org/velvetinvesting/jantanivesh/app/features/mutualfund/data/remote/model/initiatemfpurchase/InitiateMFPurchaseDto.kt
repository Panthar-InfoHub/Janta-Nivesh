package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.initiatemfpurchase

import kotlinx.serialization.Serializable

@Serializable
data class InitiateMFPurchaseDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)