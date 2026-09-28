package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model

/**
 * Bank details the server already holds from a reverse penny drop, used to fill the bank form.
 * Anything the server left out reads as an empty string.
 */
data class PrefilledBankDetails(
    val accountNumber: String,
    val ifscCode: String,
    val accountHolderName: String,
    val accountType: String,
    val bankName: String
)

/** The UPI apps the reverse penny drop hands out a dedicated payment link for. */
enum class UpiApp {
    GOOGLE_PAY,
    PHONEPE,
    PAYTM,
    CRED,
    BHIM
}

/**
 * Where to pay the ₹1 verification. [validationLink] is the generic page, which shows a QR code to
 * scan from any UPI app; [appLinks] open that same page aimed straight at one app.
 */
data class ReversePennyDropLinks(
    val validationLink: String?,
    val appLinks: Map<UpiApp, String>
)

/**
 * Where the ₹1 payment stands. Anything that is neither [isSuccess] nor [isPending] — including a
 * status the server has not named before — is treated as failed, so the user can start over
 * rather than wait on it forever. A missing status is still pending: the payment may not have
 * been seen yet.
 */
data class ReversePennyDropStatus(
    val status: String?,
    val description: String?
) {
    val isSuccess: Boolean
        get() = status.equals(SUCCESS, ignoreCase = true)

    val isPending: Boolean
        get() = status.isNullOrBlank() || status.equals(PENDING, ignoreCase = true)

    val isFailed: Boolean
        get() = !isSuccess && !isPending

    private companion object {
        const val SUCCESS = "SUCCESS"
        const val PENDING = "PENDING"
    }
}
