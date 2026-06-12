package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundledfunds

import kotlinx.serialization.Serializable

@Serializable
data class Bundle(
    val bundle_name: String,
    val bundle_products: List<BundleProduct>,
    val id: String
)