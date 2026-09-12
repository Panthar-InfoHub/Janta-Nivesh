package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models


data class MutualFundDomain(
    val id: String,
    val name: String,
    val icon: String,
    val category: String = "",
    val remark: String? = null,
    val riskText: String? = null,
    val type: String = "",
    val returnYearsRate: ReturnYearsRateDomain,
    val latestNav: String = "",
    val isin: String? = null,
    val latestNavDate: String? = null,
)

data class ReturnYearsRateDomain(
    val month3:Double?,
    val month6:Double?,
    val year1:Double?,
    val year3:Double?,
    val month1:Double? = null,
    val year5:Double? = null,
    val navChangePct:Double? = null,
)

/**
 * The return figure for one period, or null where the gateway reports none for it.
 *
 * The list card and the ranking behind it read the same value through here, so a fund can never
 * be placed by one figure while showing another.
 */
fun ReturnYearsRateDomain.forPeriod(period: SelectedReturnRatePeriod): Double? = when (period) {
    SelectedReturnRatePeriod.THREE_MONTH -> month3
    SelectedReturnRatePeriod.SIX_MONTH -> month6
    SelectedReturnRatePeriod.ONE_YEAR -> year1
    SelectedReturnRatePeriod.THREE_YEAR -> year3
}
