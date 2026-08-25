package org.velvetinvesting.jantanivesh.app.features.plans.domain.model

/**
 * A SIP registration. The gateway echoes a large payload back; only the fields the app acts on
 * or displays are carried through.
 */
data class PurchasePlan(
    val id: String,
    val state: String,
    /** ISIN of the scheme — the readable name has to be resolved from the scheme list. */
    val scheme: String,
    val folioNumber: String?,
    val amount: String,
    val frequency: String,
    val installmentDay: Int?,
    val numberOfInstallments: Int?,
    val remainingInstallments: Int?,
    val startDate: String?
) {
    val isConfirmed: Boolean
        get() = state.equals(CONFIRMED, ignoreCase = true)

    /**
     * The gateway reviews a freshly created plan asynchronously. [CREATED] means the review is
     * still running.
     */
    val isUnderReview: Boolean
        get() = state.equals(CREATED, ignoreCase = true)

    /**
     * Review finished and the plan is ready to be confirmed. This is the only state the OTP
     * endpoints accept, so it is what the submission waits for rather than merely "not created".
     */
    val isReviewCompleted: Boolean
        get() = state.equals(REVIEW_COMPLETED, ignoreCase = true)

    val hasFailed: Boolean
        get() = state.equals(FAILED, ignoreCase = true) ||
                state.equals(CANCELLED, ignoreCase = true)

    companion object {
        const val CREATED = "created"
        const val REVIEW_COMPLETED = "review_completed"
        const val FAILED = "failed"
        const val CANCELLED = "cancelled"
        const val CONFIRMED = "confirmed"
    }
}
