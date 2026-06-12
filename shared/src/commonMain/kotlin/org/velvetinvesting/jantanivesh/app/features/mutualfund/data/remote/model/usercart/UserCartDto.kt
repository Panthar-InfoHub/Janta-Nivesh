package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.usercart

import kotlinx.serialization.Serializable

@Serializable
data class UserCartDto(
    val code: Int,
    val `data`: Data,
    val message: String
)