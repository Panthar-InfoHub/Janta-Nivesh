package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.usercart

import kotlinx.serialization.Serializable

@Serializable
data class UserCartDto(
    val success: Boolean,
    val message: String,
    val `data`: Data
)
