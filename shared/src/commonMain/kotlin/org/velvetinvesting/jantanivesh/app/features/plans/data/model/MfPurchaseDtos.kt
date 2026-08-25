package org.velvetinvesting.jantanivesh.app.features.plans.data.model

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MfPurchase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MfPurchaseConfirmation

/** `POST /mf/purchase/` — a one-time buy. A blank folio tells the gateway to open a new one. */
@Serializable
data class CreateMfPurchaseBody(
    val mf_product_id: String,
    val amount: Int,
    val folio_number: String
)

@Serializable
data class CreateMfPurchaseResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: CreatedMfPurchaseDataDto? = null
)

/**
 * The create response is the gateway's own object, not the stored record: it carries `fp_id` and
 * no `state` or `amount`. Everything the UI shows therefore comes from the read-back that
 * follows, which is why almost every field here is optional.
 */
@Serializable
data class CreatedMfPurchaseDataDto(
    val fp_id: String? = null,
    val user_id: String? = null,
    val plan_type: String? = null,
    val systematic: Boolean = false,
    val mf_investment_account: String? = null,
    val fp_payment_id: String? = null,
    val fp_created_at: String? = null,
    val scheduled_on: String? = null
)

fun CreateMfPurchaseResponseDto.toDomain(): MfPurchase? {
    val data = this.data ?: return null
    val id = data.fp_id ?: return null

    return MfPurchase(
        id = id,
        // The create call reports no state at all. Leaving it blank keeps the caller honest:
        // whether the purchase is ready for an OTP is only knowable from the read-back.
        state = "",
        scheme = "",
        folioNumber = null,
        amount = "",
        scheduledOn = data.scheduled_on
    )
}

/** `GET /mf/purchase/{id}` and the verify-otp response — both echo the stored record. */
@Serializable
data class MfPurchaseResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: MfPurchaseDataDto? = null
)

@Serializable
data class MfPurchaseDataDto(
    /** The app's own database key. */
    val id: String? = null,
    /** Gateway id (`mfp_…`), which is what the confirm endpoints accept. */
    val fp_id: String? = null,
    val state: String? = null,
    val scheme: String? = null,
    val folio_number: String? = null,
    val amount: String? = null,
    val systematic: Boolean = false,
    val scheduled_on: String? = null,
    val mf_product_id: String? = null
)

fun MfPurchaseResponseDto.toDomain(): MfPurchase? = data?.toDomain()

fun MfPurchaseDataDto.toDomain(): MfPurchase? {
    // Same rule as the SIP plan: the gateway id is the only one the confirm endpoints accept, so
    // that is what the domain exposes.
    val id = fp_id ?: this.id ?: return null

    return MfPurchase(
        id = id,
        state = state.orEmpty(),
        scheme = scheme.orEmpty(),
        folioNumber = folio_number,
        amount = amount.orEmpty(),
        scheduledOn = scheduled_on
    )
}

/**
 * `POST /mf/purchase/{id}/confirm/verify-otp`.
 *
 * Unlike the SIP confirmation, this returns a payment link: authorising the purchase does not
 * pay for it, so the response carries the gateway page that does, alongside the purchase record.
 */
@Serializable
data class ConfirmMfPurchaseResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: ConfirmMfPurchaseDataDto? = null
)

@Serializable
data class ConfirmMfPurchaseDataDto(
    val payment_id: String? = null,
    val payment_url: String? = null,
    val purchase: MfPurchaseDataDto? = null
)

fun ConfirmMfPurchaseResponseDto.toDomain(): MfPurchaseConfirmation? {
    val data = this.data ?: return null
    val purchase = data.purchase?.toDomain() ?: return null

    return MfPurchaseConfirmation(
        purchase = purchase,
        paymentId = data.payment_id,
        paymentUrl = data.payment_url
    )
}
