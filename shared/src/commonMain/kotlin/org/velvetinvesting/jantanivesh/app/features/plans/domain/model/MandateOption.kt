package org.velvetinvesting.jantanivesh.app.features.plans.domain.model

/**
 * One autopay mandate the user can debit a SIP against.
 *
 * A mandate is bank-backed, but the list endpoint does not resolve the bank account, so what
 * identifies a mandate here is its type, its provider and its per-debit ceiling. The purchase is
 * attached to the mandate, not to the account behind it.
 */
data class MandateOption(
    /** The gateway's mandate id, which is what any future select/change call would key on. */
    val id: String,
    val mandateType: String,
    val providerName: String,
    val status: String,
    /** Per-debit ceiling in rupees, as the gateway reports it. */
    val limit: Long?,
    val umrn: String?,
    val startDate: String?
) {
    /**
     * Only an approved mandate can carry a debit; everything else is still with the bank. The
     * purchase screen shows approved mandates and nothing else.
     */
    val isApproved: Boolean
        get() = status.equals(APPROVED, ignoreCase = true)

    /** "UPI Autopay", plus a UMRN tail when the user holds more than one. */
    val displayName: String
        get() {
            val type = mandateType.takeIf { it.isNotBlank() } ?: "Autopay"
            val tail = umrn?.takeIf { it.isNotBlank() }?.takeLast(UMRN_TAIL_LENGTH)
            return if (tail != null) "$type Autopay ••$tail" else "$type Autopay"
        }

    companion object {
        const val APPROVED = "SUCCESS"

        private const val UMRN_TAIL_LENGTH = 4
    }
}
