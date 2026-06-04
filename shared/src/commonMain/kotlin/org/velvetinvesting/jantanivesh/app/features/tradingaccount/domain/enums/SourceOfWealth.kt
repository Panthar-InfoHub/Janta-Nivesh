package org.sharad.velvetinvestment.utils.tradingaccount

enum class SourceOfWealth(
    val code: String,
    val displayName: String
) {

    SALARY(
        code = "01",
        displayName = "Salary (01)"
    ),

    BUSINESS_INCOME(
        code = "02",
        displayName = "Business Income (02)"
    ),

    GIFT(
        code = "03",
        displayName = "Gift (03)"
    ),

    ANCESTRAL_PROPERTY(
        code = "04",
        displayName = "Ancestral Property (04)"
    ),

    RENTAL_INCOME(
        code = "05",
        displayName = "Rental Income (05)"
    ),

    PRIZE_MONEY(
        code = "06",
        displayName = "Prize Money (06)"
    ),

    ROYALTY(
        code = "07",
        displayName = "Royalty (07)"
    ),

    OTHER(
        code = "08",
        displayName = "Other (08)"
    );

    companion object {

        fun getDisplayName(code: String): String {
            return entries.find { it.code == code }?.displayName ?: ""
        }

        fun fromCode(code: String): SourceOfWealth? {
            return entries.find { it.code == code }
        }
    }
}