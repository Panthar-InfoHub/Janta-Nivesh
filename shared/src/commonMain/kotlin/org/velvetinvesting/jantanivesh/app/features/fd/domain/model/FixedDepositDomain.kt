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
