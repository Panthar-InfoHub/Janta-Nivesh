package org.sharad.velvetinvestment.utils.tradingaccount

enum class DividendPayMode(
    val code: String,
    val displayName: String
) {
    CHEQUE("01", "Cheque"),
    DIRECT_CREDIT("02", "Direct Credit"),
    ECS("03", "ECS"),
    NEFT("04", "NEFT"),
    RTGS("05", "RTGS");

    companion object {

        fun fromCode(code: String): DividendPayMode? {
            return entries.firstOrNull { it.code == code }
        }

        fun getDisplayName(code: String): String {
            return fromCode(code)?.displayName.orEmpty()
        }
    }
}