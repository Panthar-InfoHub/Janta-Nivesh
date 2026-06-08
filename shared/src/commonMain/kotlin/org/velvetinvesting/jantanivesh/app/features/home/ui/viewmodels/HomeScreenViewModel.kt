package org.velvetinvesting.jantanivesh.app.features.home.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface HomeScreenEvent {
    data object OnNotificationClicked : HomeScreenEvent
    data object OnInvestInFdClicked : HomeScreenEvent
    data object OnInvestInMfClicked : HomeScreenEvent
    data object OnCreateGoalClicked : HomeScreenEvent
    data object OnInsuranceClicked : HomeScreenEvent
    data object OnVerifyKycClicked : HomeScreenEvent
    data object OnGoToGoalsClicked : HomeScreenEvent
    data object OnCreateCustomGoalClicked : HomeScreenEvent
}

sealed interface HomeScreenSideEffect {
    data object NavigateToNotifications : HomeScreenSideEffect
    data object NavigateToInvestFd : HomeScreenSideEffect
    data object NavigateToInvestMf : HomeScreenSideEffect
    data object NavigateToCreateGoal : HomeScreenSideEffect
    data object NavigateToInsurance : HomeScreenSideEffect
    data object NavigateToKycVerification : HomeScreenSideEffect
    data object NavigateToGoals : HomeScreenSideEffect
    data object NavigateToCustomGoal : HomeScreenSideEffect
}

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<HomeScreenSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onEvent(event: HomeScreenEvent) {
        viewModelScope.launch {
            when (event) {
                HomeScreenEvent.OnNotificationClicked -> _sideEffect.send(HomeScreenSideEffect.NavigateToNotifications)
                HomeScreenEvent.OnInvestInFdClicked -> _sideEffect.send(HomeScreenSideEffect.NavigateToInvestFd)
                HomeScreenEvent.OnInvestInMfClicked -> _sideEffect.send(HomeScreenSideEffect.NavigateToInvestMf)
                HomeScreenEvent.OnCreateGoalClicked -> _sideEffect.send(HomeScreenSideEffect.NavigateToCreateGoal)
                HomeScreenEvent.OnInsuranceClicked -> _sideEffect.send(HomeScreenSideEffect.NavigateToInsurance)
                HomeScreenEvent.OnVerifyKycClicked -> _sideEffect.send(HomeScreenSideEffect.NavigateToKycVerification)
                HomeScreenEvent.OnGoToGoalsClicked -> _sideEffect.send(HomeScreenSideEffect.NavigateToGoals)
                HomeScreenEvent.OnCreateCustomGoalClicked -> _sideEffect.send(HomeScreenSideEffect.NavigateToCustomGoal)
            }
        }
    }
}

data class HomeScreenUiState(
    val username: String = "Aham",
    val timeOfDay: String = "",
    val portfolioValue: String = "245680",
    val fixedDepositsAmount: String = "150000",
    val mutualFundsAmount: String = "95680",
    val pnlTrend: String = "+5.3",
    val goals: List<Goal> = listOf(
        Goal(
            name = "New Car", amount = "175000", progress = 0.35f
        ),
        Goal(
            name = "Emergency", amount = "175000", progress = 0.35f
        )
    )
)

data class Goal(
    val name: String,
    val amount: String,
    val progress: Float,
)