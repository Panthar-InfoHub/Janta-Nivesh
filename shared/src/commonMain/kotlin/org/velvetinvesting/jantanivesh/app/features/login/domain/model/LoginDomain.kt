package org.velvetinvesting.jantanivesh.app.features.login.domain.model

import org.velvetinvesting.jantanivesh.app.core.domain.model.OnboardingStage

data class LoginDomain(
    val onboarded: Boolean,
    val stage: OnboardingStage,
    val userId: String
)
