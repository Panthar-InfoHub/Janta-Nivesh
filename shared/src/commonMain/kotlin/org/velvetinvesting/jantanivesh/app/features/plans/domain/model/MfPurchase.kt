package org.velvetinvesting.jantanivesh.app.features.plans.domain.model

/**
 * A one-time (lumpsum) purchase, as it moves through the gateway.
 *
 * The lifecycle the app drives is `PENDING → CONFIRMED → SUBMITTED`: created and awaiting the
 * OTP, then authorised and awaiting payment, then paid. The SIP equivalent is [PurchasePlan],
 * which stops at confirmation because a mandate carries the debit instead.
 */
data class MfPurchase(
    /** Gateway id (`mfp_…`) — every follow-up call is keyed on this, not on the database id. */
    val id: String,
    val state: String,
    /** ISIN of the scheme; the readable name comes from the scheme lookup. */
    val scheme: String,
    val folioNumber: String?,
    val amount: String,
    val scheduledOn: String?
) {
    /** Reviewed and waiting for the OTP. This is the state the confirm endpoints accept. */
    val isPending: Boolean
        get() = state.equals(PENDING, ignoreCase = true)

    /** OTP verified. The purchase is authorised but the money has not moved yet. */
    val isConfirmed: Boolean
        get() = state.equals(CONFIRMED, ignoreCase = true)

    /** Payment went through and the order is with the exchange — the end of this flow. */
    val isSubmitted: Boolean
        get() = state.equals(SUBMITTED, ignoreCase = true) ||
                state.equals(SUCCEEDED, ignoreCase = true)

    val hasFailed: Boolean
        get() = state.equals(FAILED, ignoreCase = true) ||
                state.equals(CANCELLED, ignoreCase = true)

    companion object {
        const val PENDING = "PENDING"
        const val CONFIRMED = "CONFIRMED"
        const val SUBMITTED = "SUBMITTED"
        const val SUCCEEDED = "SUCCEEDED"
        const val FAILED = "FAILED"
        const val CANCELLED = "CANCELLED"
    }
}

/**
 * What `confirm/verify-otp` hands back. The purchase is authorised at this point but not paid —
 * [paymentUrl] is the gateway page that actually takes the money, and the flow is not finished
 * until the purchase reads back as [MfPurchase.SUBMITTED] afterwards.
 */
data class MfPurchaseConfirmation(
    val purchase: MfPurchase,
    val paymentId: String?,
    val paymentUrl: String?
) {
    val hasPaymentUrl: Boolean
        get() = !paymentUrl.isNullOrBlank()
}
