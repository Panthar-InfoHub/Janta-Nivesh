package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.fdportfoliobyid

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val amount: String,
    val createdAt: String,
    val failure_reason: String?,
    val fd_account_number: String?,
    val fd_issued_at: String?,
    val id: String,
    val is_vkyc_pending: Boolean,
    val maturity_amount: String?,
    val maturity_date: String?,
    val maturity_instruction: String?,
    val onboarded_at: String,
    val payment_completed_at: String?,
    val payment_tx_id: String?,
    val payout_frequency: String,
    val pending_action: String,
    val product: Product,
    val refund_date: String?,
    val roi_at_booking: String,
    val status: String,
    val tenure_at_booking: Int,
    val updatedAt: String,
    val user_id: String,
    val vkyc_completed_at: String?,
    val vkyc_failure_reason: String?
)