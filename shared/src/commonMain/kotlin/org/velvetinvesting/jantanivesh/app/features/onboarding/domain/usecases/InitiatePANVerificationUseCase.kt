package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository.OnboardingRepo

class InitiatePANVerificationUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(
        pan: String
    ): NetworkResponse<Unit, ErrorDomain> {
        return onboardingRepo.initiatePan(pan = pan)
    }
}
