package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models

data class MutualFundPurchaseInitiateDomain(
    val mandateId:String,
    val url: String,
    val status: MandateStatus
)

enum class MandateStatus{
    PENDING,APPROVED
}