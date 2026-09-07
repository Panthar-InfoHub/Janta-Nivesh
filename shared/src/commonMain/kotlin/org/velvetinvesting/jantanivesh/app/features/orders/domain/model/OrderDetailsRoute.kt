package org.velvetinvesting.jantanivesh.app.features.orders.domain.model

import org.velvetinvesting.jantanivesh.app.core.navigation.Route

/**
 * Packs a listed order into the details route.
 *
 * The two directions are kept next to each other on purpose: the route flattens nulls to zero
 * because navigation arguments cannot carry a nullable primitive, and [toOrderDomain] is what
 * unflattens them again, so the details screen sees the same absent-versus-zero distinction the
 * list did.
 */
fun OrderDomain.toDetailsRoute(): Route.OrderDetails = Route.OrderDetails(
    planType = planType.name,
    state = state.name,
    stateLabel = stateLabel,
    systematic = systematic,
    fundName = fundName,
    fundIconUrl = fundIconUrl,
    fundCategory = fundCategory,
    mfProductId = mfProductId,
    isin = isin,
    folioNumber = folioNumber,
    orderId = orderId,
    transactionId = transactionId,
    amount = amount ?: 0.0,
    units = units ?: 0.0,
    allottedUnits = allottedUnits ?: 0.0,
    purchasedAmount = purchasedAmount ?: 0.0,
    purchasedPrice = purchasedPrice ?: 0.0,
    latestNav = latestNav ?: 0.0,
    paymentMethod = paymentMethod,
    frequency = frequency,
    reason = reason,
    createdAt = createdAt,
    submittedAt = submittedAt,
    succeededAt = succeededAt,
    failedAt = failedAt,
    allottedNavDate = allottedNavDate
)

/** Rebuilds the order the details screen renders. Fields the listing does not carry stay empty. */
fun Route.OrderDetails.toOrderDomain(): OrderDomain = OrderDomain(
    id = orderId,
    planType = runCatching { OrderPlanType.valueOf(planType) }.getOrDefault(OrderPlanType.UNKNOWN),
    state = runCatching { OrderState.valueOf(state) }.getOrDefault(OrderState.UNKNOWN),
    stateLabel = stateLabel,
    fundName = fundName,
    fundIconUrl = fundIconUrl,
    fundCategory = fundCategory,
    mfProductId = mfProductId,
    isin = isin,
    folioNumber = folioNumber,
    orderId = orderId,
    transactionId = transactionId,
    amount = amount.takeIf { it > 0.0 },
    units = units.takeIf { it > 0.0 },
    systematic = systematic,
    frequency = frequency,
    installmentDay = null,
    numberOfInstallments = null,
    remainingInstallments = null,
    allottedUnits = allottedUnits.takeIf { it > 0.0 },
    allottedNavDate = allottedNavDate,
    purchasedAmount = purchasedAmount.takeIf { it > 0.0 },
    purchasedPrice = purchasedPrice.takeIf { it > 0.0 },
    latestNav = latestNav.takeIf { it > 0.0 },
    paymentMethod = paymentMethod,
    paymentSource = "",
    switchToScheme = "",
    reason = reason,
    scheduledOn = "",
    tradedOn = "",
    createdAt = createdAt,
    submittedAt = submittedAt,
    succeededAt = succeededAt,
    failedAt = failedAt
)
