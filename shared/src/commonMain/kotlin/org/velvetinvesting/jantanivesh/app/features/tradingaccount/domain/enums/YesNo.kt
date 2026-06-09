package org.sharad.velvetinvestment.utils.tradingaccount

enum class YesNo(
    val code: String,
    val displayName: String
) {
    YES(
        code = "Y",
        displayName = "Yes"
    ),
    NO(
        code = "N",
        displayName = "No"
    );

    companion object {

        fun fromCode(code: String?): YesNo? {
            return entries.firstOrNull { it.code == code }
        }

        fun displayNameFromCode(code: String?): String {
            return fromCode(code)?.displayName.orEmpty()
        }
    }
}