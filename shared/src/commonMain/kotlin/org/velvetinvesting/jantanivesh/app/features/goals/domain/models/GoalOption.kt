package org.velvetinvesting.jantanivesh.app.features.goals.domain.models

import androidx.compose.ui.graphics.Color
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType

data class GoalOption(
    val title: String,
    val color: Color,
    val type: GoalType,
    val goalItemId: Int? = null,   // only for wealth goals
    val goalItemName: String? = null
)