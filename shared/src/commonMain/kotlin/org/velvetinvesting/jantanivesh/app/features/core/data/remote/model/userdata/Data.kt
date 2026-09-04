package org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata

import kotlinx.serialization.Serializable

/**
 * `GET /user/`.
 *
 * Almost everything is optional: the profile fills in as the user moves through onboarding, and
 * the finance, assets and insurance blocks come back null until those sections are completed.
 * The hashes and the refresh token are echoed by the server but are not modelled here — the app
 * has no use for them, and unknown keys are ignored by the shared Json config.
 */
@Serializable
data class Data(
    val id: String,
    val full_name: String? = null,
    val email: String? = null,
    val phone_no: String? = null,
    val city: String? = null,
    val dob: String? = null,
    val fcm_token: String? = null,
    val meta_data: MetaData? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,

    /** Gateway identifiers created once KYC clears; null before that. */
    val investor_profile: String? = null,
    val investment_account: String? = null,
    val investment_account_old_id: Int? = null,

    val mpin_is_setup: Boolean = false,
    val mpin_enabled: Boolean = false,

    val user_finance: UserFinance? = null,
    val user_assets: UserAssets? = null,
    val user_insurance: UserInsurance? = null,
    val user_loan: List<UserLoan> = emptyList(),
    val user_goals: List<UserGoal> = emptyList(),

    val onboarding: OnboardingStatusDto? = null,
    /** True when the user chose to skip onboarding and go straight into the app. */
    val is_skip: Boolean = false,
    val dashboard: DashboardDto? = null
)

/** Per-step onboarding progress. Each status is `PENDING` / `VERIFIED` / `SKIPPED`. */
@Serializable
data class OnboardingStatusDto(
    val current_stage: String? = null,
    val basic_details_status: String? = null,
    val readiness_status: String? = null,
    val kyc_status: String? = null,
    val penny_drop_status: String? = null,
    val email_status: String? = null,
    val profile_status: String? = null,
    val nominee_status: String? = null,
    val is_completed: Boolean = false,
    val completed_at: String? = null
)

/** The headline figures the home screen's portfolio card shows. */
@Serializable
data class DashboardDto(
    val portfolio_value: Double = 0.0,
    val mutual_funds: Double = 0.0,
    val fixed_deposits: Double = 0.0,
    val total_returns: Double = 0.0,
    val return_percent: Double = 0.0,
    /** Null until there is a previous month to compare against. */
    val month_change_percent: Double? = null
)
