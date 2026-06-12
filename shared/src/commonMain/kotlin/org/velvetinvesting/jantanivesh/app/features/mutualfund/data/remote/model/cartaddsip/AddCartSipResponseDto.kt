package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.cartaddsip

import kotlinx.serialization.Serializable

@Serializable
data class AddCartSipResponseDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)