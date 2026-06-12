package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FullRedemptionRequestDto(
    @SerialName("source")
    val source: String= "transaction",

    @SerialName("scheme_id")
    val schemeId: Int,

    @SerialName("folio_no")
    val folioNo: String,

    @SerialName("redem_type")
    val redemptionType: String = "FULL",
)

@Serializable
data class PartialRedemptionRequestDto(
    @SerialName("source")
    val source: String = "transaction",

    @SerialName("scheme_id")
    val schemeId: Int,

    @SerialName("folio_no")
    val folioNo: String,

    @SerialName("redem_type")
    val redemptionType: String = "PARTIAL",

    @SerialName("redemption_units")
    val redemptionUnits: Double?,

    @SerialName("redemption_amount")
    val redemptionAmount: Int?,
)