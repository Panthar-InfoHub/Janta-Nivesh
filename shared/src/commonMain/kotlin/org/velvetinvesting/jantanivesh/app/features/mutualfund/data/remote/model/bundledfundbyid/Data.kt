package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundledfundbyid

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val accumulated_min_amount: Double,
    val allowed_dates: List<Int>,
    val allowed_frequencies: List<String>,
    val bundle_name: String,
    val bundle_products: List<BundleProduct>,
    val id: String,
    val img_url: String? = null
)