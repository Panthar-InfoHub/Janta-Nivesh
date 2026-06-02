package org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val city: String?,
    val createdAt: String,
    val dob: String,
    val email: String,
    val fcm_token: String?,
    val full_name: String,
    val id: String,
    val inv_id: Int?,
    val kyc_progress: Int,
    val kyc_types: KycType,
    val meta_data: MetaData,
    val nse_client_code: String?,
    val phone_no: String,
    val refresh_token: String,
    val updatedAt: String,
    val user_assets: UserAssets?,
    val user_finance: UserFinance?,
    val user_goals: List<UserGoal>?,
    val user_home_data: UserHomeData?,
    val user_insurance: UserInsurance?,
    val user_loan: List<UserLoan>?,
    val usr: String?
)