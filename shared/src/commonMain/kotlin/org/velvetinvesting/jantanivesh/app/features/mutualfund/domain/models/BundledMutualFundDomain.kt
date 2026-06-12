package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models

data class BundledMutualFundDomain(
    val categoryName: String,
    val key: String,
    val mutualFunds: List<BundledMutualFundItemDomain>,
    val sipDates: List<Int> = emptyList(),
    val minAmount: Double = 0.0,
    val allowedFrequencies: List<InvestmentFrequency> = emptyList(),
    val img_url: String
)

data class BundledMutualFundItemDomain(
    val id: String,
    val scheme_id: String,
    val isin: String,
    val mapping_code: String,
    val nse_scheme_code: String,
    val platform_code: String,
    val scheme_name: String,
    val amc_id: String,
    val amc_code: String,
    val amc_name: String,
    val asset_type: String,
    val scheme_type: String,
    val structure: String,
    val risk_name: String,
    val risk_level: Int,
    val latest_nav: String,
    val latest_nav_date: String,
    val purchase_allowed: Boolean,
    val sip_allowed: Boolean,
    val redemption_allowed: Boolean,
    val switch_allowed: Boolean,
    val maturity_date: String?,
    val nfo_end_date: String?,
    val createdAt: String,
    val updatedAt: String,
    val allocation_percentage: Int,
    val minAmount: String,
    val metrics: FundMetricsDomain? = null,
    val icon: String,
)

data class FundMetricsDomain(
    val return1Y: Double?,
    val return3Y: Double?,
    val return6M: Double?,
    val return90D: Double?
)