package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.allbundles

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val accumulated_min_amount: Int,
    val bundle_name: String,
    val bundle_products: List<BundleProduct>,
    val id: String,
    val img_url: String? = null
)