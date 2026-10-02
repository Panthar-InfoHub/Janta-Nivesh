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
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.PennyDropStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.PrefilledBankDetails
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.ReversePennyDropLinks
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.ReversePennyDropStatus

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
     * The bank's verdict on the account [accountNumber] was submitted for. Verification is
     * asynchronous, so this is polled after [submitPennyDrop] until the status settles.
     */
    suspend fun getPennyDropStatus(
        accountNumber: String
    ) : NetworkResponse<PennyDropStatus, ErrorDomain>

    /**
     * Bank details a reverse penny drop has already read, or null when there are none yet — in
     * which case the user has to make the ₹1 verification payment first.
     */
    suspend fun getPrefilledBankDetails() : NetworkResponse<PrefilledBankDetails?, ErrorDomain>

    /** Raises the ₹1 reverse penny drop and returns the links the user can pay it through. */
    suspend fun initiateReversePennyDrop() : NetworkResponse<ReversePennyDropLinks, ErrorDomain>

    /**
     * Whether the ₹1 payment has come through. Asynchronous on the bank's side, so it is polled
     * after the user comes back from the payment page.
     */
    suspend fun getReversePennyDropStatus() : NetworkResponse<ReversePennyDropStatus, ErrorDomain>

    /**
     * Mails a 4-digit code to [email]. Also used to resend it, since the server treats a repeat
     * call as a fresh send.
     */
    suspend fun requestEmailOtp(email: String) : NetworkResponse<OnboardingStatus, ErrorDomain>

    /** Confirms the address the last [requestEmailOtp] was sent to. */
    suspend fun verifyEmailOtp(otp: String) : NetworkResponse<EmailVerification, ErrorDomain>

    suspend fun submitInvestorProfile(profile: InvestorProfile) : NetworkResponse<Unit, ErrorDomain>

    /** `GET /frontend/city` — the city for a 6-digit [pincode], to prefill the profile's city. */
    suspend fun getCityByPincode(pincode: String) : NetworkResponse<String, ErrorDomain>

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
