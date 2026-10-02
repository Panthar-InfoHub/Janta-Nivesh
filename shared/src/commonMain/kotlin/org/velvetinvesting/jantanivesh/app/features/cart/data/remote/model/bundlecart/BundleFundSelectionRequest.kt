package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.bundlecart

import kotlinx.serialization.Serializable

/** One fund the user picked for a bundle, with its share of the bundle amount. */
@Serializable
data class BundleFundSelectionRequest(
    val mf_product_id: String,
    val allocation_percentage: Double
)
