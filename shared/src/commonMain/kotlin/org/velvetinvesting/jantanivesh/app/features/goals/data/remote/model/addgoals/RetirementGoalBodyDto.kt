package org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.addgoals

import kotlinx.serialization.Serializable

@Serializable
data class RetirementGoalBodyDto(
    val current_age: Int,
    val current_monthly_expense: Long,
    val current_saved_amount: Long,
    val goal_type_id: Int,
    val inflation_rate: Double,
    val life_expectancy: Int,
    val post_retirement_return: Double,
    val retirement_age: Int,
    val return_rate: Double
)
