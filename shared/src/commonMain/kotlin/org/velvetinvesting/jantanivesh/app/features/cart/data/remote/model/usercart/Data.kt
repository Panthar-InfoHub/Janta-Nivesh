package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.usercart

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val sip: List<CartItem> = emptyList(),
    val lumpsum: List<CartItem> = emptyList()
)
