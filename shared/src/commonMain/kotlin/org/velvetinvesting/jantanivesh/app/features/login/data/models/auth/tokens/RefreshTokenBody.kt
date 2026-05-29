package org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.tokens

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenBody(
    val token: String
)