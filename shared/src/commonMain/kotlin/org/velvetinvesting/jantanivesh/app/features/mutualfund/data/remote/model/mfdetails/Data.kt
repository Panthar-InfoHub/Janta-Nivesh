package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfdetails

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val id: String,

    val scheme_id: String?,
    val isin: String?,
    val img_url: String? = null,
    val mapping_code: String?,

    val scheme_name: String?,
    val display_name_001: String? = null,
    val display_name_002: String? = null,

    val amc_id: String?,
    val amc_code: String?,
    val amc_name: String?,

    val asset_type: String?,
    val scheme_type: String?,
    val structure: String?,

    val risk_name: String?,
    val risk_level: Int?,

    val latest_nav: String?,
    val latest_nav_date: String?,

    val purchase_allowed: Boolean?,
    val sip_allowed: Boolean?,
    val redemption_allowed: Boolean?,
    val switch_allowed: Boolean?,

    val maturity_date: String?,
    val nfo_end_date: String?,

    val createdAt: String?,
    val updatedAt: String?,

    val nse_scheme_code: String?,
    val platform_code: String?,

    val metrics: Metrics? = null,
    val transaction_rules: TransactionRules? = TransactionRules()
)