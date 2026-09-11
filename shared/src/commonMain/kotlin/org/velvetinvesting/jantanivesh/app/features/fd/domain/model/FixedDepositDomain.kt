package org.velvetinvesting.jantanivesh.app.features.fd.domain.model

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

/** Whole years stay years; anything else reads in months, or days for a slab under a month. */
fun TenureRangeList.label(): String = when (this) {
    is TenureRangeList.Years -> "${years}Y"
    is TenureRangeList.Days -> if (days >= 30) "${days / 30}M" else "${days}D"
}
