package org.velvetinvesting.jantanivesh.app.features.kyc.domain.model

data class DigiLockerDetailsDomain(
    val aadhaarPdfUrl: String,
    val addressLine: String,
    val city: String,
    val contractPdfUrl: String?,
    val country: String,
    val createdAt: String,
    val digilockerPhotoUrl: String,
    val district: String,
    val dob: String,
    val fullAddress: String,
    val fullName: String,
    val gender: String,
    val id: String,
    val isFinalConfirmed: Boolean,
    val landMark: String,
    val pincode: String,
    val signatureUrl: String?,
    val state: String,
    val uid: String,
    val updatedAt: String,
    val userId: String,
    val userPhotoUrl: String?,
    val verifiedAt: String?
)
