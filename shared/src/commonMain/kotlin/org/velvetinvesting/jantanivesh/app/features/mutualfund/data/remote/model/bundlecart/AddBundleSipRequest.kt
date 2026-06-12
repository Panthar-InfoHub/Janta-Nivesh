package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundlecart
import kotlinx.serialization.Serializable
@Serializable
data class AddBundleSipRequest(
    val type: String = "SIP",
    val bundle_id: String,
    val amount: Long,
    val sip_st_date: String,
    val sip_en_date: String,
    val sip_freq: String,
    val sip_day: Int,
    val sip_amt: Long
)
