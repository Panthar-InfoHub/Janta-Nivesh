package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.bundlecart

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * `POST /mf/cart/bundle` body for a SIP bundle.
 *
 * [frequency] is `MONTHLY` or `DAILY`; only a monthly SIP has an [installment_day], and it is left
 * out of the body entirely for a daily one.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AddBundleSipRequest(
    val bundle_id: String,
    val cart_type: String = "SIP",
    val amount: Long,
    val frequency: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val installment_day: Int? = null,
    val clear_existing: Boolean = true,
    val selections: List<BundleFundSelectionRequest>
)
