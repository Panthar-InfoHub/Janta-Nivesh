package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.initiatemfpurchase.body

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val amc_code: String,
    val folio: String,
    val prod_code: String,
    val sip_amt: Long,
    val sip_en_date: String,
    val sip_freq: String,
    val sip_st_date: String,
    val step_up_amount: String,
    val step_up_end_date: String,
    val step_up_required: String,
    val step_up_start_date: String
)