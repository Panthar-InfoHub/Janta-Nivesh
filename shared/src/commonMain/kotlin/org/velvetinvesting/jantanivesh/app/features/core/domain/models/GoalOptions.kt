package org.velvetinvesting.jantanivesh.app.features.core.domain.models

import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType

data class GoalOption(
    val title: String,
    val type: GoalType,
    val goalItemId: Int? = null,
    val goalItemName: String? = null
)