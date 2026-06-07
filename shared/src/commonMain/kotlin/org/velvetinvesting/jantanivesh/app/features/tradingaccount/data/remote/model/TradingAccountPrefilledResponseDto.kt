package org.velvetinvesting.jantanivesh.app.features.tradingaccount.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TradingAccountPrefilledResponseDto(
    val success: Boolean,
    val message: String,
    val data: PrefilledDataDto
)

@Serializable
data class PrefilledDataDto(
    val full_name: String,
    val email: String,
    val phone_no: String,
    val dob: String,
    val gender: String,
    val pan_no: String,
    val place_of_birth: String,
    val full_address: String,
    val uid: String,
    val pin_code: String,
    val city: String,
    val district: String,
    val state: String,
    val country: String,
    val martial_status: String,
    val father_name: String,
    val mother_name: String
)
