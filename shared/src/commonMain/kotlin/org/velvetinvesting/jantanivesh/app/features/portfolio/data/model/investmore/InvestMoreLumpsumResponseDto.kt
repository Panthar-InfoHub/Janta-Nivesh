package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.investmore

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InvestMoreLumpsumResponseDto(
    @SerialName("success")
    val success: Boolean,

    @SerialName("message")
    val message: String,

    @SerialName("data")
    val paymentUrl: String
)