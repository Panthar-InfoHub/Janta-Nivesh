package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models

data class UserCartDomain(
    val sipItems: List<SipItemDomain>,
    val lumpSumItems: List<LumpSumItemDomain>
)

data class SipItemDomain(
    val id: String,
    val amcName: String,
    val productName: String,
    val amount: Long,
    val type: CartType,
    val date: String,
    val sipDetails: SipDetails,
    val imageUrl: String,
    val inv_id: String,
    val prodCode: String,
    val folio: String? = "",
    val stepUpRequired: Boolean = false,
    val stepUpAmount: Long = 0,
    val minStepUpAmount: Long,
    val amcCode: String,
    val minStepUpPercent: Double
)

data class LumpSumItemDomain(
    val id: String,
    val amcName: String,
    val productName: String,
    val amount: Long,
    val type: CartType,
    val date: String,
    val imageUrl: String,
    val inv_id: String,
    val prodCode: String,
    val folio: String,
    val amcCode: String,
)

enum class CartType {
    SIP,
    LUMPSUM
}

data class SipDetails(
    val startDate: String,
    val endDate: String,
    val frequency: String,
    val day: Int,
    val sipAmount: Long
)