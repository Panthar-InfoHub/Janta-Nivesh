package org.velvetinvesting.jantanivesh.app.features.goals.domain.models

import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType

/**
 * One row of `GET /user-goal/config`: what a goal type is for, the rates it is sized with, and
 * the tenures and costs to suggest while the user fills the form in.
 */
data class GoalConfigDomain(
    val id: String,
    val goalTypeId: Int,
    val name: String,
    val purpose: String,
    val calculationMode: String,
    /** Fractions (0.06 == 6%). Inflation is null where the target is a fixed corpus. */
    val inflationRate: Double?,
    val expectedReturnRate: Double?,
    val minYears: Int,
    val maxYears: Int,
    val tenureSuggestions: List<Int>,
    val costChips: List<Long>,
    val assetSubtypes: List<String>,
    val configVersion: String
) {
    val type: GoalType? = GoalType.fromId(goalTypeId)
}

fun GoalConfigDomain.toOption(): GoalOption = GoalOption(
    title = name,
    color = goalTypeColor(goalTypeId),
    goalTypeId = goalTypeId,
    type = type,
    purpose = purpose,
    calculationMode = calculationMode,
    inflationRate = inflationRate,
    expectedReturnRate = expectedReturnRate,
    minYears = minYears,
    maxYears = maxYears,
    tenureSuggestions = tenureSuggestions,
    costChips = costChips,
    assetSubtypes = assetSubtypes,
    configId = id,
    configVersion = configVersion
)
