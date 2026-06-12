package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundledfunds

import kotlinx.serialization.Serializable

@Serializable
data class BundledFundsDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)