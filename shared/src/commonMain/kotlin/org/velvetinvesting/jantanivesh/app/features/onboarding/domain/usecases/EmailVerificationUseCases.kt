package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.EmailVerification
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.OnboardingStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository.OnboardingRepo

/** Sends the code, and re-sends it when the user asks for another one from the OTP screen. */
class RequestEmailOtpUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(
        email: String
    ): NetworkResponse<OnboardingStatus, ErrorDomain> {
        return onboardingRepo.requestEmailOtp(email.trim())
    }
}

class VerifyEmailOtpUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(
        otp: String
    ): NetworkResponse<EmailVerification, ErrorDomain> {
        return onboardingRepo.verifyEmailOtp(otp)
    }
}
