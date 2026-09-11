package org.velvetinvesting.jantanivesh.app.features.fd.domain.model

import kotlin.math.roundToInt

data class FixedDepositDomain(
    val id: String,
    val bankName: String,
    val bankLogoUrl: String,
    val riskLevel: RiskLevel,
    val baseInterest: Double,
    val minDeposit: Long,
    val tenures: List<FixedDepositTenureDomain>,
    val bankTag: String,
    val tags: List<String>
)

data class FixedDepositTenureDomain(
    val tenure: TenureRangeList,
    val tenureDays: Int,
    val interestRate: Double,
    val receiveMin: Long,
    val receiveMax: Long
)

sealed class TenureRangeList {
    data class Days(val days: Int) : TenureRangeList()
    data class Years(val years: Int) : TenureRangeList()

    companion object {
        fun fromDays(days: Int): TenureRangeList {
            return if (days % 365 == 0) {
                Years(days / 365)
            } else {
                Days(days)
            }
        }
    }
}

/**
 * The tenure paying the most, which is the rate [FixedDepositDomain.baseInterest] already reports —
 * this resolves the slab behind that figure so the list can name the term it belongs to.
 */
val FixedDepositDomain.bestTenure: FixedDepositTenureDomain?
    get() = tenures.maxByOrNull { it.interestRate }

/** "3Y", "1Y 6M", "9M" — see [toTenureLabel]. */
fun TenureRangeList.label(): String = when (this) {
    // Years only ever comes from an exact multiple of 365, so the day count round-trips.
    is TenureRangeList.Years -> years * 365
    is TenureRangeList.Days -> days
}.toTenureLabel()

/**
 * A tenure in days, read the way a bank quotes it: "3Y" for a whole term, "1Y 6M" when there are
 * months left over, "9M" under a year, and days only for a slab shorter than a month.
 *
 * The leftover months are rounded rather than truncated, so the 548-day and 540-day spellings of
 * an eighteen-month deposit both come out as "1Y 6M" instead of one of them drifting to "1Y 5M".
 * That rounding can reach a full twelve months, which rolls into the year rather than reading
 * "1Y 12M".
 */
fun Int.toTenureLabel(): String {
    if (this < DAYS_IN_MONTH) return "${this}D"

    var years = this / DAYS_IN_YEAR
    var months = ((this % DAYS_IN_YEAR) / DAYS_IN_MONTH.toDouble()).roundToInt()
    if (months == 12) {
        years++
        months = 0
    }

    return when {
        years > 0 && months > 0 -> "${years}Y ${months}M"
        years > 0 -> "${years}Y"
        else -> "${months}M"
    }
}

private const val DAYS_IN_YEAR = 365

private const val DAYS_IN_MONTH = 30
