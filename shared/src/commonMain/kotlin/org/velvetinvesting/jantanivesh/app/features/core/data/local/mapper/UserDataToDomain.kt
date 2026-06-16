package org.velvetinvesting.jantanivesh.app.features.core.data.local.mapper

import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.Data
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserDataDto
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserGoal
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.GoalOption
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.UserDataDomain

fun UserDataDto.toDomain(): UserDataDomain = UserDataDomain(
    name = this.data.full_name,
    email = this.data.email,
    mobile = this.data.phone_no,
    goals = this.data.user_goals?.map { it.toGoalSummary() }?: emptyList(),
    kycVerified = data.toKycCompletion(),
    tradingAccountVerified = data.toTradingCompletion()
)

fun UserGoal.toGoalSummary(): GoalsSummaryDomain {
    return GoalsSummaryDomain(
        goalTypes = mapGoalOption(),
        amount = current_saved_amount.toLong(),
        targetAmount = current_goal_cost?.toLong()?:0L,
        goalId = id
    )
}

fun UserGoal.mapGoalOption(): GoalOption {
    return when (goal_type_id) {

        1 -> GoalOption(
            title = "Child Education",
            type = GoalType.ChildEducation
        )

        2 -> GoalOption(
            title = "Child Marriage",
            type = GoalType.ChildMarriage
        )

        3 -> GoalOption(
            title = "Retirement",
            type = GoalType.Retirement
        )

        4 -> GoalOption(
            title = goal_name?:"Wealth Building",
            type = GoalType.WealthBuilding,
            goalItemId = goal_item_id,
            goalItemName = goal_item_name
        )

        else -> GoalOption(
            title = "Unknown",
            type = GoalType.WealthBuilding
        )
    }
}

fun Data.toKycCompletion(): Boolean {


    val isCompleted = this.kyc_types.mf?.status
        ?.lowercase()
        ?.contains("verified") == true

    return isCompleted
}

fun Data.toTradingCompletion(): Boolean {
    val isCompleted = this.kyc_types.trading?.status
        ?.lowercase()
        ?.contains("verified") == true
    return isCompleted
}