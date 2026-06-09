package org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums

enum class ClientType(
    val code: String,
    val displayName: String
) {
    DEMAT("D", "Demat"),
    PHYSICAL("P", "Physical");

    companion object {

        fun fromCode(code: String): ClientType? {
            return entries.firstOrNull { it.code == code }
        }

        fun getDisplayName(code: String): String {
            return fromCode(code)?.displayName.orEmpty()
        }
    }
}