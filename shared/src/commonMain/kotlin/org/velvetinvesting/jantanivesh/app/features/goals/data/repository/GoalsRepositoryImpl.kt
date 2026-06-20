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
import org.velvetinvesting.jantanivesh.app.features.goals.data.mapper.toDomain
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.addgoals.ChildGoalBodyDto
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.addgoals.RetirementGoalBodyDto
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.addgoals.WealthBuildingGoalBodyDto
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalbyid.GoalByIdDto
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalmapping.GoalMapBodyDto
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalmapping.UnMapGoalRequestDto
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalRequest
import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.GoalsRepository

class GoalsRepositoryImpl(
    private val client: HttpClient
) : GoalsRepository {

    override suspend fun addChildMarriageGoal(goal: GoalRequest.ChildMarriage): NetworkResponse<Unit, ErrorDomain> {
        val body = ChildGoalBodyDto(
            child_age = goal.childAge,
            child_name = goal.childName,
            current_goal_cost = goal.currentGoalCost,
            current_saved_amount = goal.currentSavedAmount,
            goal_type_id = goal.goalTypeId,
            inflation_rate = goal.inflationRate.toDouble(),
            return_rate = goal.returnRate,
            years_left = goal.yearsToGoal
        )
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal")) {
                setBody(body)
            }
        }
    }

    override suspend fun addChildEducationGoal(goal: GoalRequest.ChildEducation): NetworkResponse<Unit, ErrorDomain> {
        val body = ChildGoalBodyDto(
            child_age = goal.childAge,
            child_name = goal.childName,
            current_goal_cost = goal.currentGoalCost,
            current_saved_amount = goal.currentSavedAmount,
            goal_type_id = goal.goalTypeId,
            inflation_rate = goal.inflationRate.toDouble(),
            return_rate = goal.returnRate,
            years_left = goal.yearsToGoal
        )
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal")) {
                setBody(body)
            }
        }
    }

    override suspend fun addRetirementGoal(goal: GoalRequest.Retirement): NetworkResponse<Unit, ErrorDomain> {
        val body = RetirementGoalBodyDto(
            current_age = goal.currentAge,
            current_monthly_expense = goal.currentMonthlyExpense,
            current_saved_amount = goal.currentSavedAmount,
            goal_type_id = goal.goalTypeId,
            inflation_rate = goal.inflationRate.toDouble(),
            life_expectancy = goal.lifeExpectancy,
            post_retirement_return = goal.postRetirementReturn.toDouble(),
            retirement_age = goal.retirementAge,
            return_rate = goal.returnRate.toDouble()
        )
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal")) {
                setBody(body)
            }
        }
    }

    override suspend fun addWealthBuildingGoal(goal: GoalRequest.WealthBuildingGoal): NetworkResponse<Unit, ErrorDomain> {
        val body = WealthBuildingGoalBodyDto(
            current_goal_cost = goal.currentGoalCost,
            current_saved_amount = goal.currentSavedAmount,
            goal_item_id = goal.goalItemId,
            goal_item_name = goal.goalItemName,
            goal_name = goal.goalName,
            goal_type_id = goal.goalTypeId,
            inflation_rate = goal.inflationRate.toDouble(),
            return_rate = goal.returnRate.toDouble(),
            years_left = goal.yearsToGoal
        )
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal")) {
                setBody(body)
            }
        }
    }

    override suspend fun deleteGoal(goalId: String): NetworkResponse<Unit, ErrorDomain> {
        return safeRequest<Unit> {
            client.delete(getUrl("/user-goal/$goalId"))
        }
    }

    override suspend fun getGoalById(id: String): NetworkResponse<GoalDomain, ErrorDomain> {
        val response = safeRequest<GoalByIdDto> {
            client.get(getUrl("/user-goal/$id"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.data.toDomain())
        }
    }

    override suspend fun mapGoal(body: GoalMapBodyDto): NetworkResponse<Unit, ErrorDomain> {
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal/map")) {
                setBody(body)
            }
        }
    }

    override suspend fun unMapGoal(goalId: Int): NetworkResponse<Unit, ErrorDomain> {
        return safeRequest<Unit> {
            client.delete(getUrl("/user-goal/map-remove")) {
                setBody(UnMapGoalRequestDto(goalId))
            }
        }
    }
}
