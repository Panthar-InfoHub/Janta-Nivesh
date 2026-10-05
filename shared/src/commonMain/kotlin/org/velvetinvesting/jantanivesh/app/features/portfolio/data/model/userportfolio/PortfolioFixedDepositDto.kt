package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.userportfolio

import kotlinx.serialization.Serializable

/**
 * One fixed-deposit holding.
 *
 * `fixed_deposits` comes back empty today, so these fields mirror the previous FD shape and may
 * change once the server starts populating the list.
 */
@Serializable
data class PortfolioFixedDepositDto(
    val id: String,
    val title: String? = null,
    val category: String? = null,
    val amount: Double = 0.0,
    val start_date: String? = null,
    val `return`: Double = 0.0,
    val roi: Double = 0.0,
    val tenure_days: Int = 0,
    val status: String = "",
    val maturity_amount: Double = 0.0,
    val issuer_logo: String? = null,
    val maturity_date: String? = null
)
