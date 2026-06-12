package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfpurchasemandatestatus

import kotlinx.serialization.Serializable

@Serializable
data class CheckMFPurchaseMandateStatusDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)