package org.velvetinvesting.jantanivesh.app.features.bundles.domain.models

import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.utils.toTitleCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SchemePlan

/** `sip_frequencies` codes the bundle reads to tell which SIP modes every fund allows. */
private const val SIP_FREQUENCY_DAILY = "D"
private const val SIP_FREQUENCY_MONTHLY = "OM"

/**
 * A fund picked from the full fund list, as a bundle slot holds it. The list item gives its
 * identity and returns; its [plan] gives the limits, so it counts towards the bundle minimums.
 */
fun MutualFundDomain.toBundleFund(plan: SchemePlan): FundDomain {
    val productId = plan.id.ifBlank { id }
    return FundDomain(
        id = productId,
        name = plan.schemeName.ifBlank { name.toTitleCase() },
        isin = plan.isin.ifBlank { isin.orEmpty() },
        imageUrl = icon,
        latestNav = latestNav,
        latestNavDate = latestNavDate.orEmpty(),
        metrics = FundMetricsDomain(
            return1M = returnYearsRate.month1 ?: 0.0,
            return1Y = returnYearsRate.year1 ?: 0.0,
            return3Y = returnYearsRate.year3 ?: 0.0,
            return5Y = returnYearsRate.year5 ?: 0.0,
            return6M = returnYearsRate.month6 ?: 0.0,
            return90D = returnYearsRate.month3 ?: 0.0
        ),
        amcName = plan.fundName,
        assetType = type,
        riskName = riskText.orEmpty(),
        sipAllowed = plan.monthlySip != null || plan.dailySip != null,
        transactionRules = plan.toTransactionRules(productId)
    )
}

/**
 * The scheme's limits in the shape the bundle minimums are worked out from. A mode the scheme
 * doesn't offer has a null minimum here, which is what marks the fund unavailable for it.
 */
private fun SchemePlan.toTransactionRules(mfProductId: String): TransactionRulesDomain {
    val monthlyMin = monthlySip?.amountMin?.toLong()
    val lumpsumMin = lumpsum?.amountMin?.toLong()
    return TransactionRulesDomain(
        id = "",
        mfProductId = mfProductId,
        minSipAmount = monthlyMin,
        minLumpSumAmount = lumpsumMin,
        minInvestmentAmount = lumpsumMin ?: 0L,
        minLumpsumAddOnAmount = lumpsum?.additionalAmountMin?.toLong() ?: 0L,
        minRedemptionQty = 0,
        minRedemptionAmount = 0L,
        minDailySipAmount = dailySip?.amountMin?.toLong(),
        minWeeklySipAmount = 0L,
        minFortnightlySipAmount = 0L,
        minMonthlySipAmount = monthlyMin,
        minQuarterlySipAmount = 0L,
        minSemiAnnualSipAmount = 0L,
        minAnnualSipAmount = 0L,
        sipAllowedDates = monthlySip?.dates.orEmpty(),
        sipFrequencies = listOfNotNull(
            SIP_FREQUENCY_DAILY.takeIf { dailySip != null },
            SIP_FREQUENCY_MONTHLY.takeIf { monthlySip != null }
        ),
        createdAt = "",
        updatedAt = ""
    )
}
