package org.velvetinvesting.jantanivesh.app.features.plans.domain.model

/**
 * A fund scheme the user can start a SIP in. Only the monthly SIP threshold is modelled — the
 * other threshold types the gateway returns (lumpsum, switch, swp, stp) are not offered here.
 */
data class SchemePlan(
    val isin: String,
    val schemeName: String,
    val fundName: String,
    val option: String,
    val monthlySip: SipThreshold?
) {
    val isSipAvailable: Boolean
        get() = monthlySip != null
}

data class SipThreshold(
    val amountMin: Int,
    val amountMax: Long,
    val amountMultiples: Int,
    val installmentsMin: Int,
    /** Days of the month the gateway accepts as debit days, always within 1..28. */
    val dates: List<Int>
) {
    fun isAmountAllowed(amount: Int): Boolean =
        amount >= amountMin &&
                amount <= amountMax &&
                (amountMultiples <= 0 || amount % amountMultiples == 0)

    companion object {
        /** The regulator-wide ceiling: no gateway offers a debit day past the 28th. */
        val ALL_DEBIT_DAYS = (1..28).toList()
    }
}
