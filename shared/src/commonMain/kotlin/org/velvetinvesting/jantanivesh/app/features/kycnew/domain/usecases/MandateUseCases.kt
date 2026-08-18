package org.velvetinvesting.jantanivesh.app.features.kycnew.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.Mandate
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.MandateStatus
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.repository.OnboardingRepo

class CreateMandateUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(
        mandateLimit: Long,
        validFrom: String,
        paymentPostbackUrl: String
    ): NetworkResponse<Mandate, ErrorDomain> {
        return onboardingRepo.createMandate(mandateLimit, validFrom, paymentPostbackUrl)
    }
}

class ConfirmMandateUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(mandateId: Int): NetworkResponse<MandateStatus, ErrorDomain> {
        return onboardingRepo.confirmMandate(mandateId)
    }
}
