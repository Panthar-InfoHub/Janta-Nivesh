package org.velvetinvesting.jantanivesh.app.features.goals.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalmapping.GoalMapBodyDto
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.CreateGoalRequest
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalCalculationDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalCalculationRequest
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalConfigDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalDomain

interface GoalsRepository {

    /** The goal types on offer, and the rates, tenures and costs each is sized with. */
    suspend fun getGoalConfig(): NetworkResponse<List<GoalConfigDomain>, ErrorDomain>

    /** What a goal would require, for inputs the user has not saved yet. */
    suspend fun calculateGoal(
        request: GoalCalculationRequest
    ): NetworkResponse<GoalCalculationDomain, ErrorDomain>

    suspend fun createGoal(request: CreateGoalRequest): NetworkResponse<Unit, ErrorDomain>

    suspend fun getAllGoals(): NetworkResponse<List<GoalDomain>, ErrorDomain>

    suspend fun getGoalById(id: String): NetworkResponse<GoalDomain, ErrorDomain>

    suspend fun deleteGoal(goalId: String): NetworkResponse<Unit, ErrorDomain>

    suspend fun mapGoal(body: GoalMapBodyDto): NetworkResponse<Unit, ErrorDomain>

    suspend fun unMapGoal(goalId: String): NetworkResponse<Unit, ErrorDomain>
}
