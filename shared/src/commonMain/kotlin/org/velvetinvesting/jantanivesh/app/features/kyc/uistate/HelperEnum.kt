package org.velvetinvesting.jantanivesh.app.features.kyc.uistate

enum class Gender(
    val code: String,
    val displayName: String
) {
    MALE("M", "Male"),
    FEMALE("F", "Female");

    companion object{
        fun fromCode(code: String): Gender? {
            return entries.find { it.code == code }
        }
    }
}

enum class MaritalStatus(
    val code: String,
    val displayName: String,
) {
    MARRIED("MARRIED", "Married"),
    UNMARRIED("UNMARRIED", "Unmarried");
}
