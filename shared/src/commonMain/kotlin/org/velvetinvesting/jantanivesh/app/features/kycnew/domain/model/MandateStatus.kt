package org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model

/**
 * Result of reading a mandate back after the user has been through the authorization page.
 *
 * Two different fields report progress: [status] is the transport-level outcome of the lookup,
 * while [mandateStatus] is the bank's verdict — only the latter says whether autopay is live.
 */
data class MandateStatus(
    val mandateId: String?,
    val status: String?,
    val mandateStatus: String?,
    val umrn: String?,
    val approvedAt: String?,
    val rejectedReason: String?
) {
    val isApproved: Boolean
        get() = mandateStatus.equals(APPROVED, ignoreCase = true)

    companion object {
        const val APPROVED = "APPROVED"
    }
}
