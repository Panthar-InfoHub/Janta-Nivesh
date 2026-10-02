package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.cartaddlumpsum

import kotlinx.serialization.Serializable

/** `POST /mf/cart` body for a one-time fund. */
@Serializable
data class AddCartLumpSumRequest(
    val mf_product_id: String,
    val cart_type: String = "LUMPSUM",
    val amount: Long
)
