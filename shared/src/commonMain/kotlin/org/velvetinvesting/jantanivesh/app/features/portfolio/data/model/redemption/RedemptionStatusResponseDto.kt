package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.redemption

import kotlinx.serialization.Serializable

/**
 * `GET /mf/redemption/{id}`, polled until the gateway settles on a state.
 *
 * The payload carries the full gateway record; only the fields the flow acts on are declared —
 * everything else is ignored by the lenient decoder rather than modelled and left unused.
 */
@Serializable
data class RedemptionStatusResponseDto(
    val success: Boolean = false,
    val message: String = "",
    val `data`: RedemptionStatusDataDto? = null
)

@Serializable
data class RedemptionStatusDataDto(
    val id: String? = null,
    val fp_id: String? = null,
    val scheme: String? = null,
    val folio_number: String? = null,
    val amount: String? = null,
    val units: String? = null,
    val state: String? = null,
    /** Populated by the gateway when the redemption fails; shown to the user as-is. */
    val reason: String? = null,
    val failed_at: String? = null,
    val fp_created_at: String? = null
)
