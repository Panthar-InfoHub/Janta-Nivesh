package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.frontendmfdata

import kotlinx.serialization.Serializable

@Serializable
data class SectionFundMetricsDto(
    val return_1y: Double? = null,
    val return_3y: Double? = null,
    val return_5y: Double? = null,
    val return_6m: Double? = null,
    val return_90d: Double? = null,
    val return_30d: Double? = null,
    val nav_change_pct: Double? = null
)
