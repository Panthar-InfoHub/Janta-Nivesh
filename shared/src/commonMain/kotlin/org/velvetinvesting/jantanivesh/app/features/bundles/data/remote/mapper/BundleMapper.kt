package org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model.AllBundlesDto
import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model.BundleCategoryDto
import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model.BundleDetailsDto
import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model.BundleFundDto
import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model.BundleFundMetricsDto
import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model.BundleMetaDataDto
import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model.BundlePreselectedFundDto
import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model.BundleSummaryCategoryDto
import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model.BundleSummaryDto
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.AssetAllocationDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleCategoryDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleCategorySummaryDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleMetaDataDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleRisk
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.FundDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.FundMetricsDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.PortfolioSlotDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.TransactionRulesDomain
import kotlinx.serialization.json.contentOrNull
import org.velvetinvesting.jantanivesh.app.features.mutualfund.utils.toTitleCase

fun AllBundlesDto.toDomain(): List<BundleSummaryDomain> = data.bundles.map { it.toDomain() }

private fun BundleSummaryDto.toDomain(): BundleSummaryDomain {
    return BundleSummaryDomain(
        id = id,
        name = bundle_name,
        description = bundle_description.orEmpty(),
        imageUrl = img_url.orEmpty(),
        assetAllocation = AssetAllocationDomain(
            equity = equity_percentage,
            debt = debt_percentage,
            hybrid = hybrid_percentage,
            commodity = commodity_percentage
        ),
        metaData = meta_data.toDomain(),
        categories = categories.map { it.toDomain() }
    )
}

private fun BundleSummaryCategoryDto.toDomain(): BundleCategorySummaryDomain {
    return BundleCategorySummaryDomain(
        id = id,
        categoryName = category_name,
        displayName = display_name,
        allocationPercentage = total_percentage,
        slotCount = slots.size
    )
}

private fun BundleMetaDataDto?.toDomain(): BundleMetaDataDomain {
    return BundleMetaDataDomain(
        riskLevel = this?.risk_level.orEmpty(),
        investmentTime = this?.investment_time.orEmpty(),
        investmentGrowth = this?.investment_growth.orEmpty(),
        startAmount = this?.start_amount?.let { kotlin.math.ceil(it).toLong() },
        // NEW: daily and monthly SIP start amounts, rounded up to the rupee like start_amount.
        dailyStartAmount = this?.daily_start_amount?.let { kotlin.math.ceil(it).toLong() },
        monthlyStartAmount = this?.monthly_start_amount?.let { kotlin.math.ceil(it).toLong() },
        risk = this?.risk_level.toBundleRisk()
    )
}

/**
 * The API's risk wording (Low, Moderate, High, Very High) to a [BundleRisk]. Case, surrounding
 * spaces, underscores and a trailing "RISK" are ignored, so "VERY_HIGH", "very high risk" and
 * " Very High " read the same. Any wording with "moderate" in it — "Moderate", "Low to Moderate",
 * "Moderately High" — is [BundleRisk.MODERATE]. Anything unmatched is drawn in a neutral style.
 */
private fun String?.toBundleRisk(): BundleRisk {
    val risk = this?.trim()?.replace('_', ' ')?.uppercase()?.removeSuffix(" RISK")?.trim()
        ?: return BundleRisk.UNKNOWN
    return when {
        "MODERATE" in risk -> BundleRisk.MODERATE
        risk == "LOW" -> BundleRisk.LOW
        risk == "HIGH" -> BundleRisk.HIGH
        risk == "VERY HIGH" -> BundleRisk.VERY_HIGH
        else -> BundleRisk.UNKNOWN
    }
}

fun BundleDetailsDto.toDomain(): BundleDetailsDomain {
    return BundleDetailsDomain(
        name = data.bundle_name,
        description = data.bundle_description.orEmpty(),
        assetAllocation = AssetAllocationDomain(
            equity = data.equity_percentage,
            debt = data.debt_percentage,
            hybrid = data.hybrid_percentage,
            commodity = data.commodity_percentage
        ),
        metaData = data.meta_data.toDomain(),
        categories = data.categories.map { it.toDomain() }
    )
}

/**
 * Each slot's default is the fund the API pre-selects for it. Where a slot has none, it is looked
 * up by `pre_selected_product_id` in the category's list, and failing that the slots fall back to
 * rank order — the first slot takes the first fund, the second the second — so two slots of one
 * category don't default to the same fund. Whatever lands here is stored as the slot's default
 * and starts out as its selection.
 */
