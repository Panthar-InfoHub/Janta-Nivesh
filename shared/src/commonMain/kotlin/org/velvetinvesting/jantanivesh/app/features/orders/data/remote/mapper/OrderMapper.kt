package org.velvetinvesting.jantanivesh.app.features.orders.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PaginatedData
import org.velvetinvesting.jantanivesh.app.features.orders.data.remote.model.OrderTransactionDto
import org.velvetinvesting.jantanivesh.app.features.orders.data.remote.model.UserOrdersDto
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderDomain
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderPlanType
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderState

fun UserOrdersDto.toPaginatedDomain(): PaginatedData<OrderDomain> {
    val page = data?.pagination?.page ?: 1
    val totalPages = data?.pagination?.total_pages ?: 1

    return PaginatedData(
        items = data?.transactions.orEmpty().map { it.toDomain() },
        page = page,
        pageSize = data?.pagination?.limit ?: 20,
        totalItems = data?.pagination?.total ?: data?.transactions?.size ?: 0,
        totalPages = totalPages,
        hasNextPage = page < totalPages
    )
}

fun OrderTransactionDto.toDomain(): OrderDomain = OrderDomain(
    id = id,
    planType = plan_type.toPlanType(),
    state = state.toOrderState(),
    stateLabel = state.orEmpty(),
    fundName = mf_product?.name.orEmpty(),
    fundIconUrl = mf_product?.img_url.orEmpty(),
    // The sub-category is the useful half — "Debt" says more than "DEBT" does — and the plan
    // option rides along with it the way it does on the fund screens.
    fundCategory = listOfNotNull(
        mf_product?.scheme_plan?.sub_category?.takeIf { it.isNotBlank() },
        mf_product?.scheme_plan?.option?.takeIf { it.isNotBlank() }?.replaceFirstChar { it.uppercase() }
    ).joinToString(" • "),
    mfProductId = mf_product?.id.orEmpty(),
    isin = mf_product?.isin ?: scheme.orEmpty(),
    folioNumber = folio_number.orEmpty(),
    orderId = fp_id.orEmpty(),
    transactionId = fp_payment_id.orEmpty(),
    amount = amount?.toDoubleOrNull(),
    units = units?.toDoubleOrNull(),
    systematic = systematic,
    frequency = frequency.orEmpty(),
    installmentDay = installment_day,
    numberOfInstallments = number_of_installments,
    remainingInstallments = remaining_installments,
    allottedUnits = allotted_units?.toDoubleOrNull(),
    allottedNavDate = allotted_nav_date.orEmpty(),
    purchasedAmount = purchased_amount?.toDoubleOrNull(),
    purchasedPrice = purchased_price?.toDoubleOrNull(),
    latestNav = mf_product?.latest_nav?.toDoubleOrNull(),
    paymentMethod = payment_method.orEmpty(),
    paymentSource = payment_source.orEmpty(),
    switchToScheme = switch_to_scheme.orEmpty(),
    reason = reason.orEmpty(),
    scheduledOn = scheduled_on.orEmpty(),
    tradedOn = traded_on.orEmpty(),
    createdAt = createdAt.orEmpty(),
    submittedAt = submitted_at.orEmpty(),
    succeededAt = succeeded_at.orEmpty(),
    failedAt = failed_at.orEmpty()
)

private fun String?.toPlanType(): OrderPlanType = when (this?.uppercase()) {
    "PURCHASE" -> OrderPlanType.PURCHASE
    "REDEMPTION" -> OrderPlanType.REDEMPTION
    "SWITCH" -> OrderPlanType.SWITCH
    else -> OrderPlanType.UNKNOWN
}

/**
 * SUBMITTED and ACTIVE are not outcomes the user is waiting on in the same way, but they are
 * both "placed, not settled" as far as this screen is concerned — a registered SIP counts as
 * done, an order still with the gateway does not.
 */
private fun String?.toOrderState(): OrderState = when (this?.uppercase()) {
    "SUCCESSFUL", "SUCCESS", "COMPLETED", "ACTIVE" -> OrderState.SUCCESSFUL
    "PENDING", "SUBMITTED", "CREATED", "INITIATED", "PROCESSING" -> OrderState.PENDING
    "FAILED", "REJECTED", "EXPIRED" -> OrderState.FAILED
    "CANCELLED", "CANCELED", "REVOKED" -> OrderState.CANCELLED
    else -> OrderState.UNKNOWN
}
