package org.velvetinvesting.jantanivesh.app.features.kycnew.data.repository

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
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.GeoLocationBody
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.InvestorProfileBody
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.KycFormStatusResponseDto
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.MandateRequestBody
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.MandateResponseDto
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.MandateStatusResponseDto
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.NomineeAddressBody
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.NomineeBody
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.NomineePhoneBody
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.NomineeRequestBody
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.PANInitiateBody
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.PennyDropBody
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.SkipNomineeRequestBody
import org.velvetinvesting.jantanivesh.app.features.kycnew.data.model.toDomain
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.BankAccount
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.InvestorProfile
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.KYCError
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.KycFormStatus
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.Mandate
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.MandateStatus
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.Nominee
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.PANVerificationError
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.repository.OnboardingRepo

class OnboardingRepoImpl(
    private val client: HttpClient,
    private val authPrefs: AuthPrefs
) : OnboardingRepo {

    override suspend fun initiatePan(
        pan: String,
        name: String,
        dob: String
    ): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/onboarding/pan-verification")) {
                setBody(
                    PANInitiateBody(
                        date_of_birth = dob,
                        name = name,
                        pan = pan
                    )
                )
            }
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

    override suspend fun initiateKycForm(): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/onboarding/kyc-form"))
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

                authPrefs.setOnboardingStage(
                    OnboardingStage
                        .fromIdOrDefault(
                            responseDto.data?.onboarding?.current_stage
                        )
                        .id
                )

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
        return safeUnitRequest {
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
        return safeUnitRequest {
            client.post(getUrl("/onboarding/nominee")) {
                setBody(SkipNomineeRequestBody(skip = true))
            }
        }
    }

    private suspend fun postNominees(body: NomineeRequestBody): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/onboarding/nominee")) {
                setBody(body)
            }
        }
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
