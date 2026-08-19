package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.OnboardingStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.BankAccount
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.EmailVerification
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.InvestorProfile
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.KYCError
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.KycFormInitiation
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.KycFormStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.Mandate
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.MandateStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.Nominee
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.PANVerificationError

interface OnboardingRepo  {

    suspend fun submitBasicDetails(fullName: String, dob: String): NetworkResponse<OnboardingStatus, ErrorDomain>

    suspend fun initiatePan(pan:String) : NetworkResponse<Unit, ErrorDomain>

    suspend fun skipPan() : NetworkResponse<OnboardingStatus, ErrorDomain>

    suspend fun getPANVerificationStatus() : NetworkResponse<Unit, PANVerificationError>

    /**
     * `POST /onboarding/kyc-form`. Idempotent: it raises the form the first time and reports the
     * existing one thereafter, so the response is usable directly instead of always needing a
     * follow-up [getKycFormStatus].
     */
    suspend fun initiateKycForm() : NetworkResponse<KycFormInitiation, ErrorDomain>

    suspend fun getKycFormStatus() : NetworkResponse<KycFormStatus, KYCError>

    suspend fun uploadKycFormSignature(
        imageBytes: ByteArray,
        mimeType: String
    ) : NetworkResponse<Unit, ErrorDomain>

    suspend fun submitPennyDrop(bankAccount: BankAccount) : NetworkResponse<Unit, ErrorDomain>

    /**
     * Mails a 4-digit code to [email]. Also used to resend it, since the server treats a repeat
     * call as a fresh send.
     */
    suspend fun requestEmailOtp(email: String) : NetworkResponse<OnboardingStatus, ErrorDomain>

    /** Confirms the address the last [requestEmailOtp] was sent to. */
    suspend fun verifyEmailOtp(otp: String) : NetworkResponse<EmailVerification, ErrorDomain>

    suspend fun submitInvestorProfile(profile: InvestorProfile) : NetworkResponse<Unit, ErrorDomain>

    suspend fun submitNominees(nominees: List<Nominee>) : NetworkResponse<Unit, ErrorDomain>

    suspend fun skipNominees() : NetworkResponse<Unit, ErrorDomain>

    suspend fun createMandate(
        mandateLimit: Long,
        validFrom: String,
        paymentPostbackUrl: String
    ) : NetworkResponse<Mandate, ErrorDomain>

    /**
     * Reads the mandate back after the user has been through the authorization page. The bank's
     * verdict lives in [MandateStatus.mandateStatus], which the caller polls until it settles.
     */
    suspend fun confirmMandate(mandateId: Int) : NetworkResponse<MandateStatus, ErrorDomain>
}
