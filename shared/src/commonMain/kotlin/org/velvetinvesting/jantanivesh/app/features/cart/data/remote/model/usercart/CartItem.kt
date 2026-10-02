package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.usercart

import kotlinx.serialization.Serializable

/**
 * One row of `GET /user/cart`, shared by the `sip` and `lumpsum` lists.
 *
 * [frequency] (`DAILY` / `MONTHLY`) and [installment_day] only apply to a SIP row.
 */
@Serializable
data class CartItem(
    val id: String,
    val user_id: String,
    val mf_product_id: String,
    val cart_type: String,
    val amount: String,
    val frequency: String? = null,
    val installment_day: Int? = null,
    val createdAt: String,
    val updatedAt: String,
    val mf_product: MfProduct
)

@Serializable
data class MfProduct(
    val id: String,
    val name: String,
    val isin: String? = null,
    val img_url: String? = null,
    val scheme_plan: SchemePlan? = null
)

@Serializable
data class SchemePlan(
    val lumpsum_amount_min: String? = null,
    val lumpsum_amount_max: String? = null,
    val lumpsum_amount_multiples: String? = null,
    val sip_daily_amount_min: String? = null,
    val sip_daily_amount_max: String? = null,
    val sip_daily_amount_multiples: String? = null,
    val sip_monthly_amount_min: String? = null,
    val sip_monthly_amount_max: String? = null,
    val sip_monthly_amount_multiples: String? = null,
    val sip_monthly_dates: List<Int> = emptyList()
)
