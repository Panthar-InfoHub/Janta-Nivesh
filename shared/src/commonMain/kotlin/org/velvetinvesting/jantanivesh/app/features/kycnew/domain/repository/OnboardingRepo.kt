package org.velvetinvesting.jantanivesh.app.features.kycnew.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.BankAccount
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.InvestorProfile
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.KYCError
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.KycFormStatus
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.Mandate
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.MandateStatus
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.Nominee
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.PANVerificationError

interface OnboardingRepo  {

    suspend fun initiatePan(pan:String, name: String, dob: String) : NetworkResponse<Unit, ErrorDomain>

    suspend fun getPANVerificationStatus() : NetworkResponse<Unit, PANVerificationError>

    suspend fun initiateKycForm() : NetworkResponse<Unit, ErrorDomain>

    suspend fun getKycFormStatus() : NetworkResponse<KycFormStatus, KYCError>

    suspend fun uploadKycFormSignature(
        imageBytes: ByteArray,
        mimeType: String
    ) : NetworkResponse<Unit, ErrorDomain>

    suspend fun submitPennyDrop(bankAccount: BankAccount) : NetworkResponse<Unit, ErrorDomain>

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
