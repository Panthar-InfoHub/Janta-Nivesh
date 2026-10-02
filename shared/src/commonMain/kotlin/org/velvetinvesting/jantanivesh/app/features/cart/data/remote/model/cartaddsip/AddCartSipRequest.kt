package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.cartaddsip

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * `POST /mf/cart` body for a SIP fund.
 *
 * [frequency] is `MONTHLY` or `DAILY`; only a monthly SIP has an [installment_day], and it is left
 * out of the body entirely for a daily one.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AddCartSipRequest(
    val mf_product_id: String,
    val cart_type: String = "SIP",
    val amount: Long,
    val frequency: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val installment_day: Int? = null
)
