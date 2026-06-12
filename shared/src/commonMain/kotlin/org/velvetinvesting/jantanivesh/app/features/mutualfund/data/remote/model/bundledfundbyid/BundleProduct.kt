package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundledfundbyid

import kotlinx.serialization.Serializable

@Serializable
data class BundleProduct(
    val allocation_percentage: Int,
    val bundle_id: String,
    val id: String,
    val mf_product: MfProduct,
    val mf_product_id: String,
    val min_amount: String
)