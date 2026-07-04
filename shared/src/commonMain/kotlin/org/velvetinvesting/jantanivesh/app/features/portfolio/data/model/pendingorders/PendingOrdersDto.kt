package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.pendingorders

import kotlinx.serialization.Serializable

@Serializable
data class PendingOrdersDto(
    val code: Int? = null,
    val message: String? = null,
    val data: PendingOrdersDataDto? = null
)

@Serializable
data class PendingOrdersDataDto(
    val total_count: Int? = null,
    val pending_orders: List<PendingOrderDto>? = null
)

@Serializable
data class PendingOrderDto(
    val id: String? = null,
    val type: String? = null,
    val scheme_name: String? = null,
    val amount: Double? = null,
    val date: String? = null,
    val status: String? = null,
    val status_remark: String? = null,
    val amc: String? = null,
    val frequency: String? = null,
    val start_date: String? = null,
    val raw_data: RawDataDto? = null,
    val img_url: String? = null,
)

@Serializable
data class RawDataDto(
    val status: String? = null,
    val member_code: String? = null,
    val client_code: String? = null,
    val client_name: String? = null,
    val pg_bank_ref_no: String? = null,
    val xsip_registration_no: String? = null,
    val xsip_registration_date: String? = null,
    val amc_name: String? = null,
    val rta_scheme_code: String? = null,
    val scheme_name: String? = null,
    val frequency_type: String? = null,
    val start_date: String? = null,
    val end_date: String? = null,
    val installments_amount: String? = null,
    val brokerage: String? = null,
    val entry_by: String? = null,
    val nse_mandate_id: String? = null,
    val dpc_flag: String? = null,
    val dp_trans: String? = null,
    val sub_broker: String? = null,
    val euin_no: String? = null,
    val euin_declaration: String? = null,
    val first_order_today: String? = null,
    val folio_number: String? = null,
    val remarks: String? = null,
    val sub_broker_arn: String? = null,
    val no_of_installments: String? = null,
    val exchange_remark: String? = null,
    val health_declaration_flag: String? = null,
    val nominee_dob: String? = null,
    val disclaimer_flag: String? = null,
    val internal_ref_no: String? = null,
    val primary_holder_email: String? = null,
    val primary_holder_mobile: String? = null,
    val second_holder_email: String? = null,
    val second_holder_mobile: String? = null,
    val third_holder_email: String? = null,
    val third_holder_mobile: String? = null,
    val member_unique_id: String? = null,
)