package org.velvetinvesting.jantanivesh.app.features.tradingaccount.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class PANVerifyDto(
    val `data`: PANVerifyDataDto,
    val message: String,
    val success: Boolean
)

@Serializable
data class PANVerifyDataDto(
    val app_verified: Boolean,
    val full_name: String,
    val pan_verified: Boolean
)
