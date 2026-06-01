package org.velvetinvesting.jantanivesh.app.features.kyc.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.features.kyc.data.remote.model.*
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.model.*

fun DigiLockerDetailsDto.toDomain(): DigiLockerDetailsDomain {
    val data = this.data

    return DigiLockerDetailsDomain(
        aadhaarPdfUrl = data.aadhaar_pdf_url,
        addressLine = data.address_line,
        city = data.city,
        contractPdfUrl = data.contract_pdf_url,
        country = data.country,
        createdAt = data.createdAt,
        digilockerPhotoUrl = data.digilocker_photo_url,
        district = data.district,
        dob = data.dob,
        fullAddress = data.full_address,
        fullName = data.full_name,
        gender = data.gender,
        id = data.id,
        isFinalConfirmed = data.is_final_confirmed,
        landMark = data.land_mark,
        pincode = data.pincode,
        signatureUrl = data.signature_url,
        state = data.state,
        uid = data.uid,
        updatedAt = data.updatedAt,
        userId = data.user_id,
        userPhotoUrl = data.user_photo_url,
        verifiedAt = data.verified_at
    )
}

fun InitiateKycDto.toDomain(): KYCInitInfo {
    val data = this.data
    return KYCInitInfo(
        digilockerUrl = data.digilocker_url,
        kycAccessToken = data.kyc_access_token,
        kycTypeId = data.kyc_type_id,
        sessionId = data.session_id,
        userId = data.user_id
    )
}

fun KycFormDataDomain.toDto(): FormSubmissionDto {
    return FormSubmissionDto(
        kyc_data = KycDataDto(
            aadhaarNumber = aadhaarNumber,
            applicationStatusCode = applicationStatusCode,
            applicationStatusDescription = applicationStatusDescription,
            citizenshipCountry = citizenshipCountry,
            citizenshipCountryCode = citizenshipCountryCode,
            communicationAddressCode = communicationAddressCode,
            communicationAddressType = communicationAddressType,
            countryCode = countryCode,
            dob = dob,
            emailId = emailId,
            fatherName = fatherName,
            fatherTitle = fatherTitle,
            gender = gender,
            kycAccountCode = kycAccountCode,
            kycAccountDescription = kycAccountDescription,
            maritalStatus = maritalStatus,
            mobileNumber = mobileNumber,
            motherName = motherName,
            motherTitle = motherTitle,
            name = name,
            nomineeRelationShip = nomineeRelationShip,
            occupationCode = occupationCode,
            occupationDescription = occupationDescription,
            panNumber = panNumber,
            permanentAddressCode = permanentAddressCode,
            permanentAddressType = permanentAddressType,
            placeOfBirth = placeOfBirth,
            residentialStatus = residentialStatus
        )
    )
}