private fun BundleCategoryDto.toDomain(): BundleCategoryDomain {

    val domainFunds = funds.map { it.toDomain() }

    return BundleCategoryDomain(
        id = id,
        categoryName = category_name,
        displayName = display_name,
        allocationPercentage = total_percentage,
        funds = domainFunds,
        slots = slots
            .sortedBy { it.default_rank }
            .mapIndexed { index, slot ->
                val defaultFund = slot.pre_selected_fund?.toDomain()
                    ?: domainFunds.find { it.id == slot.pre_selected_product_id }
                    ?: domainFunds.getOrNull(index)
                PortfolioSlotDomain(
                    id = slot.id,
                    allocationPercentage = slot.allocation_percentage,
                    rank = slot.default_rank,
                    defaultFund = defaultFund,
                    selectedFund = defaultFund
                )
            }
    )
}

private fun BundleFundDto.toDomain(): FundDomain {
    return FundDomain(
        id = id,
        name = name.orEmpty().toTitleCase(),
        isin = isin.orEmpty(),
        imageUrl = img_url.orEmpty(),
        latestNav = latest_nav?.contentOrNull.orEmpty(),
        latestNavDate = latest_nav_date.orEmpty(),
        metrics = metrics.toDomain(),
        // NEW: a category fund now carries its minimums too, so it counts towards the bundle
        // minimums once picked.
        transactionRules = scheme_plan?.let {
            minimumsToTransactionRules(
                mfProductId = id,
                lumpsumMin = it.lumpsum_amount_min.toAmountOrNull(),
                monthlySipMin = it.sip_monthly_amount_min.toAmountOrNull(),
                dailySipMin = it.sip_daily_amount_min.toAmountOrNull()
            )
        }
    )
}

/**
 * A pre-selected fund carries its lumpsum, monthly and daily SIP minimums, which become partial
 * transaction rules so the bundle minimums count it.
 */
private fun BundlePreselectedFundDto.toDomain(): FundDomain {
    return FundDomain(
        id = id,
        name = name.orEmpty().toTitleCase(),
        isin = isin.orEmpty(),
        imageUrl = img_url.orEmpty(),
        latestNav = latest_nav?.contentOrNull.orEmpty(),
        latestNavDate = latest_nav_date.orEmpty(),
        metrics = returns.toDomain(),
        transactionRules = min_investment?.let {
            minimumsToTransactionRules(
                mfProductId = id,
                lumpsumMin = it.lumpsum_min.toAmountOrNull(),
                monthlySipMin = it.sip_monthly_min.toAmountOrNull(),
                dailySipMin = it.sip_daily_min.toAmountOrNull()
            )
        }
    )
}

/**
 * A fund's three minimums as partial transaction rules; a null one means the fund can't be bought
 * that way. No SIP dates or frequencies are sent, so those stay empty — read as "not known" when
 * the bundle's rules are derived.
 */
private fun minimumsToTransactionRules(
    mfProductId: String,
    lumpsumMin: Long?,
    monthlySipMin: Long?,
    dailySipMin: Long?
): TransactionRulesDomain {
    return TransactionRulesDomain(
        id = "",
        mfProductId = mfProductId,
        minSipAmount = monthlySipMin,
        minLumpSumAmount = lumpsumMin,
        minInvestmentAmount = 0,
        minLumpsumAddOnAmount = 0,
        minRedemptionQty = 0,
        minRedemptionAmount = 0,
        // NEW: was always 0; now the fund's daily SIP minimum.
        minDailySipAmount = dailySipMin,
        minWeeklySipAmount = 0,
        minFortnightlySipAmount = 0,
        minMonthlySipAmount = monthlySipMin,
        minQuarterlySipAmount = 0,
        minSemiAnnualSipAmount = 0,
        minAnnualSipAmount = 0,
        sipAllowedDates = emptyList(),
        sipFrequencies = emptyList(),
        createdAt = "",
        updatedAt = ""
    )
}

/** A minimum given as a number, or null when it isn't sent; a fraction rounds up to the rupee. */
private fun Double?.toAmountOrNull(): Long? = this?.let { kotlin.math.ceil(it).toLong() }

/** A minimum given as a string ("500", "0.01"); a fraction rounds up, and a missing or junk value is null. */
private fun String?.toAmountOrNull(): Long? = this?.toDoubleOrNull().toAmountOrNull()

private fun BundleFundMetricsDto?.toDomain(): FundMetricsDomain {
    return FundMetricsDomain(
        return1M = this?.return_30d ?: 0.0,
        return1Y = this?.return_1y ?: 0.0,
        return3Y = this?.return_3y ?: 0.0,
        return5Y = this?.return_5y ?: 0.0,
        return6M = this?.return_6m ?: 0.0,
        return90D = this?.return_90d ?: 0.0
    )
}
