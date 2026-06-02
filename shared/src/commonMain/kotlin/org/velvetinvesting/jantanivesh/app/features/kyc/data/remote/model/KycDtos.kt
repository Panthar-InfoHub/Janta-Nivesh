package org.velvetinvesting.jantanivesh.app.features.kyc.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class InitiateKycDto(
    val data: InitiateKycDataDto,
    val message: String,
    val success: Boolean
)

@Serializable
data class InitiateKycDataDto(
    val digilocker_url: String,
    val kyc_access_token: String,
    val kyc_type_id: String,
    val session_id: String,
    val user_id: String
)

@Serializable
data class DigiLockerDetailsDto(
    val data: DigiLockerDetailsDataDto,
    val message: String,
    val success: Boolean
)

@Serializable
data class DigiLockerDetailsDataDto(
    val aadhaar_pdf_url: String,
    val address_line: String,
    val city: String,
    val contract_pdf_url: String?,
    val country: String,
    val createdAt: String,
    val digilocker_photo_url: String,
    val district: String,
    val dob: String,
    val full_address: String,
    val full_name: String,
    val gender: String,
    val id: String,
    val is_final_confirmed: Boolean,
    val land_mark: String,
    val pincode: String,
    val signature_url: String?,
    val state: String,
    val uid: String,
    val updatedAt: String,
    val user_id: String,
    val user_photo_url: String?,
    val verified_at: String?
)

@Serializable
data class FormSubmissionDto(
    val kyc_data: KycDataDto
)

@Serializable
data class KycDataDto(
    val aadhaarNumber: String,
    val applicationStatusCode: String,
    val applicationStatusDescription: String,
    val citizenshipCountry: String,
    val citizenshipCountryCode: String,
    val communicationAddressCode: String,
    val communicationAddressType: String,
    val countryCode: Int,
    val dob: String,
    val emailId: String,
    val fatherName: String,
    val fatherTitle: String,
    val gender: String,
    val kycAccountCode: String,
    val kycAccountDescription: String,
    val maritalStatus: String,
    val mobileNumber: String,
    val motherName: String,
    val motherTitle: String,
    val name: String,
    val nomineeRelationShip: String,
    val occupationCode: String,
    val occupationDescription: String,
    val panNumber: String,
    val permanentAddressCode: String,
    val permanentAddressType: String,
    val placeOfBirth: String,
    val residentialStatus: String
)

@Serializable
data class ImageUploadDto(
    val file: ImageFileDto
)

@Serializable
data class ImageFileDto(
    val directURL: String
)

@Serializable
data class UrlUploadBodyDto(
    val type: String,
    val img_url: String
)

@Serializable
data class UrlUploadResultDto(
    val success: Boolean
)

@Serializable
data class ContractPdfDto(
    val data: ContractPdfDataDto
)

@Serializable
data class ContractPdfDataDto(
    val `object`: ContractPdfObjectDto
)

@Serializable
data class ContractPdfObjectDto(
    val result: ContractPdfResultDto
)

@Serializable
data class ContractPdfResultDto(
    val combinedPdf: String
)

@Serializable
data class ESignDto(
    val data: String,
    val success: Boolean
)
