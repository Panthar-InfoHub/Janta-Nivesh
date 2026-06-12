package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.getmf

import kotlinx.serialization.Serializable

@Serializable
data class MutualFund(
    val amc_code: String?,
    val amc_id: String?,
    val amc_name: String?,
    val asset_type: String?,
    val createdAt: String?,
    val id: String,
    val isin: String?,
    val latest_nav: String?,
    val latest_nav_date: String?,
    val mapping_code: String?,
    val maturity_date: String?,
    val metrics: Metrics?,
    val nfo_end_date: String?,
    val nse_scheme_code: String?,
    val platform_code: String?,
    val purchase_allowed: Boolean?,
    val redemption_allowed: Boolean?,
    val risk_level: Int?,
    val risk_name: String?,
    val scheme_id: String?,
    val scheme_name: String,
    val scheme_type: String?,
    val sip_allowed: Boolean?,
    val structure: String?,
    val switch_allowed: Boolean?,
    val updatedAt: String?,
    val img_url: String? = null
)