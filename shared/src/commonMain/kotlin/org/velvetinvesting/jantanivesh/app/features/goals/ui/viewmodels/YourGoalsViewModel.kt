package org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.core.utils.formatWithCommas
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.goals.data.mapper.toSummary
import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.GoalsRepository

data class YourGoalsUiData(
    val totalGoalProgressAmt: String = "0",
    val goalTargetAmt: String = "0",
    val goalPercentage: String = "0",
    val goals: List<GoalsSummaryDomain> = emptyList()
)

sealed interface YourGoalsEvent {
    data object OnBackClicked : YourGoalsEvent
    data object OnAddGoalClicked : YourGoalsEvent
    data object OnInvestNowClicked : YourGoalsEvent
    data class OnGoalCardClicked(val goalId: String) : YourGoalsEvent
    data object LoadGoals : YourGoalsEvent
}

sealed interface YourGoalsEffect {
    data object NavigateBack : YourGoalsEffect
    data object NavigateToAddGoal : YourGoalsEffect
    data object NavigateToInvest : YourGoalsEffect
    data class NavigateToGoalDetails(val goalId: String) : YourGoalsEffect
}

/**
 * Reads the goal list from `GET /user-goal/` rather than from the copy embedded in `GET /user/`:
 * this screen is where a goal is created and deleted, so it needs the goals as they are now, not
 * as the last profile fetch saw them.
 */
class YourGoalsViewModel(
    private val goalsRepository: GoalsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<YourGoalsUiData>>(UiState.Loading)
    val uiState: StateFlow<UiState<YourGoalsUiData>> = _uiState.asStateFlow()

    private val _effect = Channel<YourGoalsEffect>()
    val effect = _effect.receiveAsFlow()

    /**
     * Deliberately not loaded in `init`: the screen requests it whenever it enters composition,
     * so coming back from creating or deleting a goal re-reads the list instead of showing the
     * one this view model fetched the first time it was built.
     */
    private fun loadGoals() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            goalsRepository.getAllGoals()
                .onSuccess { goals ->
                    val summaries = goals.map { it.toSummary() }
                    val totalProgress = summaries.sumOf { it.amount }
                    val totalTarget = summaries.sumOf { it.targetAmount }
                    val percentage =
                        if (totalTarget > 0) {
                            ((totalProgress.toDouble() / totalTarget) * 100).toInt()
                        } else {
                            0
                        }

                    _uiState.value = UiState.Success(
                        YourGoalsUiData(
                            goals = summaries,
                            totalGoalProgressAmt = formatWithCommas(totalProgress),
                            goalTargetAmt = formatWithCommas(totalTarget),
                            goalPercentage = percentage.toString()
                        )
                    )
                }
                .onError {
                    SnackBarController.showError(it.message)
                    _uiState.value = UiState.Error(it.message)
                }
        }
    }

    fun handleEvent(event: YourGoalsEvent) {
        when (event) {
            YourGoalsEvent.OnBackClicked -> sendEffect(YourGoalsEffect.NavigateBack)
            YourGoalsEvent.OnAddGoalClicked -> sendEffect(YourGoalsEffect.NavigateToAddGoal)
            YourGoalsEvent.OnInvestNowClicked -> sendEffect(YourGoalsEffect.NavigateToInvest)
            is YourGoalsEvent.OnGoalCardClicked -> sendEffect(
                YourGoalsEffect.NavigateToGoalDetails(event.goalId)
            )
            YourGoalsEvent.LoadGoals -> loadGoals()
        }
    }

    private fun sendEffect(effect: YourGoalsEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
