package org.velvetinvesting.jantanivesh.app.features.plans.domain.model

/**
 * A fund scheme the user can invest in, with the limits that bound each way of buying it.
 *
 * The server exposes a limit set per transaction type; the three the purchase screen offers —
 * monthly SIP, daily SIP and one-time lumpsum — are modelled here. A null threshold means the
 * scheme does not allow that mode, and the screen hides its tab.
 */
data class SchemePlan(
    /**
     * The product id, which is what the purchase and SIP endpoints take as `mf_product_id`. The
     * ISIN identifies the scheme to look up; this identifies the product to buy.
     */
    val id: String,
    val isin: String,
    val schemeName: String,
    val fundName: String,
    val option: String,
    val planType: String = "",
    val isActive: Boolean = true,
    val monthlySip: SipThreshold?,
    val dailySip: SipThreshold? = null,
    val lumpsum: SipThreshold? = null
) {
    val isSipAvailable: Boolean
        get() = monthlySip != null

    /** The threshold backing [mode], or null when the scheme does not offer that mode. */
    fun thresholdFor(mode: PurchaseMode): SipThreshold? = when (mode) {
        PurchaseMode.DAILY -> dailySip
        PurchaseMode.MONTHLY -> monthlySip
        PurchaseMode.ONE_TIME -> lumpsum
    }
}

/**
 * The three ways this fund can be bought. [frequency] is what the SIP endpoint expects; it is
 * unused for [ONE_TIME], which goes to the lumpsum endpoint instead.
 */
enum class PurchaseMode(val label: String, val frequency: String) {
    DAILY("Daily", "daily"),
    MONTHLY("Monthly", "monthly"),
    ONE_TIME("One-time", "");

    val isSip: Boolean
        get() = this != ONE_TIME

    /** Only a monthly SIP debits on a fixed day of the month. */
    val needsInstallmentDay: Boolean
        get() = this == MONTHLY

    companion object {
        /** Recovers a mode from a navigation argument; an unknown name falls back to monthly. */
        fun fromName(name: String?): PurchaseMode =
            entries.firstOrNull { it.name == name } ?: MONTHLY
    }
}

data class SipThreshold(
    val amountMin: Int,
    val amountMax: Long,
    val amountMultiples: Int,
    val installmentsMin: Int,
    /** Days of the month the gateway accepts as debit days, always within 1..28. */
    val dates: List<Int>,
    /**
     * Minimum for a top-up into an existing folio, where that differs from the first purchase.
     * Null when the server reports no separate figure.
     */
    val additionalAmountMin: Int? = null
) {
    fun isAmountAllowed(amount: Int): Boolean =
        amount >= amountMin &&
                amount <= amountMax &&
                (amountMultiples <= 0 || amount % amountMultiples == 0)

    /**
     * The quick-pick chips on the purchase screen. Anchored on the minimum so a fund with a
     * ₹500 floor offers ₹500/₹1,000/₹2,500/₹5,000, and one with a ₹30 daily floor scales down.
     */
    val suggestedAmounts: List<Int>
        get() = CHIP_MULTIPLIERS.map { amountMin * it }.filter { it <= amountMax }

    companion object {
        /** The regulator-wide ceiling: no gateway offers a debit day past the 28th. */
        val ALL_DEBIT_DAYS = (1..28).toList()

        private val CHIP_MULTIPLIERS = listOf(1, 2, 5, 10)
    }
}
