package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mutualfundcombined

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val bundle_name: String,
    val bundle_products: List<BundleProduct>,
    val id: String,
    val accumulated_min_amount: Double,
    val img_url: String? = null
)