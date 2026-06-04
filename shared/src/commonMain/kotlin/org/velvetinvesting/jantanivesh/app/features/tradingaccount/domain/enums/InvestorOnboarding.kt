package org.sharad.velvetinvestment.utils.tradingaccount

enum class InvestorOnboarding(
    val code: String,
    val displayName: String
) {

    PAPER(
        code = "P",
        displayName = "Paper (P)"
    ),

    PAPERLESS(
        code = "Z",
        displayName = "Paperless (Z)"
    );

    companion object {

        fun fromCode(code: String): InvestorOnboarding? {
            return entries.find { it.code == code }
        }

        fun getDisplayName(code: String): String {
            return fromCode(code)?.displayName.orEmpty()
        }
    }
}