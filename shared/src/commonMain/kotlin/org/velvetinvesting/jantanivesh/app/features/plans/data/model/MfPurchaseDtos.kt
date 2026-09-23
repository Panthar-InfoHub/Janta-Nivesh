package org.velvetinvesting.jantanivesh.app.features.plans.data.model

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MfPaymentStatus
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MfPurchase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MfPurchaseConfirmation
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MfPurchasePayment

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
    val user_id: String? = null,
    val plan_type: String? = null,
    /** Gateway id (`mfp_…`), which is what the confirm endpoints accept. */
    val fp_id: String? = null,
    val fp_old_id: Int? = null,
    val fp_payment_id: String? = null,
    /**
     * How the debit went: `SUCCESS`, `PENDING` or `FAILED`, and absent until the gateway has
     * something to report. It moves independently of [state], so both are read.
     */
    val payment_status: String? = null,
    val mf_investment_account: String? = null,
    val mf_product_id: String? = null,
    val scheme: String? = null,
    val folio_number: String? = null,
    val amount: String? = null,
    val units: String? = null,
    val systematic: Boolean = false,
    val frequency: String? = null,
    val installment_day: Int? = null,
    val scheduled_on: String? = null,
    val number_of_installments: Int? = null,
    val remaining_installments: Int? = null,
    val requested_activation_date: String? = null,
    val start_date: String? = null,
    val end_date: String? = null,
    val next_installment_date: String? = null,
    val previous_installment_date: String? = null,
    val state: String? = null,
    val auto_generate_installments: Boolean? = null,
    val generate_first_installment_now: Boolean? = null,
    val payment_method: String? = null,
    val payment_source: String? = null,
    val purpose: String? = null,
    val switch_to_scheme: String? = null,
    val source_ref_id: String? = null,
    val partner: String? = null,
    val gateway: String? = null,
    val euin: String? = null,
    val user_ip: String? = null,
    val server_ip: String? = null,
    val initiated_by: String? = null,
    val initiated_via: String? = null,
    val consent_email: String? = null,
    val consent_isd_code: String? = null,
    val consent_mobile: String? = null,
    val consent_given_at: String? = null,
    val traded_on: String? = null,
    val submitted_at: String? = null,
    val succeeded_at: String? = null,
    val allotted_units: String? = null,
    val allotted_nav_date: String? = null,
    val purchased_amount: String? = null,
    val purchased_price: String? = null,
    val fp_created_at: String? = null,
    val activated_at: String? = null,
    val cancelled_at: String? = null,
    val cancellation_scheduled_on: String? = null,
    val cancellation_code: String? = null,
    val auto_cancelled: Boolean? = null,
    val failed_at: String? = null,
    val completed_at: String? = null,
    val reason: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    /** The debit itself. Absent until a payment has been attempted. */
    val payment: MfPurchasePaymentDto? = null
)

/** The `payment` block of a purchase read-back — the debit and everything the gateway knows of it. */
@Serializable
data class MfPurchasePaymentDto(
    val id: Int? = null,
    val status: String? = null,
    val payment_type: String? = null,
    val method: String? = null,
    val amount: Double? = null,
    val debit_date: String? = null,
    val amc_order_ids: List<Int>? = null,
    val failed_reason: String? = null,
    val created_at: String? = null,
    val submitted_at: String? = null,
    val debit_confirmed_at: String? = null,
    val failed_at: String? = null,
    val transfer_initiated_at: String? = null,
    val settled_at: String? = null,
    val rejected_at: String? = null,
    val from_bank_account_id: Int? = null,
    val mandate_id: String? = null,
    val provider_name: String? = null,
    val failure_code: String? = null,
    val late_auth: Boolean? = null,
    val refund_reference: String? = null,
    val refund_reason: String? = null,
    val refund_status: String? = null,
    val refund_created_at: String? = null,
    val token_url: String? = null,
    val payment_reference: String? = null
)

fun MfPurchasePaymentDto.toDomain(): MfPurchasePayment = MfPurchasePayment(
    id = id?.toString(),
    status = MfPaymentStatus.fromApi(status),
    paymentType = payment_type,
    method = method,
    amount = amount,
    debitDate = debit_date,
    failedReason = failed_reason,
    paymentReference = payment_reference,
    providerName = provider_name
)

fun MfPurchaseResponseDto.toDomain(): MfPurchase? = data?.toDomain()

fun MfPurchaseDataDto.toDomain(): MfPurchase? {
    // Same rule as the SIP plan: the gateway id is the only one the confirm endpoints accept, so
    // that is what the domain exposes.
    val id = fp_id ?: this.id ?: return null

    val paymentDetails = payment?.toDomain()

    return MfPurchase(
        id = id,
        state = state.orEmpty(),
        scheme = scheme.orEmpty(),
        folioNumber = folio_number,
        amount = amount.orEmpty(),
        scheduledOn = scheduled_on,
        // The top-level status is the record's own; the nested payment block stands in when the
        // gateway has only filled that one in.
        paymentStatus = MfPaymentStatus.fromApi(payment_status) ?: paymentDetails?.status,
        payment = paymentDetails,
        units = units,
        allottedUnits = allotted_units,
        purchasedAmount = purchased_amount,
        purchasedPrice = purchased_price,
        mfProductId = mf_product_id,
        systematic = systematic
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
