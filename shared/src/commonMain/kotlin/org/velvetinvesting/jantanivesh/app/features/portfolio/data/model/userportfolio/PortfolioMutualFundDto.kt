package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.userportfolio

import kotlinx.serialization.Serializable

/**
 * One mutual-fund holding.
 *
 * [folio] is the one the row shows; [folios] is every folio rolled into it, which is why a
 * holding can be one row here and several on the folio screen.
 *
 * [is_sip] is not in the payload yet — it is declared now, defaulted to null, so the SIP/Lumpsum
 * tag lights up on its own once the server starts sending it.
 */
@Serializable
data class PortfolioMutualFundDto(
    val id: String,
    val title: String? = null,
    val category: String? = null,
    val sub_category: String? = null,
    val nav_as_on: String? = null,
    val xirr: Double? = null,
    val return_percentage: String? = null,
    val amount: Double = 0.0,
    val current_value: Double = 0.0,
    val `return`: Double = 0.0,
    val curr_nav: Double = 0.0,
    val avg_nav: Double = 0.0,
    val folio: String? = null,
    val folios: List<String> = emptyList(),
    val bal_units: Double = 0.0,
    val img_url: String? = null,
    val is_sip: Boolean? = null
)
