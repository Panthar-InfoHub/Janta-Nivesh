package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model

data class BankAccount(
    val accountNumber: String,
    val ifscCode: String,
    val bankName: String,
    val accountHolderName: String,
    val accountType: String
)

enum class AccountType(val id: String) {
    SAVINGS("savings"),
    CURRENT("current")
}

data class GeoLocation(
    val latitude: Double,
    val longitude: Double
)

data class InvestorProfile(
    val email: String,
    val fullName: String,
    val dob: String,
    val gender: String,
    val address: String,
    val pincode: String,
    val city: String,
    val maritalStatus: String,
    val fatherName: String,
    val placeOfBirth: String,
    val occupation: String,
    val sourceOfFund: String,
    val incomeSlab: String,
    val pepConfirmed: Boolean,
    val residencyConfirmed: Boolean,
    val geoLocation: GeoLocation,
    val spouseName: String
)

data class NomineeAddress(
    val line1: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String
)

data class Nominee(
    val name: String,
    val relationship: String,
    val percentageAllocation: Int,
    val dob: String,
    val documentType: String,
    val documentNumber: String,
    val emailAddress: String,
    val phoneIsd: String,
    val phoneNumber: String,
    val address: NomineeAddress
)
