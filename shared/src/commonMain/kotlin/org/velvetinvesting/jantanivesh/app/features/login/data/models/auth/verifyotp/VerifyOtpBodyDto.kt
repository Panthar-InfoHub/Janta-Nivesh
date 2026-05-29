package org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.verifyotp

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpBodyDto(
    val mob: String,
    val otp: String
)