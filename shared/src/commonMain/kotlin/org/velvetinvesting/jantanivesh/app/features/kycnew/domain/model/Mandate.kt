package org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model

/**
 * A UPI autopay mandate awaiting authorization. [tokenUrl] is the payment-gateway page the user
 * has to complete; [id] is what the confirmation call is keyed on afterwards.
 */
data class Mandate(
    val id: Int?,
    val tokenUrl: String?,
    val status: String?
)
