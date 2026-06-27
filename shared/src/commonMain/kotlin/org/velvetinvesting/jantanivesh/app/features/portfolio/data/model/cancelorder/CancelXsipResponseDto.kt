package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.cancelorder

import kotlinx.serialization.Serializable

@Serializable
data class CancelXsipResponseDto(
    val success: Boolean? = null,
    val message: String? = null,
    val data: CancelXsipDataDto? = null
)

@Serializable
data class CancelXsipDataDto(
    val code: Int? = null,
    val message: String? = null,
    val data: CancelXsipCanDataListDto? = null
)

@Serializable
data class CancelXsipCanDataListDto(
    val can_data: List<CancelXsipCanDataDto>? = null
)

@Serializable
data class CancelXsipCanDataDto(
    val client_code: String? = null,
    val xsip_reg_no: String? = null,
    val remarks: String? = null,
    val send_2fa: String? = null,
    val send_comm: String? = null,
    val can_status: String? = null,
    val can_remark: String? = null
)
