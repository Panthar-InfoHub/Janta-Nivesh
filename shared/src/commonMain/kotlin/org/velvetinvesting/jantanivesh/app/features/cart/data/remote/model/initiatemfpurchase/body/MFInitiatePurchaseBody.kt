package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.initiatemfpurchase.body

import kotlinx.serialization.Serializable

@Serializable
data class MFInitiatePurchaseBody(
    val items: List<Item>
)