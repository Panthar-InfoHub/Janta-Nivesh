package org.velvetinvesting.jantanivesh.app.features.goals.domain.models

/**
 * `POST /user-goal/calculate`: what a goal would cost and require, for inputs the user is still
 * editing. The create screen shows this so the figures it previews are the same ones the goal
 * will be stored with.
 */
data class GoalCalculationDomain(
    val goalTypeId: Int,
    val yearsRemaining: Int,
    val currentCost: Double?,
    val targetAmount: Double?,
    val currentSavings: Double,
    /** The rates the server actually applied — the type's defaults unless overridden. */
    val inflationRateUsed: Double?,
    val expectedReturnRateUsed: Double?,
    val futureTargetAmount: Double,
    val fvCurrentSavings: Double,
    val netRequiredCorpus: Double,
    val requiredMonthlySip: Double,
    val requiredLumpsumToday: Double
)
