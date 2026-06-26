package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models

enum class FDStatus(
    val label: String
) {

    INITIATED(
        label = "Initiated"
    ),

    ONBOARDING_COMPLETED(
        label = "Onboarding Completed"
    ),

    ONBOARDING_FAILED(
        label = "Onboarding Failed"
    ),

    PAYMENT_PENDING(
        label = "Payment Pending"
    ),

    PAYMENT_SUCCESS(
        label = "Payment Successful"
    ),

    PAYMENT_FAILED(
        label = "Payment Failed"
    ),

    VKYC_PENDING(
        label = "VKYC Pending"
    ),

    VKYC_COMPLETED(
        label = "VKYC Completed"
    ),

    VKYC_FAILED(
        label = "VKYC Failed"
    ),

    FD_CREATED(
        label = "FD Created"
    ),

    REFUNDED(
        label = "Refunded"
    ),

    MATURED(
        label = "Matured"
    ),

    PREMATURE_WITHDRAWN(
        label = "Premature Withdrawn"
    );

    companion object {

        fun fromValue(value: String?): FDStatus {
            return entries.firstOrNull {
                it.name.equals(value, ignoreCase = true)
            } ?: INITIATED
        }
    }
}