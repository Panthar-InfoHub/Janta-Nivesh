package org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.usergoal

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserGoal

/** `GET /user-goal/` */
@Serializable
data class UserGoalsListDto(
    val success: Boolean = false,
    val message: String = "",
    val `data`: List<UserGoal> = emptyList()
)

/** `GET /user-goal/{id}`, and the echo of a successful `POST /user-goal/`. */
@Serializable
data class UserGoalDto(
    val success: Boolean = false,
    val message: String = "",
    val `data`: UserGoal
)
