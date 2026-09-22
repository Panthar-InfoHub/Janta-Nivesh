package org.velvetinvesting.jantanivesh.app.features.core.data.local.mapper

import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.DashboardDto
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.OnboardingStatusDto
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserDataDto
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserGoal
import org.velvetinvesting.jantanivesh.app.features.goals.data.mapper.toDomain
import org.velvetinvesting.jantanivesh.app.features.goals.data.mapper.toSummary
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
    userFinance = data.user_finance,
    mpinIsSetup = data.mpin_is_setup,
    mpinEnabled = data.mpin_enabled
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

/**
 * `GET /user/` embeds the same goal rows `GET /user-goal/` returns, so the home screen's cards
 * come from the one goal mapper rather than from a second, drifting copy of the same arithmetic.
 */
fun UserGoal.toGoalSummary(): GoalsSummaryDomain = toDomain().toSummary()
