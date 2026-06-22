package org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels

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

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val showError: Boolean = false,
    val error: String = "",
    val userName: String = "",
    val email: String = "",
    val portfolioValue: String = "",
    val fixedDepositsAmount: String = "",
    val mutualFundsAmount: String = "",
    val pnlTrend: String = "",
    val goals: List<GoalsSummaryDomain> = emptyList(),
    val kycVerified: Boolean = false,
    val tradingAccountVerified: Boolean = false
)
sealed interface HomeScreenEvent {

    data object LoadData : HomeScreenEvent
    data object OnNotificationClicked : HomeScreenEvent
    data object OnInvestInFdClicked : HomeScreenEvent
    data object OnInvestInMfClicked : HomeScreenEvent
    data object OnCreateGoalClicked : HomeScreenEvent
    data object OnInsuranceClicked : HomeScreenEvent
    data object OnVerifyKycClicked : HomeScreenEvent
    data object OnTradingSetupClick : HomeScreenEvent
    data object OnGoToGoalsClicked : HomeScreenEvent
    data class OnGoalClicked(val goalId: String) : HomeScreenEvent
    data object OnCreateCustomGoalClicked : HomeScreenEvent
}

sealed interface HomeScreenSideEffect {
    data object NavigateToNotifications : HomeScreenSideEffect
    data object NavigateToInvestFd : HomeScreenSideEffect
    data object NavigateToInvestMf : HomeScreenSideEffect
    data object NavigateToCreateGoal : HomeScreenSideEffect
    data object NavigateToInsurance : HomeScreenSideEffect
    data object NavigateToKycVerification : HomeScreenSideEffect
    data object NavigateToTradingVerification : HomeScreenSideEffect
    data object NavigateToGoals : HomeScreenSideEffect
    data class NavigateToSpecificGoal(val goalId: String) : HomeScreenSideEffect
}

class HomeScreenViewModel(
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<HomeScreenSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        loadData()
    }

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
                HomeScreenEvent.OnCreateCustomGoalClicked -> _sideEffect.send(HomeScreenSideEffect.NavigateToCreateGoal)
                HomeScreenEvent.LoadData -> loadData()
                is HomeScreenEvent.OnGoalClicked ->{
                    _sideEffect.send(HomeScreenSideEffect.NavigateToSpecificGoal(event.goalId))
                }
                HomeScreenEvent.OnTradingSetupClick -> _sideEffect.send(HomeScreenSideEffect.NavigateToTradingVerification)
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, showError = false, error = "")
            }

            getUserDataUseCase()
                .onSuccess { userData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userName = userData.name,
                            goals = userData.goals,
                            kycVerified = userData.kycVerified,
                            tradingAccountVerified = userData.tradingAccountVerified,
                            email = userData.email
                        )
                    }
                }

        }
    }
}