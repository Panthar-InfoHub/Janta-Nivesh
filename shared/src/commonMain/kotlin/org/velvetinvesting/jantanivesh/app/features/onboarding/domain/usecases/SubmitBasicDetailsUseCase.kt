package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.OnboardingStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository.OnboardingRepo

class SubmitBasicDetailsUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(
        fullName: String,
        dob: String
    ): NetworkResponse<OnboardingStatus, ErrorDomain> {
        return onboardingRepo.submitBasicDetails(fullName, dob)
    }
}
