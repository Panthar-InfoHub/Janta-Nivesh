package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models

/**
 * Where a redemption is in the gateway's own lifecycle.
 *
 * The flow waits on two of these in turn: [PENDING], which means it is ready to be confirmed with
 * an OTP, and then [SUBMITTED], which means the confirmed request reached the AMC. [FAILED] ends
 * it. Anything else the gateway reports is [OTHER] and means "keep polling".
 */
enum class RedemptionState {
    UNDER_REVIEW,
    PENDING,
    SUBMITTED,
    FAILED,
    OTHER;

    companion object {
        /** The gateway is inconsistent about casing — `PENDING` here, `pending` in raw_response. */
        fun from(raw: String?): RedemptionState = when (raw?.trim()?.uppercase()) {
            "PENDING" -> PENDING
            "SUBMITTED" -> SUBMITTED
            "FAILED" -> FAILED
            "UNDER_REVIEW" -> UNDER_REVIEW
            else -> OTHER
        }
    }
}

/**
 * A redemption request. [id] is the gateway's `fp_id` — the handle every later call in the flow
 * is keyed on, not the database row's own id.
 */
data class MfRedemption(
    val id: String,
    val state: RedemptionState,
    val scheme: String = "",
    val folioNumber: String = "",
    val amount: Double? = null,
    val units: Double? = null,
    /** The gateway's own words when it failed; null otherwise. */
    val failureReason: String? = null
)
