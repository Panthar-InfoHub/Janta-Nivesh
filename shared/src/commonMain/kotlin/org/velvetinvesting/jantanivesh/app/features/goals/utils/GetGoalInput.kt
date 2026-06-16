package org.velvetinvesting.jantanivesh.app.features.goals.utils

import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalRequest

fun GoalRequest.getGoalInputs(): Triple<Long, Int, Int> {
    return when (this) {
        is GoalRequest.ChildEducation -> Triple(currentGoalCost, inflationRate, yearsToGoal)
        is GoalRequest.ChildMarriage -> Triple(currentGoalCost, inflationRate, yearsToGoal)
        is GoalRequest.WealthBuildingGoal -> Triple(currentGoalCost, inflationRate, yearsToGoal)

        is GoalRequest.Retirement -> {
            val years = yearsToGoal
            val yearlyExpense = currentMonthlyExpense * 12
            Triple(yearlyExpense, inflationRate, years)
        }
    }
}
