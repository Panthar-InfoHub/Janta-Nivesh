package org.velvetinvesting.jantanivesh.app.features.profile.domain.model

import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.isoUtcToDisplayDate
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FDStatus
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MutualFundPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PendingOrderDomain

/**
 * Maps what the portfolio use cases return onto the history rows.
 *
 * Nothing here invents a fact the payload does not carry: a holding gets no status and no date,
 * because `GET /portfolio` reports neither for one.
 */

/** A placed order — the only mutual-fund record that is genuinely a dated, stateful event. */
fun PendingOrderDomain.toTransactionItem(): TransactionHistoryItem {
    val orderType = type.takeIf { it.isNotBlank() }?.lowercase()?.replaceFirstChar { it.uppercase() }

    return TransactionHistoryItem(
        id = "order_$id",
        title = schemeName,
        subtitle = listOfNotNull(orderType, amc.takeIf { it.isNotBlank() }).joinToString(" • "),
        amount = amount.toRupees(),
        // Already formatted upstream for SIPs; anything still in ISO form is normalised here.
        date = date.isoUtcToDisplayDate(),
        status = status.toTransactionStatus(),
        statusLabel = status.ifBlank { "Pending" },
        type = TransactionType.MUTUAL_FUND,
        iconUrl = icon
    )
}

/**
 * A settled holding. It is shown for the invested amount only — current value and returns belong
 * to the portfolio screen, and the payload carries no order date or state to put on the row.
 */
fun MutualFundPortfolioDomain.toTransactionItem(): TransactionHistoryItem = TransactionHistoryItem(
    id = "mf_$id",
    title = title,
    subtitle = listOfNotNull(
        category.takeIf { it.isNotBlank() },
        folio.takeIf { it.isNotBlank() }?.let { "Folio $it" }
    ).joinToString(" • "),
    amount = amount.toRupees(),
    date = "",
    status = null,
    statusLabel = "",
    type = TransactionType.MUTUAL_FUND,
    iconUrl = icon
)

fun FixedDepositPortfolioDomain.toTransactionItem(): TransactionHistoryItem {
    val fdStatus = FDStatus.fromValue(status)

    return TransactionHistoryItem(
        id = "fd_$id",
        title = issuerDisplayName,
        subtitle = listOfNotNull(
            roiAtBooking.takeIf { it.isNotBlank() }?.let { "$it% p.a." },
            tenureAtBooking.takeIf { it > 0 }?.let { "$it days" }
        ).joinToString(" • "),
        amount = amount.toDoubleOrNull().toRupees(),
        date = fdIssuedAt?.isoUtcToDisplayDate().orEmpty(),
        status = status.toTransactionStatus(),
        statusLabel = fdStatus.label,
        type = TransactionType.FIXED_DEPOSIT,
        iconUrl = issuerLogoUrl
    )
}

/**
 * Both sources report state as free-form text, so the row's colour is decided on what the text
 * says rather than on a fixed vocabulary. Anything unrecognised reads as still in flight, which
 * is the safe way to be wrong about a payment.
 */
private fun String.toTransactionStatus(): TransactionStatus {
    val value = uppercase()
    return when {
        listOf("FAIL", "REJECT", "CANCEL", "REFUND").any { value.contains(it) } ->
            TransactionStatus.FAILED

        listOf("SUCCESS", "COMPLETE", "CREATED", "ACTIVE", "MATURED", "WITHDRAWN", "CONFIRM")
            .any { value.contains(it) } -> TransactionStatus.SUCCESSFUL

        else -> TransactionStatus.PENDING
    }
}

private fun Double?.toRupees(): String = "₹${formatMoneyAfterL(this?.toLong() ?: 0L)}"
