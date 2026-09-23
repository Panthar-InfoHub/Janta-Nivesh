package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model

/**
 * A UPI autopay mandate awaiting authorization. [tokenUrl] is the payment-gateway page the user
 * has to complete; [id] is what the confirmation call is keyed on afterwards.
 */
data class Mandate(
    val id: Int?,
    /**
     * The server's own record id for the mandate (`data.id`, a cuid). The purchase endpoints key
     * on this one, while [id] is the gateway's numeric id the status lookup is keyed on.
     */
    val recordId: String?,
    val tokenUrl: String?,
    val status: String?
)
