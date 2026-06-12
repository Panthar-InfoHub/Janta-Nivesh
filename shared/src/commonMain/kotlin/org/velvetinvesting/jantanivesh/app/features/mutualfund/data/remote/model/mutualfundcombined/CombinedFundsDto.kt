package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mutualfundcombined

import kotlinx.serialization.Serializable

@Serializable
data class CombinedFundsDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)