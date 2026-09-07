package org.velvetinvesting.jantanivesh.app.features.profile.domain.model

/**
 * One row of the transaction history.
 *
 * The rows are assembled from the portfolio endpoints, and those do not all report the same
 * facts: a pending order is a dated event with a state, a settled holding is a position with
 * neither. [date] and [status] are therefore allowed to be absent rather than invented, and the
 * row simply drops the parts it cannot fill.
 */
data class TransactionHistoryItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    /** Blank when the source carries no date — a holding is a position, not a dated event. */
    val date: String,
    /** Null when the source reports no state at all, which is the case for a mutual-fund holding. */
    val status: TransactionStatus?,
    /** What the source actually called the state, e.g. "Payment Pending". Blank with [status]. */
    val statusLabel: String,
    val type: TransactionType,
    /** Scheme or issuer logo; blank falls back to initials drawn from [title]. */
    val iconUrl: String
)

enum class TransactionStatus {
    SUCCESSFUL, PENDING, FAILED
}

enum class TransactionType {
    MUTUAL_FUND, FIXED_DEPOSIT
}

/**
 * A titled block of rows. The portfolio data has no single date axis to sort every row on, so the
 * header names the kind of row underneath it — "PENDING ORDERS", "HOLDINGS" — rather than a day.
 */
data class TransactionGroup(
    val header: String,
    val transactions: List<TransactionHistoryItem>
)
