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
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.core.domain.usecase.GetUserDataUseCase

data class MainGoalsUiState(
    val goals: List<GoalsSummaryDomain> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface MainGoalsEvent {
    data object OnBackClicked : MainGoalsEvent
    data object OnSetGoalClicked : MainGoalsEvent
    data object LoadGoals : MainGoalsEvent
}

sealed interface MainGoalsEffect {
    data object NavigateBack : MainGoalsEffect
    data object NavigateToSetGoal : MainGoalsEffect
    data object NavigateToYourGoals : MainGoalsEffect
}

class MainGoalsViewModel(
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainGoalsUiState())
    val uiState: StateFlow<MainGoalsUiState> = _uiState.asStateFlow()

    private val _effect = Channel<MainGoalsEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadGoals()
    }

    fun handleEvent(event: MainGoalsEvent) {
        when (event) {
            MainGoalsEvent.OnBackClicked -> sendEffect(MainGoalsEffect.NavigateBack)
            MainGoalsEvent.OnSetGoalClicked -> sendEffect(MainGoalsEffect.NavigateToSetGoal)
            MainGoalsEvent.LoadGoals -> loadGoals()
        }
    }

    private fun loadGoals() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getUserDataUseCase()
                .onSuccess { userData ->
                    _uiState.update { it.copy(isLoading = false, goals = userData.goals) }
                    if (userData.goals.isNotEmpty()) {
                        sendEffect(MainGoalsEffect.NavigateToYourGoals)
                    }
                }
                .onSuccess { 
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun sendEffect(effect: MainGoalsEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
