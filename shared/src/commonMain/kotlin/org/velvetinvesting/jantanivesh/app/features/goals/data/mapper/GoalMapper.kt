package org.velvetinvesting.jantanivesh.app.features.goals.data.mapper

import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserGoal
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.calculate.GoalCalculationDto
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.config.GoalConfigDto
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalCalculationDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalConfigDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.goalOptionFor

/**
 * The money and rate fields arrive as JSON strings; a value that will not parse is treated as
 * absent rather than as zero, so a malformed projection shows as missing instead of as a goal
 * that needs nothing.
 */
private fun String?.toAmount(): Double? = this?.toDoubleOrNull()

fun UserGoal.toDomain(): GoalDomain = GoalDomain(
    id = id,
    userId = user_id.orEmpty(),
    goalTypeId = goal_type_id,
    goalType = GoalType.fromId(goal_type_id),
    goalName = goal_name.orEmpty(),
    childName = child_name,
    childAge = child_age,
    assetSubtype = asset_subtype,
    yearsRemaining = years_remaining ?: 0,
    targetDate = target_date,
    currentCost = current_cost.toAmount(),
    targetAmount = target_amount.toAmount(),
    currentSavings = current_savings.toAmount() ?: 0.0,
    inflationRate = inflation_rate.toAmount(),
    expectedReturnRate = expected_return_rate.toAmount(),
    futureTargetAmount = future_target_amount.toAmount() ?: 0.0,
    fvCurrentSavings = fv_current_savings.toAmount() ?: 0.0,
    netRequiredCorpus = net_required_corpus.toAmount() ?: 0.0,
    requiredMonthlySip = required_monthly_sip.toAmount() ?: 0.0,
    requiredLumpsumToday = required_lumpsum_today.toAmount() ?: 0.0,
    status = status ?: GoalDomain.ACTIVE,
    calculationVersion = calculation_version,
    createdAt = createdAt,
    updatedAt = updatedAt,
    progressPercent = progress_percent?.toInt() ?: 0
)

/**
 * A goal as a list card. The target is the projected [GoalDomain.futureTargetAmount], falling
 * back to the present-day amount for a goal the server has not sized yet.
 */
fun GoalDomain.toSummary(): GoalsSummaryDomain = GoalsSummaryDomain(
    goalId = id,
    goalTypes = goalOptionFor(goalTypeId, displayName),
    title = displayName,
    amount = currentSavings.toLong(),
    targetAmount = futureTargetAmount.takeIf { it > 0.0 }?.toLong() ?: baseAmount.toLong(),
    progressPercent = progressPercent,
    requiredMonthlySip = requiredMonthlySip,
    yearsRemaining = yearsRemaining
)

fun GoalConfigDto.toDomain(): GoalConfigDomain = GoalConfigDomain(
    id = id,
    goalTypeId = goal_type_id,
    name = name,
    purpose = purpose,
    calculationMode = calculation_mode,
    inflationRate = inflation_rate,
    expectedReturnRate = expected_return_rate,
    minYears = min_years,
    maxYears = max_years,
    tenureSuggestions = tenure_suggestions,
    costChips = cost_chips,
    assetSubtypes = asset_subtypes.orEmpty(),
    configVersion = config_version
)

fun GoalCalculationDto.toDomain(): GoalCalculationDomain = GoalCalculationDomain(
    goalTypeId = goal_type_id,
    yearsRemaining = years_remaining,
    currentCost = current_cost,
    targetAmount = target_amount,
    currentSavings = current_savings,
    inflationRateUsed = inflation_rate_used,
    expectedReturnRateUsed = expected_return_rate_used,
    futureTargetAmount = future_target_amount,
    fvCurrentSavings = fv_current_savings,
    netRequiredCorpus = net_required_corpus,
    requiredMonthlySip = required_monthly_sip,
    requiredLumpsumToday = required_lumpsum_today
)
