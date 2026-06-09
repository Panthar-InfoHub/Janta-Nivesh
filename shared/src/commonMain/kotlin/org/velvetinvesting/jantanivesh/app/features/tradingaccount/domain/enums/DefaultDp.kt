package org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums

enum class DefaultDp(
    val code: String,
    val displayName: String
) {
    CDSL("CDSL", "CDSL"),
    NSDL("NSDL", "NSDL");

    companion object {

        fun fromCode(code: String): DefaultDp? {
            return entries.firstOrNull { it.code == code }
        }

        fun getDisplayName(code: String): String {
            return fromCode(code)?.displayName.orEmpty()
        }
    }
}