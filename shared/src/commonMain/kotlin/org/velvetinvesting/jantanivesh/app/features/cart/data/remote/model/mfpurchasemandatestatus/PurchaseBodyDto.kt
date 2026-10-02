package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.mfpurchasemandatestatus

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseBodyDto(
    val mandate_id: String
)