package org.velvetinvesting.jantanivesh.app.features.goals.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalmapping.GoalMapBodyDto
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalRequest

interface GoalsRepository {
    suspend fun addChildMarriageGoal(goal: GoalRequest.ChildMarriage): NetworkResponse<Unit, ErrorDomain>
    suspend fun addChildEducationGoal(goal: GoalRequest.ChildEducation): NetworkResponse<Unit, ErrorDomain>
    suspend fun addRetirementGoal(goal: GoalRequest.Retirement): NetworkResponse<Unit, ErrorDomain>
    suspend fun addWealthBuildingGoal(goal: GoalRequest.WealthBuildingGoal): NetworkResponse<Unit, ErrorDomain>
    suspend fun deleteGoal(goalId: String): NetworkResponse<Unit, ErrorDomain>
    suspend fun getGoalById(id: String): NetworkResponse<GoalDomain, ErrorDomain>
    suspend fun mapGoal(body: GoalMapBodyDto): NetworkResponse<Unit, ErrorDomain>
    suspend fun unMapGoal(goalId: Int): NetworkResponse<Unit, ErrorDomain>
}
