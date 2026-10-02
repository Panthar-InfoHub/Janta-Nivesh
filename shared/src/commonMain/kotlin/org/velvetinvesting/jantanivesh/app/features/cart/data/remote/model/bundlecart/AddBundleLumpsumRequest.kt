package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.bundlecart

import kotlinx.serialization.Serializable

/** `POST /mf/cart/bundle` body for a one-time bundle. */
@Serializable
data class AddBundleLumpsumRequest(
    val bundle_id: String,
    val cart_type: String = "LUMPSUM",
    val amount: Long,
    val selections: List<BundleFundSelectionRequest>
)
