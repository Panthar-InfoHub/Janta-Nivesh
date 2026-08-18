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
        get() = state == CONFIRMED

    /**
     * The gateway reviews a freshly created plan asynchronously. [CREATED] is the only state that
     * means "still working"; anything else — review completed, failed, or a later lifecycle state
     * such as `active` — is terminal and stops the caller waiting.
     */
    val isUnderReview: Boolean
        get() = state == CREATED

    val hasFailed: Boolean
        get() = state == FAILED

    companion object {
        const val CREATED = "created"
        const val REVIEW_COMPLETED = "review_completed"
        const val FAILED = "failed"
        const val CONFIRMED = "confirmed"
    }
}
