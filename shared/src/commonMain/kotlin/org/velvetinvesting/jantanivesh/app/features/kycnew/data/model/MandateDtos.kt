package org.velvetinvesting.jantanivesh.app.features.kycnew.data.model

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.Mandate
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.MandateStatus

@Serializable
data class MandateRequestBody(
    val mandate_limit: Long,
    val valid_from: String,
    val payment_postback_url: String
)

@Serializable
data class MandateResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: MandateDataDto? = null
)

@Serializable
data class MandateDataDto(
    val mandate_id: Int? = null,
    val token_url: String? = null,
    val status: String? = null
)

fun MandateResponseDto.toDomain(): Mandate = Mandate(
    id = data?.mandate_id,
    tokenUrl = data?.token_url,
    status = data?.status
)

@Serializable
data class MandateStatusResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: MandateStatusDataDto? = null
)

/**
 * The status lookup returns `mandate_id` as a string where creation returns it as a number, so
 * this cannot share [MandateDataDto].
 */
@Serializable
data class MandateStatusDataDto(
    val mandate_id: String? = null,
    val status: String? = null,
    val mandate_status: String? = null,
    val umrn: String? = null,
    val mandate_token: String? = null,
    val approved_at: String? = null,
    val rejected_reason: String? = null
)

fun MandateStatusResponseDto.toDomain(): MandateStatus = MandateStatus(
    mandateId = data?.mandate_id,
    status = data?.status,
    mandateStatus = data?.mandate_status,
    umrn = data?.umrn,
    approvedAt = data?.approved_at,
    rejectedReason = data?.rejected_reason
)
