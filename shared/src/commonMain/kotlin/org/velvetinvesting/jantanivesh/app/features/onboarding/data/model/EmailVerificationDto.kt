package org.velvetinvesting.jantanivesh.app.features.onboarding.data.model

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.EmailVerification

@Serializable
data class EmailVerificationResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: EmailVerificationDataDto? = null
)

@Serializable
data class EmailVerificationDataDto(
    val email: String? = null,
    val onboarding: OnboardingDto? = null
)

fun EmailVerificationResponseDto.toDomain(): EmailVerification {
    return EmailVerification(
        email = data?.email,
        onboarding = data?.onboarding?.toDomain()
    )
}
