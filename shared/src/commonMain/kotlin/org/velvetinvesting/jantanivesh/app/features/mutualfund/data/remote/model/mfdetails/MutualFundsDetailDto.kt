package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfdetails

import kotlinx.serialization.Serializable

@Serializable
data class MutualFundsDetailDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)