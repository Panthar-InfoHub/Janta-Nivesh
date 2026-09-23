package org.velvetinvesting.jantanivesh.app.features.plans.domain.model

/**
 * Where the money for a purchase has got to, which the gateway reports separately from the
 * purchase's own state: an order can read as submitted while its debit is still in flight.
 */
enum class MfPaymentStatus {
    SUCCESS,
    PENDING,
    FAILED;

    companion object {
        /**
         * Null for a missing status and for anything the gateway adds later — an unrecognised
         * status is not evidence the money moved, so callers treat it exactly like [PENDING].
         */
        fun fromApi(value: String?): MfPaymentStatus? = when {
            value.isNullOrBlank() -> null
            value.equals(SUCCESS.name, ignoreCase = true) -> SUCCESS
            value.equals(PENDING.name, ignoreCase = true) -> PENDING
            value.equals(FAILED.name, ignoreCase = true) -> FAILED
            else -> null
        }
    }
}

/** The payment record attached to a purchase — how it was paid, and how that went. */
data class MfPurchasePayment(
    val id: String?,
    val status: MfPaymentStatus?,
    /** `NETBANKING`, `UPI`, … as the gateway labels it. */
    val paymentType: String?,
    val method: String?,
    val amount: Double?,
    val debitDate: String?,
    val failedReason: String?,
    val paymentReference: String?,
    val providerName: String?
)

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
    val scheduledOn: String?,
    /**
     * The debit's own outcome, which is not implied by [state]: a purchase reads as submitted as
     * soon as the order is placed, whether or not the payment behind it has cleared.
     */
    val paymentStatus: MfPaymentStatus? = null,
    val payment: MfPurchasePayment? = null,
    val units: String? = null,
    val allottedUnits: String? = null,
    val purchasedAmount: String? = null,
    val purchasedPrice: String? = null,
    val mfProductId: String? = null,
    val systematic: Boolean = false
) {
    /** The gateway reviews a freshly created purchase asynchronously; this is that review. */
    val isUnderReview: Boolean
        get() = state.equals(CREATED, ignoreCase = true)

    val isPending: Boolean
        get() = state.equals(PENDING, ignoreCase = true)

    val isReviewCompleted: Boolean
        get() = state.equals(REVIEW_COMPLETED, ignoreCase = true)

    /**
     * Reviewed and waiting for the OTP — the only point at which the confirm endpoints accept a
     * purchase. The gateway reports this as `review_completed`; `pending` is accepted alongside
     * it because the two have been used interchangeably for the lumpsum object.
     */
    val isReadyForOtp: Boolean
        get() = isReviewCompleted || isPending

    /** OTP verified. The purchase is authorised but the money has not moved yet. */
    val isConfirmed: Boolean
        get() = state.equals(CONFIRMED, ignoreCase = true)

    /** Payment went through and the order is with the exchange — the end of this flow. */
    val isSubmitted: Boolean
        get() = state.equals(SUBMITTED, ignoreCase = true) ||
                state.equals(SUCCEEDED, ignoreCase = true) ||
                state.equals(SUCCESSFUL, ignoreCase = true)

    val hasFailed: Boolean
        get() = state.equals(FAILED, ignoreCase = true) ||
                state.equals(CANCELLED, ignoreCase = true) ||
                state.equals(REVERSED, ignoreCase = true)

    val isPaymentSuccessful: Boolean
        get() = paymentStatus == MfPaymentStatus.SUCCESS

    val hasPaymentFailed: Boolean
        get() = paymentStatus == MfPaymentStatus.FAILED

    /**
     * True while the debit is still in flight, which an absent status also counts as: nothing has
     * confirmed the money moved, so it is still worth reading back.
     */
    val isPaymentPending: Boolean
        get() = paymentStatus == null || paymentStatus == MfPaymentStatus.PENDING

    /** What the gateway said went wrong, when it said anything. */
    val paymentFailureReason: String?
        get() = payment?.failedReason?.takeIf { it.isNotBlank() }

    /**
     * The gateway's own vocabulary, which the server echoes upper-cased on the stored record and
     * lower-cased inside `raw_response`. Every comparison above ignores case for that reason.
     */
    companion object {
        const val CREATED = "created"
        const val REVIEW_COMPLETED = "review_completed"
        const val PENDING = "pending"
        const val CONFIRMED = "confirmed"
        const val SUBMITTED = "submitted"

        /** What a completed purchase actually reports; `succeeded` is kept as an alias. */
        const val SUCCESSFUL = "successful"
        const val SUCCEEDED = "succeeded"
        const val FAILED = "failed"
        const val CANCELLED = "cancelled"
        const val REVERSED = "reversed"
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
