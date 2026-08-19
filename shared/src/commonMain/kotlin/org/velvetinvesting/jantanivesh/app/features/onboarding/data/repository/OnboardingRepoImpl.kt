package org.velvetinvesting.jantanivesh.app.features.onboarding.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.util.network.UnresolvedAddressException
import org.velvetinvesting.jantanivesh.app.core.domain.model.OnboardingStage
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.ServerError
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
import org.velvetinvesting.jantanivesh.app.core.networking.safeUnitRequest
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.EmailOtpRequestBody
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.EmailOtpVerifyBody
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.EmailVerificationResponseDto
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.GeoLocationBody
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.InvestorProfileBody
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.KycFormInitiateResponseDto
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.KycFormStatusResponseDto
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.MandateRequestBody
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.MandateResponseDto
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.MandateStatusResponseDto
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.NomineeAddressBody
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.NomineeBody
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.NomineePhoneBody
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.NomineeRequestBody
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.BasicDetailsBody
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.OnboardingResponseDto
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.PANVerificationBody
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.toDomain
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.OnboardingStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.PennyDropBody
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.SkipNomineeRequestBody
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.model.toDomain
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
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository.OnboardingRepo

class OnboardingRepoImpl(
    private val client: HttpClient,
    private val authPrefs: AuthPrefs
) : OnboardingRepo {

    override suspend fun submitBasicDetails(
        fullName: String,
        dob: String
    ): NetworkResponse<OnboardingStatus, ErrorDomain> {
        val response = safeRequest<OnboardingResponseDto> {
            client.post(getUrl("/onboarding/basic-details/")) {
                setBody(
                    BasicDetailsBody(
                        full_name = fullName,
                        date_of_birth = dob
                    )
                )
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                val status = response.data.toDomain()
                persistStage(status.currentStage)
                NetworkResponse.Success(status)
            }

            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun initiatePan(
        pan: String
    ): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/onboarding/pan-verification")) {
                setBody(
                    PANVerificationBody(
                        skip = false,
                        pan = pan
                    )
                )
            }
        }
    }

    override suspend fun skipPan(): NetworkResponse<OnboardingStatus, ErrorDomain> {
        val response = safeRequest<OnboardingResponseDto> {
            client.post(getUrl("/onboarding/pan-verification")) {
                setBody(
                    PANVerificationBody(
                        skip = true
                    )
                )
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                val status = response.data.toDomain()
                persistStage(status.currentStage)
                NetworkResponse.Success(status)
            }

            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    /**
     * Success carries no data, so it maps to [Unit] like any other unit request. The failure side
     * is what matters here: the caller acts on the server's error `type`, which [ErrorDomain] would
     * flatten into [org.velvetinvesting.jantanivesh.app.core.networking.ErrorType.SERVER], so the
     * raw type is preserved in [PANVerificationError] instead.
     */
    override suspend fun getPANVerificationStatus(): NetworkResponse<Unit, PANVerificationError> {
        val response = try {
            client.get(getUrl("/onboarding/pan-verification/status"))
        } catch (e: UnresolvedAddressException) {
            return NetworkResponse.Error(
                PANVerificationError(
                    code = -1,
                    message = "No internet connection",
                    type = PANVerificationError.UNKNOWN
                )
            )
        } catch (e: Exception) {
            return NetworkResponse.Error(
                PANVerificationError(
                    code = -1,
                    message = "Unknown network error",
                    type = PANVerificationError.UNKNOWN
                )
            )
        }

        return if (response.status.value in 200..299) {
            NetworkResponse.Success(Unit)
        } else {
            NetworkResponse.Error(response.toPANVerificationError())
        }
    }

    override suspend fun initiateKycForm(): NetworkResponse<KycFormInitiation, ErrorDomain> {
        val response = safeRequest<KycFormInitiateResponseDto> {
            client.post(getUrl("/onboarding/kyc-form"))
        }
        return when (response) {
            is NetworkResponse.Success -> {
                val initiation = response.data.toDomain()
                initiation.onboarding?.let { persistStage(it.currentStage) }
                NetworkResponse.Success(initiation)
            }

            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun getKycFormStatus(): NetworkResponse<KycFormStatus, KYCError> {

        val response = try {
            client.get(
                getUrl("/onboarding/kyc-form/status")
            )
        } catch (e: UnresolvedAddressException) {
            return NetworkResponse.Error(
                KYCError(
                    code = -1,
                    message = "No internet connection",
                    type = KYCError.UNKNOWN
                )
            )
        } catch (e: Exception) {
            return NetworkResponse.Error(
                KYCError(
                    code = -1,
                    message = "Unknown network error",
                    type = KYCError.UNKNOWN
                )
            )
        }

        return if (response.status.value in 200..299) {
            try {
                val responseDto = response.body<KycFormStatusResponseDto>()

                persistStage(responseDto.data?.onboarding?.current_stage)

                NetworkResponse.Success(
                    responseDto.toDomain()
                )
            } catch (e: Exception) {
                NetworkResponse.Error(
                    KYCError(
                        code = -1,
                        message = "Failed to parse KYC status",
                        type = KYCError.UNKNOWN
                    )
                )
            }
        } else {
            NetworkResponse.Error(
                response.toKYCError()
            )
        }
    }

    override suspend fun uploadKycFormSignature(
        imageBytes: ByteArray,
        mimeType: String
    ): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(
                getUrl("/onboarding/kyc-form/signature")
            ) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                key = "file",
                                value = imageBytes,
                                headers = Headers.build {
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=\"signature.jpg\""
                                    )
                                    append(
                                        HttpHeaders.ContentType,
                                        mimeType
                                    )
                                }
                            )
                        }
                    )
                )
            }
        }
    }

    override suspend fun submitPennyDrop(
        bankAccount: BankAccount
    ): NetworkResponse<Unit, ErrorDomain> {
        val response = safeUnitRequest {
            client.post(getUrl("/onboarding/penny-drop")) {
                setBody(
                    PennyDropBody(
                        account_number = bankAccount.accountNumber,
                        ifsc_code = bankAccount.ifscCode,
                        bank_name = bankAccount.bankName,
                        account_holder_name = bankAccount.accountHolderName,
                        account_type = bankAccount.accountType
                    )
                )
            }
        }
        if (response is NetworkResponse.Success) {
            persistStage(OnboardingStage.EmailVerification)
        }
        return response
    }

    override suspend fun requestEmailOtp(
        email: String
    ): NetworkResponse<OnboardingStatus, ErrorDomain> {
        val response = safeRequest<OnboardingResponseDto> {
            client.post(getUrl("/onboarding/email/request-otp")) {
                setBody(EmailOtpRequestBody(email = email))
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                val status = response.data.toDomain()
                persistStage(status.currentStage)
                NetworkResponse.Success(status)
            }

            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun verifyEmailOtp(
        otp: String
    ): NetworkResponse<EmailVerification, ErrorDomain> {
        val response = safeRequest<EmailVerificationResponseDto> {
            client.post(getUrl("/onboarding/email/verify-otp")) {
                setBody(EmailOtpVerifyBody(otp = otp))
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                val verification = response.data.toDomain()
                persistStage(verification.onboarding?.currentStage)
                NetworkResponse.Success(verification)
            }

            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun submitInvestorProfile(
        profile: InvestorProfile
    ): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/onboarding/investor-profile/")) {
                setBody(
                    InvestorProfileBody(
                        email = profile.email,
                        full_name = profile.fullName,
                        dob = profile.dob,
                        gender = profile.gender,
                        address = profile.address,
                        pincode = profile.pincode,
                        city = profile.city,
                        marital_status = profile.maritalStatus,
                        father_name = profile.fatherName,
                        spouse_name=profile.spouseName,
                        place_of_birth = profile.placeOfBirth,
                        occupation = profile.occupation,
                        source_of_fund = profile.sourceOfFund,
                        income_slab = profile.incomeSlab,
                        pep_confirmed = profile.pepConfirmed,
                        residency_confirmed = profile.residencyConfirmed,
                        geolocation = GeoLocationBody(
                            latitude = profile.geoLocation.latitude,
                            longitude = profile.geoLocation.longitude
                        )
                    )
                )
            }
        }
    }

    override suspend fun submitNominees(
        nominees: List<Nominee>
    ): NetworkResponse<Unit, ErrorDomain> {
        return postNominees(
            NomineeRequestBody(
                skip = false,
                nominees = nominees.map { it.toBody() }
            )
        )
    }

    override suspend fun skipNominees(): NetworkResponse<Unit, ErrorDomain> {
        val response = safeUnitRequest {
            client.post(getUrl("/onboarding/nominee")) {
                setBody(SkipNomineeRequestBody(skip = true))
            }
        }
        if (response is NetworkResponse.Success) {
            persistStage(OnboardingStage.AutopaySetup)
        }
        return response
    }

    /**
     * The nominees are the last thing the server tracks, so from here the flow is on the autopay
     * mandate — which is why the stored stage moves to [OnboardingStage.AutopaySetup] and not to
     * completed.
     */
    private suspend fun postNominees(body: NomineeRequestBody): NetworkResponse<Unit, ErrorDomain> {
        val response = safeUnitRequest {
            client.post(getUrl("/onboarding/nominee")) {
                setBody(body)
            }
        }
        if (response is NetworkResponse.Success) {
            persistStage(OnboardingStage.AutopaySetup)
        }
        return response
    }

    override suspend fun createMandate(
        mandateLimit: Long,
        validFrom: String,
        paymentPostbackUrl: String
    ): NetworkResponse<Mandate, ErrorDomain> {
        val response = safeRequest<MandateResponseDto> {
            client.post(getUrl("/mandate/")) {
                setBody(
                    MandateRequestBody(
                        mandate_limit = mandateLimit,
                        valid_from = validFrom,
                        payment_postback_url = paymentPostbackUrl
                    )
                )
            }
        }
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun confirmMandate(
        mandateId: Int
    ): NetworkResponse<MandateStatus, ErrorDomain> {
        val response = safeRequest<MandateStatusResponseDto> {
            client.get(getUrl("/mandate/$mandateId"))
        }
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    /**
     * Keeps the locally remembered stage in step with the server's, so a restart resumes on the
     * screen the user actually reached. Always stored as a resume point, so `KYC_VERIFICATION`
     * (no screen of its own) and `COMPLETED` (autopay still outstanding) never reach storage.
     */
    private fun persistStage(currentStage: String?) {
        persistStage(OnboardingStage.resumePoint(currentStage))
    }

    /**
     * For the endpoints that answer with no body: the flow is linear from here, so the next screen
     * is known client-side and a relaunch should not replay the step that just succeeded.
     */
    private fun persistStage(stage: OnboardingStage) {
        authPrefs.setOnboardingStage(stage.id)
    }

    private fun Nominee.toBody() = NomineeBody(
        nominee_name = name,
        relationship = relationship,
        percentage_allocation = percentageAllocation,
        dob = dob,
        document_type = documentType,
        document_number = documentNumber,
        email_address = emailAddress,
        phone_number = NomineePhoneBody(
            isd = phoneIsd,
            number = phoneNumber
        ),
        address = NomineeAddressBody(
            line1 = address.line1,
            city = address.city,
            state = address.state,
            postal_code = address.postalCode,
            country = address.country
        )
    )

    private suspend fun HttpResponse.toPANVerificationError(): PANVerificationError {
        return try {
            val serverError = body<ServerError>()
            PANVerificationError(
                code = status.value,
                message = serverError.error.message,
                type = serverError.error.type
            )
        } catch (e: Exception) {
            PANVerificationError(
                code = status.value,
                message = "Unknown error",
                type = PANVerificationError.UNKNOWN
            )
        }
    }

    private suspend fun HttpResponse.toKYCError(): KYCError {
        return try {
            val serverError = body<ServerError>()

            KYCError(
                code = status.value,
                message = serverError.error.message,
                type = serverError.error.type
            )
        } catch (e: Exception) {
            KYCError(
                code = status.value,
                message = "Unknown error",
                type = KYCError.UNKNOWN
            )
        }
    }
}
