package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.response

import kotlinx.serialization.Serializable

@Serializable
data class FundRedeemDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)