package org.velvetinvesting.jantanivesh.app.features.core.domain

sealed interface GoalType {
    val id: Int

    data object ChildEducation : GoalType { override val id = 1 }
    data object ChildMarriage : GoalType { override val id = 2 }
    data object Retirement : GoalType { override val id = 3 }
    data object WealthBuilding : GoalType { override val id = 4 }
}