package org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.sendotp

import kotlinx.serialization.Serializable

@Serializable
data class SendOtpDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)