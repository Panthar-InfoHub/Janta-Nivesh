package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.PrefilledBankDetails
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.ReversePennyDropLinks
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.ReversePennyDropStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository.OnboardingRepo

class GetPrefilledBankDetailsUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<PrefilledBankDetails?, ErrorDomain> {
        return onboardingRepo.getPrefilledBankDetails()
    }
}

class InitiateReversePennyDropUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<ReversePennyDropLinks, ErrorDomain> {
        return onboardingRepo.initiateReversePennyDrop()
    }
}

class GetReversePennyDropStatusUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<ReversePennyDropStatus, ErrorDomain> {
        return onboardingRepo.getReversePennyDropStatus()
    }
}
