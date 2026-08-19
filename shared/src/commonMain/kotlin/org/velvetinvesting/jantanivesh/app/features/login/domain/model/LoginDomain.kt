package org.velvetinvesting.jantanivesh.app.features.login.domain.model

import org.velvetinvesting.jantanivesh.app.core.domain.model.OnboardingStage

data class LoginDomain(
    /**
     * Whether the user goes straight to the main app. True when the server finished onboarding,
     * and also when they deliberately deferred the optional part of it — see
     * [org.velvetinvesting.jantanivesh.app.features.login.data.mapper.toLoginDomain].
     */
    val canEnterMainApp: Boolean,

    /**
     * Already resolved through [OnboardingStage.normalize], so it is safe to persist and to hand
     * to the onboarding flow, which resolves its own start destination from it.
     */
    val stage: OnboardingStage,
    val userId: String
)
