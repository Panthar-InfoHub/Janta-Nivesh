package org.velvetinvesting.jantanivesh.app.features.goals.domain.models

import androidx.compose.ui.graphics.Color
import org.velvetinvesting.jantanivesh.app.core.theme.*
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType

data class GoalOption(
    val title: String,
    val color: Color,
    val type: GoalType,
    val goalItemId: Int? = null,   // only for wealth goals
    val goalItemName: String? = null
)

val goalOptions = listOf(
    // Child Goals
    GoalOption(
        title = "Child Education",
        color = MutualFundIconBg,
        type = GoalType.ChildEducation
    ),
    GoalOption(
        title = "Child Marriage",
        color = bgColor3,
        type = GoalType.ChildMarriage
    ),

    // Retirement
    GoalOption(
        title = "Retirement",
        color = bgColor4,
        type = GoalType.Retirement
    ),

    // Wealth Goals
    GoalOption(
        title = "Wealth Building",
        color = Secondary,
        type = GoalType.WealthBuilding,
        goalItemId = 1,
        goalItemName = "General Wealth"
    ),

    GoalOption(
        title = "Custom Goal",
        color = Primary,
        type = GoalType.WealthBuilding,
        goalItemId = 2,
        goalItemName = "Custom"
    )
)
