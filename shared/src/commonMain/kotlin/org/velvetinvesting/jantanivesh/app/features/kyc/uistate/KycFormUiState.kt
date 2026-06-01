package org.velvetinvesting.jantanivesh.app.features.kyc.uistate

data class KycFormUiState(

    // --- Personal Info ---
    val name: String = "",
    val dob: String = "",
    val gender: String = "",
    val maritalStatus: String = "",

    // --- Parents Info ---
    val fatherName: String = "",
    val fatherTitle: String = "Mr.",
    val motherName: String = "",
    val motherTitle: String = "Mrs.",

    // --- Identity ---
    val panNumber: String = "",
    val aadhaarNumber: String = "",

    // --- Contact ---
    val emailId: String = "",
    val mobileNumber: String = "",
    val countryCode: Int = 91,

    // --- Address / Residency ---
    val placeOfBirth: String = "",
    val residentialStatus: String = "Resident Individual",

    // --- Occupation ---
    val occupationCode: String ="",
    val occupationDescription: String = "",

    // --- KYC Account ---
    val kycAccountCode: String = "01",
    val kycAccountDescription: String = "New",

    // --- Citizenship ---
    val citizenshipCountry: String = "India",
    val citizenshipCountryCode: String = "101",

    // --- Application Status ---
    val applicationStatusCode: String = "R",
    val applicationStatusDescription: String = "Resident Indian",

    // --- Address Types ---
    val communicationAddressCode: String = "02",
    val communicationAddressType: String = "Residential",
    val permanentAddressCode: String = "02",
    val permanentAddressType: String = "Residential",

    // --- Nominee ---
    val nomineeRelationShip: String = "FATHER"
) {
    fun isValid(): Boolean {
        return name.isNotBlank() &&
                dob.isNotBlank() &&
                gender.isNotBlank() &&
                maritalStatus.isNotBlank() &&
                fatherName.isNotBlank() &&
                motherName.isNotBlank() &&
                panNumber.isNotBlank() &&
                aadhaarNumber.isNotBlank() &&
                emailId.isNotBlank() &&
                mobileNumber.isNotBlank() &&
                placeOfBirth.isNotBlank() &&
                occupationCode.isNotBlank() &&
                occupationDescription.isNotBlank()
    }
}
