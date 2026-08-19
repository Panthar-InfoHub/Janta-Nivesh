package org.velvetinvesting.jantanivesh.app.features.onboarding.data.model

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.OnboardingStages
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.OnboardingStatus

@Serializable
data class OnboardingResponseDto(
    val success: Boolean,
    val message: String,
    val data: OnboardingDataDto
)

@Serializable
data class OnboardingDataDto(
    val onboarding: OnboardingDto
)

@Serializable
data class OnboardingDto(
    val current_stage: String? = null,
    val is_completed: Boolean = false,
    val stages: OnboardingStagesDto? = null
)

/**
 * Every field is optional because the server trims this map per endpoint — `/onboarding/kyc-form`
 * for instance omits `basic_details` and `email`.
 */
@Serializable
data class OnboardingStagesDto(
    val basic_details: String? = null,
    val kyc: String? = null,
    val nominee: String? = null,
    val penny_drop: String? = null,
    val profile: String? = null,
    val readiness: String? = null,
    val email: String? = null
)

fun OnboardingResponseDto.toDomain(): OnboardingStatus = data.onboarding.toDomain()

fun OnboardingDto.toDomain(): OnboardingStatus {
    return OnboardingStatus(
        currentStage = current_stage,
        isCompleted = is_completed,
        stages = stages?.toDomain() ?: OnboardingStages()
    )
}

fun OnboardingStagesDto.toDomain(): OnboardingStages {
    return OnboardingStages(
        basicDetails = basic_details,
        kyc = kyc,
        nominee = nominee,
        pennyDrop = penny_drop,
        profile = profile,
        readiness = readiness,
        email = email
    )
}
