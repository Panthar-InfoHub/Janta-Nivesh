package org.velvetinvesting.jantanivesh.app.features.orders.domain.model

/**
 * One order as the app speaks about it.
 *
 * `GET /user/orders` returns purchases, redemptions and switches through one row shape, and
 * fills only the fields that kind of order has. That is kept rather than flattened: the list
 * row and the details screen both branch on [planType] and [state], so the model stays honest
 * about what is absent — a redemption placed in units has no [amount], a pending purchase has
 * no [allottedUnits] — instead of substituting zero for "not known yet".
 */
data class OrderDomain(
    val id: String,
    val planType: OrderPlanType,
    val state: OrderState,
    /** What the server actually called the state, e.g. "SUBMITTED". */
    val stateLabel: String,
    val fundName: String,
    val fundIconUrl: String,
    val fundCategory: String,
    /** The `mf_product` id the purchase endpoints take — what a retry needs. */
    val mfProductId: String,
    val isin: String,
    val folioNumber: String,
    /** The gateway id shown to the user as the order id. */
    val orderId: String,
    val transactionId: String,
    /** Rupees, when the order was placed by amount. Null for a unit-based redemption. */
    val amount: Double?,
    /** Units, when the order was placed by units. Null for an amount-based order. */
    val units: Double?,
    val systematic: Boolean,
    val frequency: String,
    val installmentDay: Int?,
    val numberOfInstallments: Int?,
    val remainingInstallments: Int?,
    val allottedUnits: Double?,
    val allottedNavDate: String,
    val purchasedAmount: Double?,
    /** The NAV the order was allotted at. Null until the AMC reports it. */
    val purchasedPrice: Double?,
    /** The scheme's current NAV, which stands in for the allotment NAV before allotment. */
    val latestNav: Double?,
    val paymentMethod: String,
    val paymentSource: String,
    val switchToScheme: String,
    val reason: String,
    val scheduledOn: String,
    val tradedOn: String,
    val createdAt: String,
    val submittedAt: String,
    val succeededAt: String,
    val failedAt: String
)

enum class OrderPlanType { PURCHASE, REDEMPTION, SWITCH, UNKNOWN }

/**
 * The four outcomes the screens speak about. The gateway reports a longer vocabulary —
 * SUBMITTED, ACTIVE, CREATED — which collapses onto these; [UNKNOWN] keeps a state nobody
 * recognises visible rather than hiding the order.
 */
enum class OrderState { PENDING, SUCCESSFUL, FAILED, CANCELLED, UNKNOWN }

/** The chips above the list. [ALL] is the default, so an unrecognised state is still reachable. */
enum class OrderFilter { ALL, PENDING, COMPLETE, FAILED }

fun OrderFilter.title(): String = when (this) {
    OrderFilter.ALL -> "All"
    OrderFilter.PENDING -> "Pending"
    OrderFilter.COMPLETE -> "Complete"
    OrderFilter.FAILED -> "Failed"
}

fun OrderDomain.matches(filter: OrderFilter): Boolean = when (filter) {
    OrderFilter.ALL -> true
    OrderFilter.PENDING -> state == OrderState.PENDING
    OrderFilter.COMPLETE -> state == OrderState.SUCCESSFUL
    OrderFilter.FAILED -> state == OrderState.FAILED || state == OrderState.CANCELLED
}

/** "Lumpsum", "SIP", "Redemption" — how the row names the kind of order underneath the fund. */
fun OrderDomain.typeLabel(): String = when (planType) {
    OrderPlanType.PURCHASE -> if (systematic) "SIP" else "Lumpsum"
    OrderPlanType.REDEMPTION -> if (systematic) "SWP" else "Redemption"
    OrderPlanType.SWITCH -> "Switch"
    OrderPlanType.UNKNOWN -> stateLabel.ifBlank { "Order" }
}

/**
 * What the order was placed for: the amount when there is one, and the units when the order was
 * placed in units instead — a full redemption is stated one way and a partial one the other.
 */
fun OrderDomain.placedForLabel(): String = when {
    amount != null -> "₹${amount.asMoney()}"
    units != null -> "${units.asUnits()} units"
    else -> "--"
}

/** The units column of the list row: allotted units once known, otherwise the units asked for. */
fun OrderDomain.unitsLabel(): String {
    val settled = allottedUnits ?: units.takeIf { state != OrderState.PENDING }
    return settled?.asUnits() ?: "--"
}
