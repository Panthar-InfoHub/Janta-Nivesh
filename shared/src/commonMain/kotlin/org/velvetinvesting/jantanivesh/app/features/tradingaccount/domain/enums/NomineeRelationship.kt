package org.sharad.velvetinvestment.utils.tradingaccount

enum class NomineeRelationship(
    val code: String,
    val displayName: String
) {
    AUNT("01", "Aunt"),
    BROTHER_IN_LAW("02", "Brother-In-Law"),
    BROTHER("03", "Brother"),
    DAUGHTER("04", "Daughter"),
    DAUGHTER_IN_LAW("05", "Daughter-In-Law"),
    FATHER("06", "Father"),
    FATHER_IN_LAW("07", "Father-In-Law"),
    GRAND_DAUGHTER("08", "Grand Daughter"),
    GRAND_FATHER("09", "Grand Father"),
    GRAND_MOTHER("10", "Grand Mother"),
    GRAND_SON("11", "Grand Son"),
    MOTHER_IN_LAW("12", "Mother-In-Law"),
    MOTHER("13", "Mother"),
    NEPHEW("14", "Nephew"),
    NIECE("15", "Niece"),
    SISTER("16", "Sister"),
    SISTER_IN_LAW("17", "Sister-In-Law"),
    SON("18", "Son"),
    SON_IN_LAW("19", "Son-In-Law"),
    SPOUSE("20", "Spouse"),
    UNCLE("21", "Uncle"),
    OTHERS("22", "Others");

    companion object {

        fun fromCode(code: String?): NomineeRelationship? {
            return entries.firstOrNull { it.code == code }
        }

        fun getDisplayNameFromCode(code: String?): String? {
            return fromCode(code)?.displayName
        }
    }
}