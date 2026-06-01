package org.velvetinvesting.jantanivesh.app.features.kyc.uistate

enum class OccupationType(
    val code: String,
    val displayName: String
) {
    BUSINESS("01", "Business"),
    SERVICES("02", "Services"),
    PROFESSIONAL("03", "Professional"),
    AGRICULTURE("04", "Agriculture"),
    RETIRED("05", "Retired"),
    HOUSEWIFE("06", "Housewife"),
    STUDENT("07", "Student"),
    OTHERS("08", "Others");

    companion object {

        fun getDisplayNameFromCode(code: String?): String? {
            return entries
                .firstOrNull { it.code == code }
                ?.displayName
        }

        fun fromCode(code: String?): OccupationType? {
            return entries.firstOrNull { it.code == code }
        }
    }
}
