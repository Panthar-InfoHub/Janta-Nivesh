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

    /**
     * Reported by the server but never a destination of its own: the DigiLocker and signature
     * steps hang off the PAN screen, which is what decides whether they are needed. Use
     * [Companion.resumePoint] rather than navigating to or persisting this stage.
     */
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

    /**
     * Client-side only. The server considers onboarding finished once the nominees are in, but the
     * user still has to authorize the autopay mandate before the app lets them through, so the
     * server's `COMPLETED` resumes here instead.
     */
    @Serializable
    data object AutopaySetup : OnboardingStage {
        override val id = "AUTOPAY_SETUP"
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
            AutopaySetup,
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

        /**
         * A server-reported stage as the app records it. [KycVerification] has no screen of its
         * own — the DigiLocker and signature steps hang off the PAN screen, which re-reads the
         * server and routes into them if they are still owed — so it is folded into
         * [PanVerification] and never stored or navigated to.
         */
        fun normalize(serverStage: String?): OnboardingStage =
            when (val stage = fromIdOrDefault(serverStage)) {
                KycVerification -> PanVerification
                else -> stage
            }

        /**
         * Where the onboarding flow should start for a stage. Only meaningful once the app has
         * decided the user belongs in onboarding at all: on top of [normalize] it sends
         * [Completed] to [AutopaySetup], because the mandate is a step the server does not track
         * and so is still outstanding for anyone the server calls finished but the app does not.
         *
         * A user who is already through onboarding never reaches this — the completed flag routes
         * them to the main app without the stage being read.
         */
        fun resumePoint(serverStage: String?): OnboardingStage =
            when (val stage = normalize(serverStage)) {
                Completed -> AutopaySetup
                else -> stage
            }
    }
}