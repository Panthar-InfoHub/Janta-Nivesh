package org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models

import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalOption

/**
 * A goal as the home and goals lists show it: the name and colour of its type, what is saved
 * against it, and what it is heading for.
 *
 * [targetAmount] is the inflation-adjusted figure the server projected, not the present-day cost,
 * so the progress a card shows is progress towards the amount the goal will actually need.
 * [progressPercent] is the server's own, for the same reason.
 */
data class GoalsSummaryDomain(
    val goalId: String,
    val goalTypes: GoalOption,
    /** What to title the card: the goal's own name, falling back to its type. */
    val title: String,
    val amount: Long,
    val targetAmount: Long,
    val progressPercent: Int,
    val requiredMonthlySip: Double = 0.0,
    val yearsRemaining: Int = 0
)
