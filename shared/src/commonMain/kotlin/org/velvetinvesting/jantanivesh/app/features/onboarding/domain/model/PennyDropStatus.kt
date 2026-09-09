package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model

/**
 * Where the bank stands on the account submitted for a penny drop. The verification is
 * asynchronous, so the caller polls this until [isVerified] or [isFailed] answers; anything else —
 * including an account the server has not started on — means keep waiting.
 */
data class PennyDropStatus(
    val status: String?,
    val reason: String?
) {
    val isVerified: Boolean
        get() = status.equals(VERIFIED, ignoreCase = true)

    val isFailed: Boolean
        get() = FAILED_STATUSES.any { status.equals(it, ignoreCase = true) }

    private companion object {
        const val VERIFIED = "verified"
        val FAILED_STATUSES = listOf("failed", "rejected", "error")
    }
}
