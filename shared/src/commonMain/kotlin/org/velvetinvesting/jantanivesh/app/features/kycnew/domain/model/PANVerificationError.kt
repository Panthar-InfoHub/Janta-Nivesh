package org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model

data class PANVerificationError(
    val code: Int,
    val message: String,
    val type: String
) {
    /**
     * The only error type the app can act on: the user has no usable KYC and must be sent
     * through the KYC initiate flow. Every other type is just surfaced as a message.
     */
    val isNewKycRequired: Boolean
        get() = type == KYC_NEW_REQUIRED

    companion object {
        const val KYC_NEW_REQUIRED = "READINESS_KYC_NEW_REQUIRED"
        const val UNKNOWN = "UNKNOWN"
    }
}
data class KYCError(
    val code: Int,
    val message: String,
    val type: String
) {
    /**
     * The only error type the app can act on: the user has no usable KYC and must be sent
     * through the KYC initiate flow. Every other type is just surfaced as a message.
     */
    val isNewKycRequired: Boolean
        get() = type == KYC_FORM_NOT_FOUND

    companion object {
        const val  KYC_FORM_NOT_FOUND = "KYC_FORM_NOT_FOUND"
        const val UNKNOWN = "UNKNOWN"
    }
}
