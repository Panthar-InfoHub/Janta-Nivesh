package org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.addgoals

import kotlinx.serialization.Serializable

@Serializable
data class ChildGoalBodyDto(
    val child_age: Int,
    val child_name: String,
    val current_goal_cost: Long,
    val current_saved_amount: Long,
    val goal_type_id: Int,
    val inflation_rate: Double,
    val return_rate: Int,
    val years_left: Int
)
