package org.velvetinvesting.jantanivesh.app.features.bottonNavigation.domain.models

import org.velvetinvesting.jantanivesh.app.features.core.domain.models.GoalOption


data class GoalsSummaryDomain(
    val goalTypes: GoalOption,
    val amount:Long,
    val targetAmount: Long,
    val goalId: String
)

fun GoalsSummaryDomain.progressPercent(): Int =
    if (targetAmount > 0)
        ((amount.toDouble() / targetAmount) * 100).toInt()
    else 0
