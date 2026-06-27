package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class MutualFundPortfolioDto(
    val id: String,
    val scheme_id: Int,
    val bal_units: Double,
    val title: String,
    val category: String,
    val amount: Double,
    val current_value: Double,
    val `return`: Double,
    val return_percentage: String,
    val folio: String,
    val img_url: String? = null,
    val transaction_rules: TransactionRules
)

@Serializable
data class TransactionRules(
    val min_sip_amount: String,
    val min_lump_sum_amount: String
)