package org.velvetinvesting.jantanivesh.app.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface OnboardingStage {

    val id: String

    @Serializable
    data object Readiness : OnboardingStage {
        override val id = "PAN_VERIFICATION"
    }

//    @Serializable
//    data object KYC : OnboardingStage {
//        override val id = "kyc"
//    }

    @Serializable
    data object PennyDrop : OnboardingStage {
        override val id = "PENNY_DROP_VERIFICATION"
    }

    @Serializable
    data object Profile : OnboardingStage {
        override val id = "INVESTOR_PROFILE"
    }

    @Serializable
    data object Nominee : OnboardingStage {
        override val id = "NOMINEE_ADDITION"
    }
    @Serializable
    data object Completed : OnboardingStage {
        override val id = "COMPLETED"
    }

    companion object {
        // Explicitly typed: inferring it recurses through the objects back into this companion,
        // which the Kotlin/Native frontend resolves as List<Any>.
        val entries: List<OnboardingStage> = listOf(
            Readiness,
//            KYC,
            PennyDrop,
            Profile,
            Nominee,
            Completed
        )

        fun fromId(id: String): OnboardingStage? =
            entries.firstOrNull { it.id.equals(id, ignoreCase = true) }

        fun fromIdOrDefault(
            id: String?,
            default: OnboardingStage = Readiness
        ): OnboardingStage =
            fromId(id.orEmpty()) ?: default
    }
}