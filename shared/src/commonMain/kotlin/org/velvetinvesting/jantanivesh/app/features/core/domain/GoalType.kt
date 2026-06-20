package org.velvetinvesting.jantanivesh.app.features.core.domain

sealed interface GoalType {
    val id: Int
    val displayName: String

    data object ChildEducation : GoalType {
        override val id = 1
        override val displayName = "Child Education"
    }

    data object ChildMarriage : GoalType {
        override val id = 2
        override val displayName = "Child Marriage"
    }

    data object Retirement : GoalType {
        override val id = 3
        override val displayName = "Retirement"
    }

    data object WealthBuilding : GoalType {
        override val id = 4
        override val displayName = "Wealth Building"
    }
}
