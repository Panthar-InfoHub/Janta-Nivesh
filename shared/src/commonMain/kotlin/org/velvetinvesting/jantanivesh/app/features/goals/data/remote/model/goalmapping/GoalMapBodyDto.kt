package org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalmapping

import kotlinx.serialization.Serializable

@Serializable
data class GoalMapBodyDto(
    val goal_id: Int,
    val map_data: List<MapData>
)

@Serializable
data class MapData(
    val folio: String,
    val scheme_id: String
)

@Serializable
data class UnMapGoalRequestDto(
    val goal_id: Int
)
