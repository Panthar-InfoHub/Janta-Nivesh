package org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata

import kotlinx.serialization.Serializable

@Serializable
data class UserDataDto(
    val code: Int,
    val `data`: Data,
    val message: String
)