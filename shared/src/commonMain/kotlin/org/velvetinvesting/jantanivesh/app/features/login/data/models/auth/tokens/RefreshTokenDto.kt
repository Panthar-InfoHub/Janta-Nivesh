package org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.tokens

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)