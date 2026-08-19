package org.velvetinvesting.jantanivesh.app.features.onboarding.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PennyDropBody(
    val account_number: String,
    val ifsc_code: String,
    val bank_name: String,
    val account_holder_name: String,
    val account_type: String
)

@Serializable
data class GeoLocationBody(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class InvestorProfileBody(
    val email: String,
    val full_name: String,
    val dob: String,
    val gender: String,
    val address: String,
    val pincode: String,
    val city: String,
    val marital_status: String,
    val father_name: String,
    val place_of_birth: String,
    val occupation: String,
    val source_of_fund: String,
    val income_slab: String,
    val pep_confirmed: Boolean,
    val residency_confirmed: Boolean,
    val geolocation: GeoLocationBody,
    val spouse_name: String
)

@Serializable
data class NomineePhoneBody(
    val isd: String,
    val number: String
)

@Serializable
data class NomineeAddressBody(
    val line1: String,
    val city: String,
    val state: String,
    val postal_code: String,
    val country: String
)

@Serializable
data class NomineeBody(
    val nominee_name: String,
    val relationship: String,
    val percentage_allocation: Int,
    val dob: String,
    val document_type: String,
    val document_number: String,
    val email_address: String,
    val phone_number: NomineePhoneBody,
    val address: NomineeAddressBody
)

/**
 * `nominees` is omitted entirely when the user skips, which is why it is nullable rather than an
 * empty list — the server rejects an empty array alongside `skip = true`.
 */
@Serializable
data class NomineeRequestBody(
    val skip: Boolean,
    val nominees: List<NomineeBody>? = null
)
@Serializable
data class SkipNomineeRequestBody(
    val skip: Boolean,
)

@Serializable
data class BasicDetailsBody(
    val full_name: String,
    val date_of_birth: String
)

@Serializable
data class PANVerificationBody(
    val skip: Boolean,
    val pan: String? = null
)

@Serializable
data class EmailOtpRequestBody(
    val email: String
)

/**
 * No email is sent back on verification — the server pairs the code with the address it last sent
 * an OTP to for this session.
 */
@Serializable
data class EmailOtpVerifyBody(
    val otp: String
)
