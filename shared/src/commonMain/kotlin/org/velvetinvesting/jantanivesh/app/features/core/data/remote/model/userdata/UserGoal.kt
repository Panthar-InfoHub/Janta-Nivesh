package org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata

import kotlinx.serialization.Serializable

/**
 * One row of `GET /user-goal/` — and the same shape `GET /user/` embeds under `user_goals` and
 * `GET /user-goal/{id}` returns as its `data`.
 *
 * The `v2.0` goal engine sizes every goal server-side: the client sends the inputs (cost or
 * target, years, savings) and the server returns the projection alongside them, so the
 * `future_*`/`net_*`/`required_*` figures below are read, never recomputed here.
 *
 * Money and rates come back as JSON strings (Postgres numerics), which is why they are typed as
 * `String?` and parsed in the mapper rather than declared as numbers.
 */
@Serializable
data class UserGoal(
    val id: String,
    val user_id: String? = null,
    val goal_type_id: Int,

    /** Absent for the child goals, which the server titles from `child_name` instead. */
    val goal_name: String? = null,
    val child_name: String? = null,
    val child_age: Int? = null,
    /** `BIKE` / `SCOOTER` / `CAR`, for the vehicle goal only. */
    val asset_subtype: String? = null,

    val years_remaining: Int? = null,
    val target_date: String? = null,

    /** Present-day cost, for every type except "Build My Savings". */
    val current_cost: String? = null,
    /** The corpus asked for directly, for "Build My Savings". */
    val target_amount: String? = null,
    val current_savings: String? = null,

    val inflation_rate: String? = null,
    val expected_return_rate: String? = null,

    /** Projection, as calculated by the server. */
    val future_target_amount: String? = null,
    val fv_current_savings: String? = null,
    val net_required_corpus: String? = null,
    val required_monthly_sip: String? = null,
    val required_lumpsum_today: String? = null,

    val status: String? = null,
    val calculation_version: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val progress_percent: Double? = null
)
