package org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalmapping

import kotlinx.serialization.Serializable

/**
 * `POST /user-goal/map`. Both endpoints are keyed by the holding's id, and both accept a single
 * `holding_id` or a `holding_ids` list — mapping is done from a multi-select sheet, so it sends
 * the list; removal is per row, so it sends the one.
 */
@Serializable
data class GoalMapBodyDto(
    val goal_id: String,
    val holding_ids: List<String>
)

/** `POST /user-goal/remove`. */
@Serializable
data class GoalUnMapBodyDto(
    val goal_id: String,
    val holding_id: String
)
