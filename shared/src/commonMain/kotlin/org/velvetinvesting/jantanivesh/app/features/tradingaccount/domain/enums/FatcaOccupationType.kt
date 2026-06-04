package org.sharad.velvetinvestment.utils.tradingaccount

enum class FatcaOccupationType(
    val code: String,
    val displayName: String
) {

    SERVICE(
        code = "S",
        displayName = "Service (S)"
    ),

    BUSINESS(
        code = "B",
        displayName = "Business (B)"
    ),

    OTHERS(
        code = "O",
        displayName = "Others (O)"
    ),

    NOT_CATEGORIZED(
        code = "X",
        displayName = "Not Categorized (X)"
    );

    companion object {

        fun getDisplayName(code: String): String {
            return entries.find { it.code == code }?.displayName ?: ""
        }

        fun fromCode(code: String): FatcaOccupationType? {
            return entries.find { it.code == code }
        }
    }
}