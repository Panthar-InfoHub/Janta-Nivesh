package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.initiatemfpurchase

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val mandate_id: String,
    val mandate_short_url: String,
    val status: String
)