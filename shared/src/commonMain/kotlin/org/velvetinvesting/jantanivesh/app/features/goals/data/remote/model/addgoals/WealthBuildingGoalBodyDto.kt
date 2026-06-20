package org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.addgoals

import kotlinx.serialization.Serializable

@Serializable
data class WealthBuildingGoalBodyDto(
    val current_goal_cost: Long,
    val current_saved_amount: Long,
    val goal_item_id: Int,
    val goal_item_name: String,
    val goal_name: String,
    val goal_type_id: Int,
    val inflation_rate: Double,
    val return_rate: Double,
    val years_left: Int
)
