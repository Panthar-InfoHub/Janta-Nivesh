package org.velvetinvesting.jantanivesh.app.features.kycnew.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.PANVerificationError
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.repository.OnboardingRepo

class GetPANVerificationStatusUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<Unit, PANVerificationError> {
        return onboardingRepo.getPANVerificationStatus()
    }
}
