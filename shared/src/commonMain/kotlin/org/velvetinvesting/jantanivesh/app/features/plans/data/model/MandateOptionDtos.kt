package org.velvetinvesting.jantanivesh.app.features.plans.data.model

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MandateOption

/** `GET /mandate/` — every mandate registered for the user, whatever state it is in. */
@Serializable
data class MandateListResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: MandateListDataDto? = null
)

@Serializable
data class MandateListDataDto(
    val mandates: List<MandateSummaryDto> = emptyList()
)

/**
 * `bank_account` is deliberately not declared: it comes back null on every mandate seen so far,
 * so its shape is unknown and declaring it wrongly would fail the whole response. The shared Json
 * config ignores unknown keys, so it is simply skipped until the shape is confirmed.
 */
@Serializable
data class MandateSummaryDto(
    /** The app's own database key. */
    val id: String? = null,
    /** The gateway's mandate id — the one any select or change call would be keyed on. */
    val mandate_id: String? = null,
    /** Per-debit ceiling, sent as a string. */
    val amount: String? = null,
    val status: String? = null,
    val mandate_type: String? = null,
    val provider_name: String? = null,
    val failure_reason: String? = null,
    val umrn: String? = null,
    val start_date: String? = null,
    val end_date: String? = null
)

fun MandateListResponseDto.toDomain(): List<MandateOption> =
    data?.mandates.orEmpty().mapNotNull { it.toDomain() }

fun MandateSummaryDto.toDomain(): MandateOption? {
    val id = mandate_id ?: this.id ?: return null

    return MandateOption(
        id = id,
        mandateType = mandate_type.orEmpty(),
        providerName = provider_name.orEmpty(),
        status = status.orEmpty(),
        // The ceiling arrives as a string and may carry decimals; only whole rupees are shown.
        limit = amount?.toDoubleOrNull()?.toLong(),
        umrn = umrn,
        startDate = start_date
    )
}
