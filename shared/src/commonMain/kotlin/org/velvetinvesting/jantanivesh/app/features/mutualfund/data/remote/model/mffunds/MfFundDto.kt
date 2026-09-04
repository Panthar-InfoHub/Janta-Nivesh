package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mffunds

import kotlinx.serialization.Serializable

/**
 * One row of the fund list. Only [id] is guaranteed: this endpoint returns the scheme identity
 * and its returns, and leaves out the category/risk detail the fund-details call carries.
 */
@Serializable
data class MfFundDto(
    val id: String,
    val name: String? = null,
    val isin: String? = null,
    val img_url: String? = null,
    val latest_nav: String? = null,
    val latest_nav_date: String? = null,
    val metrics: MfFundMetricsDto? = null
)
