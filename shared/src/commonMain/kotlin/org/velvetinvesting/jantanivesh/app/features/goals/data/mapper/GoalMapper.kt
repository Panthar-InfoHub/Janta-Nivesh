package org.velvetinvesting.jantanivesh.app.features.goals.data.mapper

import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalbyid.Data
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalbyid.Scheme
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalSchemeDomain

fun Data.toDomain(): GoalDomain {
    return GoalDomain(
        childAge = child_age,
        childName = child_name,
        createdAt = createdAt,
        currentAge = current_age,
        currentGoalCost = current_goal_cost,
        currentMonthlyExpense = current_monthly_expense,
        currentSavedAmount = current_saved_amount,
        goalId = goal_id,
        goalItemId = goal_item_id,
        goalItemName = goal_item_name,
        goalName = goal_name,
        goalTypeId = goal_type_id,
        id = id,
        inflationRate = inflation_rate,
        lifeExpectancy = life_expectancy,
        postRetirementReturn = post_retirement_return,
        retirementAge = retirement_age,
        returnRate = return_rate,
        updatedAt = updatedAt,
        userId = user_id,
        yearsLeft = years_left,
        schemes = schemes.map { it.toDomain() }
    )
}

fun Scheme.toDomain(): GoalSchemeDomain {
    return GoalSchemeDomain(
        actualFolio = actualfolio,
        balUnits = bal_units,
        currentVal = current_val,
        folio = folio,
        nav = nav,
        schemeId = scheme_id,
        schemeName = scheme_name
    )
}
