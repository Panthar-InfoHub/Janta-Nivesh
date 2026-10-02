package org.velvetinvesting.jantanivesh.app.features.cart.domain.models

/**
 * A cart checkout awaiting its OTP, for either the SIP or the lumpsum side of the cart. The
 * confirm call and the OTP screen both key on [batchId].
 */
data class CartCheckoutDomain(
    val batchId: String,
    val type: CartType,
    /** SIP plans or lumpsum orders in the batch. */
    val itemsCount: Int,
    val totalAmount: Long
)

/** A confirmed lumpsum checkout: the batch is authorised and waits on [paymentUrl] being paid. */
data class LumpsumCheckoutPaymentDomain(
    val paymentUrl: String,
    val paymentId: String
)

/** `GET /payment/{id}`: only [SUCCESS] and [FAILED] are final, anything else is still in flight. */
data class CartPaymentStatusDomain(
    val status: String,
    val failedReason: String?
) {
    val isSuccessful: Boolean
        get() = status.equals(SUCCESS, ignoreCase = true)

    val hasFailed: Boolean
        get() = status.equals(FAILED, ignoreCase = true)

    private companion object {
        const val SUCCESS = "SUCCESS"
        const val FAILED = "FAILED"
    }
}
