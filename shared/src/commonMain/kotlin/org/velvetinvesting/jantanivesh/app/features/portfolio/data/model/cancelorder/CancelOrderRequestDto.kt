package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.cancelorder

import kotlinx.serialization.Serializable

@Serializable
data class CancelOrderRequestDto(
    val order_no: String
)
