package org.velvetinvesting.jantanivesh.app.features.core.data.local.mapper

import org.velvetinvesting.jantanivesh.app.core.theme.*
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.DashboardDto
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.OnboardingStatusDto
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserDataDto
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserGoal
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalOption
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.DashboardSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.OnboardingStatusDomain
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.UserDataDomain

fun UserDataDto.toDomain(): UserDataDomain = UserDataDomain(
    id = data.id,
    name = data.full_name.orEmpty(),
    email = data.email.orEmpty(),
    mobile = data.phone_no.orEmpty(),
    dob = data.dob.orEmpty(),
    city = data.city,
    goals = data.user_goals.map { it.toGoalSummary() },
    onboarding = data.onboarding?.toDomain() ?: OnboardingStatusDomain.EMPTY,
    isSkipped = data.is_skip,
    dashboard = data.dashboard?.toDomain() ?: DashboardSummaryDomain(),
    investorProfile = data.investor_profile,
    investmentAccount = data.investment_account,
    userFinance = data.user_finance
)

fun OnboardingStatusDto.toDomain(): OnboardingStatusDomain = OnboardingStatusDomain(
    currentStage = current_stage.orEmpty(),
    basicDetailsStatus = basic_details_status.orEmpty(),
    readinessStatus = readiness_status.orEmpty(),
    kycStatus = kyc_status.orEmpty(),
    pennyDropStatus = penny_drop_status.orEmpty(),
    emailStatus = email_status.orEmpty(),
    profileStatus = profile_status.orEmpty(),
    nomineeStatus = nominee_status.orEmpty(),
    isCompleted = is_completed,
    completedAt = completed_at
)

fun DashboardDto.toDomain(): DashboardSummaryDomain = DashboardSummaryDomain(
    portfolioValue = portfolio_value,
    mutualFunds = mutual_funds,
    fixedDeposits = fixed_deposits,
    totalReturns = total_returns,
    returnPercent = return_percent,
    monthChangePercent = month_change_percent
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
            type = GoalType.ChildEducation,
            color = MutualFundIconBg
        )

        2 -> GoalOption(
            title = "Child Marriage",
            type = GoalType.ChildMarriage,
            color = bgColor3
        )

        3 -> GoalOption(
            title = "Retirement",
            type = GoalType.Retirement,
            color = bgColor4
        )

        4 -> GoalOption(
            title = goal_name?:"Wealth Building",
            type = GoalType.WealthBuilding,
            goalItemId = goal_item_id,
            goalItemName = goal_item_name,
            color = Secondary
        )

        else -> GoalOption(
            title = "Unknown",
            type = GoalType.WealthBuilding,
            color = Primary
        )
    }
}
