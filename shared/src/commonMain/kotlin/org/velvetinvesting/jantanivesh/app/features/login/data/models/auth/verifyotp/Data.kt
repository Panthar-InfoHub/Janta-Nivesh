package org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.verifyotp

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val refresh_token: String,
    val token: String,
    val user: User
)