package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.purchaseplan

import kotlinx.serialization.Serializable

/** `GET /mf/purchase-plan` — the user's registered purchase plans, SIPs among them. */
@Serializable
data class MfPurchasePlansDto(
    val success: Boolean = false,
    val message: String = "",
    val data: MfPurchasePlansDataDto? = null
)

@Serializable
data class MfPurchasePlansDataDto(
    val purchase_plans: List<MfPurchasePlanDto> = emptyList()
)

/**
 * One plan. [systematic] is what separates a standing SIP from a one-off purchase that happens
 * to be listed here, and the installment counts are absent for a perpetual plan.
 */
@Serializable
data class MfPurchasePlanDto(
    val id: String = "",
    val user_id: String? = null,
    val plan_type: String? = null,
    val fp_id: String? = null,
    val scheme: String? = null,
    val folio_number: String? = null,
    val amount: String? = null,
    val units: String? = null,
    val systematic: Boolean = false,
    val frequency: String? = null,
    val installment_day: Int? = null,
    val next_installment_date: String? = null,
    val number_of_installments: Int? = null,
    val remaining_installments: Int? = null,
    val state: String? = null,
    val payment_source: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val mf_product: MfPurchasePlanProductDto? = null
)

@Serializable
data class MfPurchasePlanProductDto(
    val id: String? = null,
    val name: String? = null,
    val isin: String? = null,
    val img_url: String? = null,
    val latest_nav: String? = null,
    val latest_nav_date: String? = null,
    val scheme_plan: MfPurchasePlanSchemeDto? = null
)

@Serializable
data class MfPurchasePlanSchemeDto(
    val fund_category: String? = null,
    val sub_category: String? = null,
    val plan_type: String? = null,
    val option: String? = null
)
