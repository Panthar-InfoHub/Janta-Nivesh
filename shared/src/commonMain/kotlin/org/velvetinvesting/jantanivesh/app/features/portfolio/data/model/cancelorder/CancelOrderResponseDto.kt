package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.cancelorder

import kotlinx.serialization.Serializable

@Serializable
data class CancelOrderResponseDto(
    val success: Boolean? = null,
    val message: String? = null,
    val data: CancelOrderDataDto? = null
)

@Serializable
data class CancelOrderDataDto(
    val code: Int? = null,
    val message: String? = null,
    val data: CancelOrderCanDataListDto? = null
)

@Serializable
data class CancelOrderCanDataListDto(
    val can_data: List<CancelOrderCanDataDto>? = null
)

@Serializable
data class CancelOrderCanDataDto(
    val client_code: String? = null,
    val order_no: String? = null,
    val remarks: String? = null,
    val can_status: String? = null,
    val can_remark: String? = null
)
