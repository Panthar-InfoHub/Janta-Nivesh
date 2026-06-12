package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfgraph

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val createdAt: String,
    val daily_change: Double?,
    val id: String,
    val mf_product_id: String,
    val nav: String,
    val nav_date: String,
    val scheme_id: String,
    val updatedAt: String
)