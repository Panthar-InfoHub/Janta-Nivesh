package org.velvetinvesting.jantanivesh.app.features.goals.domain.models

sealed interface GoalRequest {

    val goalTypeId: Int
    val inflationRate: Int
    val returnRate: Int
    val currentSavedAmount: Long
    val yearsToGoal:Int
    val title: String

    // Child related goals
    data class ChildEducation(
        override val goalTypeId: Int = 1,
        val childName: String,
        val childAge: Int,
        override val yearsToGoal: Int,
        val currentGoalCost: Long,
        override val inflationRate: Int,
        override val returnRate: Int,
        override val currentSavedAmount: Long,
        override val title: String
    ) : GoalRequest

    data class ChildMarriage(
        override val goalTypeId: Int = 2,
        val childName: String,
        val childAge: Int,
        override val yearsToGoal: Int,
        val currentGoalCost: Long,
        override val inflationRate: Int,
        override val returnRate: Int,
        override val currentSavedAmount: Long,
        override val title: String
    ) : GoalRequest


    // Retirement goal
    data class Retirement(
        override val goalTypeId: Int = 3,
        val currentAge: Int,
        val retirementAge: Int,
        val lifeExpectancy: Int,
        val currentMonthlyExpense: Long,
        val postRetirementReturn: Int,
        override val inflationRate: Int,
        override val returnRate: Int,
        override val currentSavedAmount: Long,
        override val yearsToGoal: Int,
        override val title: String
    ) : GoalRequest


    // Item goal
    data class WealthBuildingGoal(
        override val goalTypeId: Int = 4,
        val goalName: String,
        val goalItemId: Int,
        val goalItemName: String,
        override val yearsToGoal: Int,
        val currentGoalCost: Long,
        override val inflationRate: Int,
        override val returnRate: Int,
        override val currentSavedAmount: Long,
        override val title: String
    ) : GoalRequest
}
