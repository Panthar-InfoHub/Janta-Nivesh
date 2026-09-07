package org.velvetinvesting.jantanivesh.app.features.profile.domain.model

import org.velvetinvesting.jantanivesh.app.core.utils.formatWithCommas
import org.velvetinvesting.jantanivesh.app.core.utils.isoUtcToMonthDayYear
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MandateOption

/**
 * One mandate as the listing screen shows it.
 *
 * The list endpoint resolves neither the bank account nor the UMRN for a mandate that is still
 * with the bank, so what is left to show is the provider, the per-debit ceiling, the date the
 * mandate starts and where it currently stands. Amount and date arrive pre-formatted because
 * nothing on the screen needs them as numbers.
 */
data class ActiveMandate(
    val id: String,
    val providerName: String,
    /** Per-debit ceiling, already grouped Indian-style — "1,00,000". */
    val amount: String,
    /** "Aug 07, 2026", or blank when the gateway has not set a start date yet. */
    val startDate: String,
    val status: MandateDisplayStatus
)

/**
 * The gateway reports a mandate's state as free-form text; these are the states the screen knows
 * how to speak about. [UNKNOWN] keeps an unrecognised state visible rather than hiding the mandate.
 */
enum class MandateDisplayStatus {
    ACTIVE, PENDING, FAILED, CANCELLED, UNKNOWN
}

fun MandateOption.toActiveMandate(): ActiveMandate = ActiveMandate(
    id = id,
    providerName = providerName,
    amount = limit?.let { formatWithCommas(it) }.orEmpty(),
    startDate = startDate?.takeIf { it.isNotBlank() }?.isoUtcToMonthDayYear().orEmpty(),
    status = status.toDisplayStatus()
)

private fun String.toDisplayStatus(): MandateDisplayStatus = when (uppercase()) {
    "SUCCESS", "APPROVED", "ACTIVE" -> MandateDisplayStatus.ACTIVE
    "PENDING", "INITIATED", "CREATED", "IN_PROGRESS" -> MandateDisplayStatus.PENDING
    "FAILED", "REJECTED", "EXPIRED" -> MandateDisplayStatus.FAILED
    "CANCELLED", "CANCELED", "REVOKED" -> MandateDisplayStatus.CANCELLED
    else -> MandateDisplayStatus.UNKNOWN
}
