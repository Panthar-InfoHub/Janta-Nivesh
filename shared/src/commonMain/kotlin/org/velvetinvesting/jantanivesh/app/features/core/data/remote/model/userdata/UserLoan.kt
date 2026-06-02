package org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata

import kotlinx.serialization.Serializable

@Serializable
data class UserLoan(
    val createdAt: String,
    val id: String,
    val loan_name: String,
    val loan_type: String,
    val monthly_emi: String,
    val outstanding_amount: String,
    val tenure_months: Int,
    val updatedAt: String,
    val user_id: String
)