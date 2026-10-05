package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.userportfolio

import kotlinx.serialization.Serializable

/**
 * One mutual-fund holding.
 *
 * [folio] is the one the row shows; [folios] is every folio rolled into it, which is why a
 * holding can be one row here and several on the folio screen.
 *
 * Every value is nullable: a holding with no units yet comes back with `null` NAV, XIRR and
 * return fields. [curr_nav] and [avg_nav] arrive as quoted strings and are read leniently.
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
    val amount: Double? = null,
    val current_value: Double? = null,
    val `return`: Double? = null,
    val curr_nav: Double? = null,
    val avg_nav: Double? = null,
    val folio: String? = null,
    val folios: List<String>? = null,
    val bal_units: Double? = null,
    val img_url: String? = null,
    val is_sip: Boolean? = null
)
