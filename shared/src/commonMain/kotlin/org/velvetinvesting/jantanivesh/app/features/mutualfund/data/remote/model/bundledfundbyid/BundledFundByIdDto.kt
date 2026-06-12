package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundledfundbyid

import kotlinx.serialization.Serializable

@Serializable
data class BundledFundByIdDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)