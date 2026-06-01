package org.velvetinvesting.jantanivesh.app.features.kyc.uistate

enum class Gender(
    val code: String,
    val displayName: String
) {
    MALE("M", "Male"),
    FEMALE("F", "Female")
}

enum class MaritalStatus(
    val code: String,
    val displayName: String,
) {
    MARRIED("MARRIED", "Married"),
    UNMARRIED("UNMARRIED", "Unmarried")
}
