package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class FolioFundsDto(
    val code: Int,
    val message: String,
    val `data`: List<FolioFundDataDto>
)

@Serializable
data class FolioFundDataDto(
    val id: String,
    val scheme_id: Int,
    val title: String,
    val category: String,
    val amount: Double,
    val is_sip: Boolean,
    val start_date: String,
    val return_percentage: String,
    val `return`: Double,
    val xirr: String,
    val current_nav: Double,
    val avg_nav: Double,
    val folio: String,
    val balance_units: Double,
    val img_url: String,
    val order_id: String? = ""
)
