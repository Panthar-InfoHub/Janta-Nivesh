package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.KYCError
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.KycFormInitiation
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.KycFormStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository.OnboardingRepo

class InitiateKycFormUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<KycFormInitiation, ErrorDomain> {
        return onboardingRepo.initiateKycForm()
    }
}

class GetKycFormStatusUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<KycFormStatus, KYCError> {
        return onboardingRepo.getKycFormStatus()
    }
}

class UploadKycFormSignatureUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(
        imageBytes: ByteArray,
        mimeType: String
    ): NetworkResponse<Unit, ErrorDomain> {
        return onboardingRepo.uploadKycFormSignature(imageBytes, mimeType)
    }
}

