package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.PANVerificationError
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository.OnboardingRepo

class GetPANVerificationStatusUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<Unit, PANVerificationError> {
        return onboardingRepo.getPANVerificationStatus()
    }
}
