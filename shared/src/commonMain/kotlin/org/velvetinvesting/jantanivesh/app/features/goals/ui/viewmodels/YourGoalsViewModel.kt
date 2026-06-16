package org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.UserDataRepo

data class YourGoalsUiState(
    val totalGoalProgressAmt: String = "0",
    val goalTargetAmt: String = "0",
    val goalPercentage: String = "0",
    val goals: List<GoalsSummaryDomain> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface YourGoalsEvent {
    data object OnBackClicked : YourGoalsEvent
    data object OnAddGoalClicked : YourGoalsEvent
    data object OnInvestNowClicked : YourGoalsEvent
    data class OnGoalCardClicked(val goalId: String) : YourGoalsEvent
}

sealed interface YourGoalsEffect {
    data object NavigateBack : YourGoalsEffect
    data object NavigateToAddGoal : YourGoalsEffect
    data object NavigateToInvest : YourGoalsEffect
    data class NavigateToGoalDetails(val goalId: String) : YourGoalsEffect
}

class YourGoalsViewModel(
    private val userDataRepo: UserDataRepo
) : ViewModel() {
    private val _uiState = MutableStateFlow(YourGoalsUiState())
    val uiState: StateFlow<YourGoalsUiState> = _uiState.asStateFlow()

    private val _effect = Channel<YourGoalsEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadGoals()
    }

    private fun loadGoals() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val response = userDataRepo.getUserData()
            _uiState.update { it.copy(isLoading = false) }

            if (response is NetworkResponse.Success) {
                val goals = response.data.goals
                val totalProgress = goals.sumOf { it.amount }
                val totalTarget = goals.sumOf { it.targetAmount }
                val percentage = if (totalTarget > 0) (totalProgress.toDouble() / totalTarget * 100).toInt() else 0

                _uiState.update {
                    it.copy(
                        goals = goals,
                        totalGoalProgressAmt = totalProgress.toString(),
                        goalTargetAmt = totalTarget.toString(),
                        goalPercentage = percentage.toString()
                    )
                }
            }
        }
    }

    fun handleEvent(event: YourGoalsEvent) {
        when (event) {
            YourGoalsEvent.OnBackClicked -> sendEffect(YourGoalsEffect.NavigateBack)
            YourGoalsEvent.OnAddGoalClicked -> sendEffect(YourGoalsEffect.NavigateToAddGoal)
            YourGoalsEvent.OnInvestNowClicked -> sendEffect(YourGoalsEffect.NavigateToInvest)
            is YourGoalsEvent.OnGoalCardClicked -> sendEffect(YourGoalsEffect.NavigateToGoalDetails(event.goalId))
        }
    }

    private fun sendEffect(effect: YourGoalsEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
