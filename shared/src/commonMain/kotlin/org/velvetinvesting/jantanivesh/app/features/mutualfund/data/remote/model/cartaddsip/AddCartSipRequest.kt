package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.cartaddsip

import kotlinx.serialization.Serializable

@Serializable
data class AddCartSipRequest(
    val amount: Long,
    val mf_product_id: String,
    val sip_st_date: String,
    val sip_en_date: String,
//    val sip_freq: String,
    val sip_day: Int,
    val sip_amt: Long,
    val folio:String = ""
)