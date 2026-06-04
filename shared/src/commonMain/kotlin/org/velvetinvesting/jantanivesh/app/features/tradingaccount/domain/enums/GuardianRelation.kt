package org.sharad.velvetinvestment.utils.tradingaccount

enum class GuardianRelation(
    val code: String,
    val displayName: String
) {
    FATHER(
        code = "06",
        displayName = "Father (06)"
    ),

    MOTHER(
        code = "13",
        displayName = "Mother (13)"
    ),

    COURT_APPOINTED(
        code = "23",
        displayName = "Court Appointed (23)"
    );

    companion object {
        fun getDisplayName(code: String): String {
            return entries.find { it.code == code }?.displayName ?: ""
        }
    }
}