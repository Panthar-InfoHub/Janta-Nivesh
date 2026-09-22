package org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalmapping

import kotlinx.serialization.Serializable

/**
 * Scheme mapping still posts to `/user-goal/map` and `/user-goal/map-remove`. Those endpoints
 * were not re-specified with the `v2.0` goal engine, so the id sent here is the goal's UUID —
 * every other `/user-goal` route is UUID-keyed, and the integer `goal_id` the old responses
 * carried is no longer returned by `GET /user-goal/{id}`.
 */
@Serializable
data class GoalMapBodyDto(
    val goal_id: String,
    val map_data: List<MapData>
)

@Serializable
data class MapData(
    val folio: String,
    val scheme_id: String
)

@Serializable
data class UnMapGoalRequestDto(
    val goal_id: String
)
