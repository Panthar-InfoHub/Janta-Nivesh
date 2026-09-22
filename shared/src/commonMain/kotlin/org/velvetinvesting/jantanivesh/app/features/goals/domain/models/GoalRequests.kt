package org.velvetinvesting.jantanivesh.app.features.goals.domain.models

import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType

/**
 * The inputs a goal is created from. One shape covers all six types: which fields the server
 * requires is a function of [goalType], and [org.velvetinvesting.jantanivesh.app.features.goals.data.mapper.toCreateBody]
 * sends only the ones that type uses.
 */
data class CreateGoalRequest(
    val goalType: GoalType,
    val goalName: String? = null,
    val childName: String? = null,
    val childAge: Int? = null,
    val assetSubtype: String? = null,
    val yearsRemaining: Int,
    /** Present-day cost, for types 1, 2, 3, 4 and 6. */
    val currentCost: Double? = null,
    /** The corpus asked for, for type 5. */
    val targetAmount: Double? = null,
    val currentSavings: Double = 0.0,
    /** Fractions (0.06 == 6%). Null leaves the type's configured default in place. */
    val inflationRate: Double? = null,
    val expectedReturnRate: Double? = null
)

/**
 * The inputs a projection is asked for. The same fields as [CreateGoalRequest] minus everything
 * that only labels the goal — the server sizes a goal from its money and its horizon alone.
 */
data class GoalCalculationRequest(
    val goalTypeId: Int,
    val yearsRemaining: Int,
    val currentCost: Double? = null,
    val targetAmount: Double? = null,
    val currentSavings: Double = 0.0,
    val inflationRate: Double? = null,
    val expectedReturnRate: Double? = null
)

fun CreateGoalRequest.toCalculationRequest(): GoalCalculationRequest = GoalCalculationRequest(
    goalTypeId = goalType.id,
    yearsRemaining = yearsRemaining,
    currentCost = currentCost,
    targetAmount = targetAmount,
    currentSavings = currentSavings,
    inflationRate = inflationRate,
    expectedReturnRate = expectedReturnRate
)
