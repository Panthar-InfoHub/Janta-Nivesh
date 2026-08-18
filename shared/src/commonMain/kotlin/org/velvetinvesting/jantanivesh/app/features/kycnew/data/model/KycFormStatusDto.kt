package org.velvetinvesting.jantanivesh.app.features.kycnew.data.model

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.KycFormStatus

@Serializable
data class KycFormStatusResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: KycFormStatusDataDto? = null
)

@Serializable
data class KycFormStatusDataDto(
    val kyc_form_id: String? = null,
    val status: String? = null,
    val reason: String? = null,
    val fields_needed: List<String> = emptyList(),
    val proof_details: ProofDetailsDto? = null,
    val identity_proof_type: String? = null,
    val address_proof_type: String? = null,
    val signature_provided: Boolean = false,
    val esign_details: ESignDetailsDto? = null,
    val onboarding: KycOnboardingDto? = null
)

@Serializable
data class ProofDetailsDto(
    val fetch_url: String? = null,
    val status: String? = null
)

@Serializable
data class ESignDetailsDto(
    val esign_url: String? = null,
    val status: String? = null
)

@Serializable
data class KycOnboardingDto(
    val current_stage: String? = null,
    val is_completed: Boolean = false
)

fun KycFormStatusResponseDto.toDomain(): KycFormStatus {
    val data = this.data
    return KycFormStatus(
        kycFormId = data?.kyc_form_id,
        status = data?.status,
        fieldsNeeded = data?.fields_needed.orEmpty(),
        proofStatus = data?.proof_details?.status,
        proofFetchUrl = data?.proof_details?.fetch_url,
        signatureProvided = data?.signature_provided ?: false,
        esignStatus = data?.esign_details?.status,
        esignUrl = data?.esign_details?.esign_url,
        currentStage = data?.onboarding?.current_stage,
        isOnboardingCompleted = data?.onboarding?.is_completed ?: false
    )
}
