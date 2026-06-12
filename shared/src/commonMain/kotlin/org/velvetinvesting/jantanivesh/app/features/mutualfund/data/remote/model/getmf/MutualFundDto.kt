package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.getmf

import kotlinx.serialization.Serializable

@Serializable
data class MutualFundDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)