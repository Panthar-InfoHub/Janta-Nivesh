package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfpurchasemandatestatus

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseBodyDto(
    val mandate_id: String
)