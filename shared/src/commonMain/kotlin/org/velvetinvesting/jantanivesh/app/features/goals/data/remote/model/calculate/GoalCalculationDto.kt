package org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.calculate

import kotlinx.serialization.Serializable

/** `POST /user-goal/calculate` — the projection for inputs the user has not committed to yet. */
@Serializable
data class GoalCalculationResponseDto(
    val success: Boolean = false,
    val message: String = "",
    val `data`: GoalCalculationDto
)

@Serializable
data class GoalCalculationDto(
    val goal_type_id: Int,
    val years_remaining: Int = 0,
    val current_cost: Double? = null,
    val target_amount: Double? = null,
    val current_savings: Double = 0.0,
    /** The rates actually applied — the type's defaults unless the request overrode them. */
    val inflation_rate_used: Double? = null,
    val expected_return_rate_used: Double? = null,
    val future_target_amount: Double = 0.0,
    val fv_current_savings: Double = 0.0,
    val net_required_corpus: Double = 0.0,
    val required_monthly_sip: Double = 0.0,
    val required_lumpsum_today: Double = 0.0
)
