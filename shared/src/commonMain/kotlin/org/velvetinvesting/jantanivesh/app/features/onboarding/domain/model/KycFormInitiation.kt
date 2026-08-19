package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model

/**
 * Result of `POST /onboarding/kyc-form`. The call is idempotent — it creates the form on the first
 * hit and simply reports the existing one afterwards — so the response already says where the form
 * stands, including the DigiLocker [proofFetchUrl] when documents still have to be pulled.
 */
data class KycFormInitiation(
    val kycFormId: String?,
    val status: String?,
    /** How the form was raised, e.g. "fresh" for a first-time filer. */
    val type: String?,
    val proofFetchUrl: String?,
    val fieldsNeeded: List<String>,
    val onboarding: OnboardingStatus?
) {
    /** Identity documents still have to be pulled from DigiLocker via [proofFetchUrl]. */
    val isProofPending: Boolean
        get() = !proofFetchUrl.isNullOrBlank()

    val isUnderReview: Boolean
        get() = status == STATUS_UNDER_REVIEW

    companion object {
        const val STATUS_UNDER_REVIEW = "under_review"
    }
}
