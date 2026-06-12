package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfdetails

import kotlinx.serialization.Serializable

@Serializable
data class TransactionRules(
    val sip_allowed_dates: List<Int> = emptyList(),
    val sip_frequencies: List<String> = emptyList(),

    val min_investment_amount: String = "0",
    val min_lump_sum_amount: String = "0",
    val min_sip_amount: String = "0",

    val min_lumpsum_add_on_amount: String = "0",

    val min_redem_qty: String = "0",
    val min_redem_amount: String = "0",

    val min_daily_sip_amount: String = "0",
    val min_weekly_sip_amount: String = "0",
    val min_fortnightly_sip_amount: String = "0",
    val min_monthly_sip_amount: String = "0",
    val min_quarterly_sip_amount: String = "0",
    val min_semi_annual_sip_amount: String = "0",
    val min_annual_sip_amount: String = "0"
)