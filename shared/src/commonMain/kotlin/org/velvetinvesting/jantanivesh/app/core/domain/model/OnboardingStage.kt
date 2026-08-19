package org.velvetinvesting.jantanivesh.app.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface OnboardingStage {

    val id: String

    @Serializable
    data object BasicDetails : OnboardingStage {
        override val id = "BASIC_DETAILS"
    }

    @Serializable
    data object PanVerification : OnboardingStage {
        override val id = "PAN_VERIFICATION"
    }

    @Serializable
    data object KycVerification : OnboardingStage {
        override val id = "KYC_VERIFICATION"
    }

    @Serializable
    data object PennyDropVerification : OnboardingStage {
        override val id = "PENNY_DROP_VERIFICATION"
    }

    @Serializable
    data object EmailVerification : OnboardingStage {
        override val id = "EMAIL_VERIFICATION"
    }

    @Serializable
    data object InvestorProfile : OnboardingStage {
        override val id = "INVESTOR_PROFILE"
    }

    @Serializable
    data object NomineeAddition : OnboardingStage {
        override val id = "NOMINEE_ADDITION"
    }

    @Serializable
    data object Completed : OnboardingStage {
        override val id = "COMPLETED"
    }

    companion object {

        // Explicitly typed to avoid Kotlin/Native type inference
        // recursing through the companion object.
        val entries: List<OnboardingStage> = listOf(
            BasicDetails,
            PanVerification,
            KycVerification,
            PennyDropVerification,
            EmailVerification,
            InvestorProfile,
            NomineeAddition,
            Completed
        )

        fun fromId(id: String): OnboardingStage? =
            entries.firstOrNull {
                it.id.equals(id, ignoreCase = true)
            }

        fun fromIdOrDefault(
            id: String?,
            default: OnboardingStage = BasicDetails
        ): OnboardingStage =
            fromId(id.orEmpty()) ?: default
    }
}