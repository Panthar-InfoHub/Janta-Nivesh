package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.frontendmfdata

import kotlinx.serialization.Serializable

@Serializable
data class SectionFundDto(
    val id: String,
    val name: String,
    val isin: String? = null,
    val img_url: String? = null,
    val latest_nav: String? = null,
    val latest_nav_date: String? = null,
    val metrics: SectionFundMetricsDto? = null
)
