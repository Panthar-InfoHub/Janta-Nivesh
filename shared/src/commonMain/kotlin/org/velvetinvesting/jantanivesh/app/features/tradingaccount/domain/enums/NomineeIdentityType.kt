package org.sharad.velvetinvestment.utils.tradingaccount

enum class NomineeIdentityType(
    val code: String,
    val displayName: String
) {
    PAN(
        code = "1",
        displayName = "PAN"
    ),
    AADHAR(
        code = "2",
        displayName = "Aadhar"
    ),
    DRIVING_LICENSE(
        code = "3",
        displayName = "Driving Licence"
    ),
    OCI_PASSPORT(
        code = "4",
        displayName = "OCI / Passport"
    );

    companion object {

        fun fromCode(code: String?): NomineeIdentityType? {
            return entries.firstOrNull { it.code == code }
        }

        fun getDisplayNameFromCode(code: String?): String? {
            return fromCode(code)?.displayName
        }
    }
}