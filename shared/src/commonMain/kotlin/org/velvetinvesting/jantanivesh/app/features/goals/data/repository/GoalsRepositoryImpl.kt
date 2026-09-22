package org.velvetinvesting.jantanivesh.app.features.goals.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
import org.velvetinvesting.jantanivesh.app.core.networking.safeUnitRequest
import org.velvetinvesting.jantanivesh.app.features.goals.data.mapper.toCalculateBody
import org.velvetinvesting.jantanivesh.app.features.goals.data.mapper.toCreateBody
import org.velvetinvesting.jantanivesh.app.features.goals.data.mapper.toDomain
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.calculate.GoalCalculationResponseDto
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.config.GoalConfigResponseDto
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalmapping.GoalMapBodyDto
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalmapping.UnMapGoalRequestDto
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.usergoal.UserGoalDto
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.usergoal.UserGoalsListDto
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.CreateGoalRequest
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalCalculationDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalCalculationRequest
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalConfigDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.GoalsRepository

/**
 * The `v2.0` goal endpoints, all under `/user-goal`. The server owns the goal arithmetic: this
 * class only carries inputs out and projections back.
 */
class GoalsRepositoryImpl(
    private val client: HttpClient
) : GoalsRepository {

    override suspend fun getGoalConfig(): NetworkResponse<List<GoalConfigDomain>, ErrorDomain> {
        val response = safeRequest<GoalConfigResponseDto> {
            client.get(getUrl("/user-goal/config"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(
                response.data.data.map { it.toDomain() }
            )
        }
    }

    override suspend fun calculateGoal(
        request: GoalCalculationRequest
    ): NetworkResponse<GoalCalculationDomain, ErrorDomain> {
        val response = safeRequest<GoalCalculationResponseDto> {
            client.post(getUrl("/user-goal/calculate")) {
                setBody(request.toCalculateBody())
            }
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.data.toDomain())
        }
    }

    override suspend fun createGoal(request: CreateGoalRequest): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/user-goal/")) {
                setBody(request.toCreateBody())
            }
        }
    }

    override suspend fun getAllGoals(): NetworkResponse<List<GoalDomain>, ErrorDomain> {
        val response = safeRequest<UserGoalsListDto> {
            client.get(getUrl("/user-goal/"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(
                response.data.data.map { it.toDomain() }
            )
        }
    }

    override suspend fun getGoalById(id: String): NetworkResponse<GoalDomain, ErrorDomain> {
        val response = safeRequest<UserGoalDto> {
            client.get(getUrl("/user-goal/$id"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.data.toDomain())
        }
    }

    override suspend fun deleteGoal(goalId: String): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.delete(getUrl("/user-goal/$goalId"))
        }
    }

    override suspend fun mapGoal(body: GoalMapBodyDto): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/user-goal/map")) {
                setBody(body)
            }
        }
    }

    override suspend fun unMapGoal(goalId: String): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.delete(getUrl("/user-goal/map-remove")) {
                setBody(UnMapGoalRequestDto(goalId))
            }
        }
    }
}
