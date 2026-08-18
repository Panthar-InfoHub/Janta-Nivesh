package org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model

/**
 * Flattened view of `GET /onboarding/kyc-form/status`. The KYC form is a single server-side
 * resource that walks through several states, so the flow is driven by re-reading this after every
 * step (DigiLocker web view, signature upload, eSign web view) rather than by local bookkeeping.
 */
data class KycFormStatus(
    val kycFormId: String?,
    val status: String?,
    val fieldsNeeded: List<String>,
    val proofStatus: String?,
    val proofFetchUrl: String?,
    val signatureProvided: Boolean,
    val esignStatus: String?,
    val esignUrl: String?,
    val currentStage: String?,
    val isOnboardingCompleted: Boolean
) {
    /** Identity documents still have to be pulled from DigiLocker via [proofFetchUrl]. */
    val isProofPending: Boolean
        get() = proofStatus == PROOF_PENDING && !proofFetchUrl.isNullOrBlank()

    val isProofFetched: Boolean
        get() = proofStatus == PROOF_FETCHED

    /** The server has not generated an eSign request yet — it is waiting on the signature image. */
    val isSignatureNeeded: Boolean
        get() = esignStatus.isNullOrBlank()

    /** An eSign document exists and the user still has to sign it at [esignUrl]. */
    val isESignPending: Boolean
        get() = esignStatus == ESIGN_PENDING && !esignUrl.isNullOrBlank()

    val isESignSuccessful: Boolean
        get() = esignStatus == ESIGN_SUCCESSFUL

    companion object {
        const val PROOF_PENDING = "pending"
        const val PROOF_FETCHED = "fetched"
        const val ESIGN_PENDING = "pending"
        const val ESIGN_SUCCESSFUL = "successful"
    }
}
