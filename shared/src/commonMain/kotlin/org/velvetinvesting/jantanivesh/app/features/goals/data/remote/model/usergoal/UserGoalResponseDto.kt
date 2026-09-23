package org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.usergoal

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserGoal

/** `GET /user-goal/` */
@Serializable
data class UserGoalsListDto(
    val success: Boolean = false,
    val message: String = "",
    val `data`: List<UserGoal> = emptyList()
)

/** `GET /user-goal/{id}`, and the echo of a successful `POST /user-goal/`. */
@Serializable
data class UserGoalDto(
    val success: Boolean = false,
    val message: String = "",
    val `data`: UserGoal
)

/**
 * One holding mapped to a goal, as `GET /user-goal/{id}` returns it under `holdings`.
 *
 * [id] is the holding's own id — the value `/user-goal/map` and `/user-goal/remove` are keyed by.
 * Money, units and NAV arrive as JSON strings (Postgres numerics) and are parsed in the mapper.
 */
@Serializable
data class UserGoalHoldingDto(
    val id: String,
    val user_id: String? = null,
    val mf_investment_account: String? = null,
    val folio_number: String? = null,
    val isin: String? = null,
    val fund_name: String? = null,
    val mf_product_id: String? = null,
    val user_goal_id: String? = null,
    val units: String? = null,
    val redeemable_units: String? = null,
    val nav: String? = null,
    val nav_as_on: String? = null,
    val invested_amount: String? = null,
    val current_value: String? = null,
    val unrealized_gain: String? = null,
    val absolute_return: String? = null,
    val avg_nav: String? = null,
    val xirr: String? = null,
    val synced_at: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val mf_product: UserGoalMfProductDto? = null
)

/** The scheme behind a holding: the name and logo worth showing, rather than the folio's own. */
@Serializable
data class UserGoalMfProductDto(
    val id: String? = null,
    val name: String? = null,
    val isin: String? = null,
    val img_url: String? = null,
    val latest_nav: String? = null,
    val latest_nav_date: String? = null
)
