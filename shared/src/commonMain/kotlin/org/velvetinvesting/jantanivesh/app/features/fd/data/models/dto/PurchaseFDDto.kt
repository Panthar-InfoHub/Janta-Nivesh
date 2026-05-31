package org.velvetinvesting.jantanivesh.app.features.fd.data.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseFDBodyDto(
    val investment_amount: Long,
    val investment_period: Int,
    val payout_frequency: String,
    val product_id: String,
    val tenure: Int
)

@Serializable
data class PurchaseFDDto(
    val `data`: String,
    val message: String,
    val success: Boolean
)
