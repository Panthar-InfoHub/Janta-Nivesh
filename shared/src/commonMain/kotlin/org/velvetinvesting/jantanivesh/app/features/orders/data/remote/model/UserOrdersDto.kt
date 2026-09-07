package org.velvetinvesting.jantanivesh.app.features.orders.data.remote.model

import kotlinx.serialization.Serializable

/** `GET /user/orders` — every mutual-fund transaction the user has placed. */
@Serializable
data class UserOrdersDto(
    val success: Boolean = false,
    val message: String = "",
    val data: UserOrdersDataDto? = null
)

@Serializable
data class UserOrdersDataDto(
    val transactions: List<OrderTransactionDto> = emptyList(),
    val pagination: OrdersPaginationDto? = null
)

/**
 * One order.
 *
 * The payload is one row shape for three different things — a purchase, a redemption and a
 * switch — so most fields are only filled for the kind of order that has them: [amount] for a
 * purchase, [units] for a unit-based redemption, the `*_at` stamps for whichever states the
 * order has actually reached. Everything is therefore optional, and the money figures arrive as
 * strings rather than numbers.
 */
@Serializable
data class OrderTransactionDto(
    val id: String = "",
    val plan_type: String? = null,
    /** The gateway's own id — what the user is shown as the order id. */
    val fp_id: String? = null,
    val fp_payment_id: String? = null,
    val mf_investment_account: String? = null,
    val scheme: String? = null,
    val folio_number: String? = null,
    val amount: String? = null,
    val units: String? = null,
    val systematic: Boolean = false,
    val frequency: String? = null,
    val installment_day: Int? = null,
    val scheduled_on: String? = null,
    val number_of_installments: Int? = null,
    val remaining_installments: Int? = null,
    val state: String? = null,
    val payment_method: String? = null,
    val payment_source: String? = null,
    val switch_to_scheme: String? = null,
    val traded_on: String? = null,
    val submitted_at: String? = null,
    val succeeded_at: String? = null,
    val allotted_units: String? = null,
    val allotted_nav_date: String? = null,
    val purchased_amount: String? = null,
    val purchased_price: String? = null,
    val failed_at: String? = null,
    val reason: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val mf_product: OrderMfProductDto? = null
)

@Serializable
data class OrderMfProductDto(
    val id: String? = null,
    val name: String? = null,
    val isin: String? = null,
    val img_url: String? = null,
    val latest_nav: String? = null,
    val latest_nav_date: String? = null,
    val scheme_plan: OrderSchemePlanDto? = null
)

@Serializable
data class OrderSchemePlanDto(
    val fund_category: String? = null,
    val sub_category: String? = null,
    val plan_type: String? = null,
    val option: String? = null
)

@Serializable
data class OrdersPaginationDto(
    val page: Int = 1,
    val limit: Int = 20,
    val total: Int = 0,
    val total_pages: Int = 1
)
