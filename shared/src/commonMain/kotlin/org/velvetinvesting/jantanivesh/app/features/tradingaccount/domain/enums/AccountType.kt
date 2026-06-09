package org.sharad.velvetinvestment.utils.tradingaccount

enum class AccountType(
    val code: String,
    val displayName: String
) {
    SAVINGS_BANK("SB", "Savings Bank (SB)"),
    CURRENT_BANK("CB", "Current Bank (CB)"),
    NRE_ACCOUNT("NE", "NRE Account (NE)"),
    NRO_ACCOUNT("NO", "NRO Account (NO)");

    companion object {

        fun fromCode(code: String): AccountType? {
            return entries.firstOrNull { it.code == code }
        }

        fun getDisplayName(code: String): String {
            return fromCode(code)?.displayName.orEmpty()
        }
    }
}