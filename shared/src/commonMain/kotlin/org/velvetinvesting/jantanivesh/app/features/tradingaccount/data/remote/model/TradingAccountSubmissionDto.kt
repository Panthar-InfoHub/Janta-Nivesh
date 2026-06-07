package org.velvetinvesting.jantanivesh.app.features.tradingaccount.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TradingAccountSubmissionDto(
    val `data`: SubmissionDataDto,
    val message: String,
    val success: Boolean
)

@Serializable
data class SubmissionDataDto(
    val short_url: String
)
