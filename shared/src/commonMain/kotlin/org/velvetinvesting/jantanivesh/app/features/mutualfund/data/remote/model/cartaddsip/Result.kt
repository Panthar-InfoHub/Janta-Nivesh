package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.cartaddsip

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val adddate: String,
    val all_units: String,
    val amc_code: String,
    val amc_name: String,
    val bank_ac_no: String,
    val bank_name: String,
    val bse_order_no: String,
    val folio: String,
    val id: String,
    val inv_id: String,
    val inv_name: String,
    val joint_name1: String,
    val joint_name2: String,
    val nse_order_no: String,
    val nse_order_status: String,
    val prod_code: String,
    val prod_name: String,
    val pur_type: String,
    val reinv_flag: String,
    val sip_amt: String,
    val sip_day: String,
    val sip_en_date: String,
    val sip_freq: String,
    val sip_st_date: String,
    val src: String,
    val src_reinv_flag: String,
    val sub_txn_type: String,
    val tgt_prod_code: String,
    val tgt_prod_name: String,
    val tgt_reinv_flag: String,
    val txn_amount: String,
    val txn_units: String
)