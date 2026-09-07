package org.velvetinvesting.jantanivesh.app.features.portfolio.data.mapper

import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.redemption.CreateRedemptionResponseDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.redemption.RedemptionStatusResponseDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MfRedemption
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.RedemptionState

/**
 * Null when the response carried no `fp_id`: without it there is nothing to poll or confirm, so
 * the caller has to treat that as a failed create rather than an empty success.
 */
fun CreateRedemptionResponseDto.toDomain(): MfRedemption? {
    val id = data?.fp_id?.takeIf { it.isNotBlank() } ?: return null

    return MfRedemption(
        id = id,
        state = RedemptionState.from(data.state),
        scheme = data.scheme.orEmpty(),
        folioNumber = data.folio_number.orEmpty(),
        // Both arrive as decimal strings, and only the one the request used is populated.
        amount = data.amount?.toDoubleOrNull(),
        units = data.units?.toDoubleOrNull()
    )
}

fun RedemptionStatusResponseDto.toDomain(): MfRedemption? {
    val id = data?.fp_id?.takeIf { it.isNotBlank() } ?: return null

    return MfRedemption(
        id = id,
        state = RedemptionState.from(data.state),
        scheme = data.scheme.orEmpty(),
        folioNumber = data.folio_number.orEmpty(),
        amount = data.amount?.toDoubleOrNull(),
        units = data.units?.toDoubleOrNull(),
        failureReason = data.reason?.takeIf { it.isNotBlank() }
    )
}
