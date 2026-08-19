package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model

data class OnboardingStatus(
    val currentStage: String?,
    val isCompleted: Boolean,
    val stages: OnboardingStages
)

/** A stage is null when the endpoint that produced this status does not report on it. */
data class OnboardingStages(
    val basicDetails: String? = null,
    val kyc: String? = null,
    val nominee: String? = null,
    val pennyDrop: String? = null,
    val profile: String? = null,
    val readiness: String? = null,
    val email: String? = null
)
