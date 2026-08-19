package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.OnboardingStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository.OnboardingRepo

class SkipPANVerificationUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<OnboardingStatus, ErrorDomain> {
        return onboardingRepo.skipPan()
    }
}
