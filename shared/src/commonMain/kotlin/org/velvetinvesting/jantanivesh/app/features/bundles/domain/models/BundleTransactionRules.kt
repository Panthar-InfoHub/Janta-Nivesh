package org.velvetinvesting.jantanivesh.app.features.bundles.domain.models

import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PurchaseMode
import kotlin.math.ceil

/**
 * What the bundle as a whole accepts, given the funds currently selected in its slots.
 *
 * Each minimum is the smallest bundle amount at which every slot's share still meets its fund's
 * own minimum; dates and frequencies are the ones every selected fund allows. A lumpsum, monthly
 * or daily minimum is null when any selected fund can't be bought that way, as no amount works.
 */
data class BundleTransactionRules(
    val minBundleSipAmount: Int?,
    val minBundleLumpsumAmount: Int?,

    val minDailySipAmount: Int?,
    val minWeeklySipAmount: Int,
    val minFortnightlySipAmount: Int,
    val minMonthlySipAmount: Int?,
    val minQuarterlySipAmount: Int,
    val minSemiAnnualSipAmount: Int,
    val minAnnualSipAmount: Int,

    val sipAllowedDates: List<Int>,
    /** SIP frequencies every fund allows, or null when none of the funds' rules are known. */
    val sipFrequencies: List<String>?
)

/**
 * Only funds that carry rules count here: pre-selected funds (their minimums only) and funds
 * fetched on their own. An empty date or frequency list means the API didn't send one, so it is
 * left out rather than intersected to nothing. Where no fund gives dates, they fall back to every
 * debit day; where none gives frequencies, [BundleTransactionRules.sipFrequencies] is null, read
 * as "not known".
 */
fun BundleDetailsDomain.deriveTransactionRules(): BundleTransactionRules {

    val slots = slots

    val knownRules = slots.mapNotNull { it.selectedFund?.transactionRules }

    val dates = knownRules
        .filter { it.sipAllowedDates.isNotEmpty() }
        .map { it.sipAllowedDates.toSet() }
        .reduceOrNull(Set<Int>::intersect)
        ?.sorted()
        ?: ALL_DEBIT_DAYS

    val frequencies = knownRules
        .filter { it.sipFrequencies.isNotEmpty() }
        .map { it.sipFrequencies.toSet() }
        .reduceOrNull(Set<String>::intersect)
        ?.sorted()

    return BundleTransactionRules(
        minBundleSipAmount = calculateBundleMinimum(slots) { it.minSipAmount },
        minBundleLumpsumAmount = calculateBundleMinimum(slots) { it.minLumpSumAmount },
        minDailySipAmount = calculateBundleMinimum(slots) { it.minDailySipAmount },
        minWeeklySipAmount = calculateBundleMinimum(slots) { it.minWeeklySipAmount } ?: 0,
        minFortnightlySipAmount = calculateBundleMinimum(slots) { it.minFortnightlySipAmount } ?: 0,
        minMonthlySipAmount = calculateBundleMinimum(slots) { it.minMonthlySipAmount },
        minQuarterlySipAmount = calculateBundleMinimum(slots) { it.minQuarterlySipAmount } ?: 0,
        minSemiAnnualSipAmount = calculateBundleMinimum(slots) { it.minSemiAnnualSipAmount } ?: 0,
        minAnnualSipAmount = calculateBundleMinimum(slots) { it.minAnnualSipAmount } ?: 0,
        sipAllowedDates = dates,
        sipFrequencies = frequencies
    )
}

/** No gateway offers a debit day past the 28th. */
private val ALL_DEBIT_DAYS = (1..28).toList()

/**
 * The bundle minimum for one way of investing, or null when any selected fund can't be bought that
 * way — its rules are missing or its minimum is null. Empty slots are left out; the bundle can't
 * be bought until they are filled anyway.
 */
private fun calculateBundleMinimum(
    slots: List<PortfolioSlotDomain>,
    selector: (TransactionRulesDomain) -> Long?
): Int? {
    var highest = 0
    for (slot in slots) {
        val fund = slot.selectedFund ?: continue
        val min = fund.transactionRules?.let(selector) ?: return null

        if (min > 0L && slot.allocationPercentage > 0.0) {
            val required = ceil(min * 100.0 / slot.allocationPercentage).toInt()
            highest = maxOf(highest, roundUpToNext10(required))
        }
    }
    return highest
}

private fun roundUpToNext10(value: Int): Int {
    return ((value + 9) / 10) * 10
}

/**
 * The bundle minimum for [mode], or null when a selected fund can't be bought that way. A fund's
 * general SIP minimum can sit above its monthly one, so for a monthly SIP the higher wins.
 */
fun BundleTransactionRules.minAmountFor(mode: PurchaseMode): Int? = when (mode) {
    PurchaseMode.DAILY -> minDailySipAmount
    PurchaseMode.MONTHLY -> {
        val monthly = minMonthlySipAmount
        val sip = minBundleSipAmount
        if (monthly == null || sip == null) null else maxOf(monthly, sip)
    }
    PurchaseMode.ONE_TIME -> minBundleLumpsumAmount
}
