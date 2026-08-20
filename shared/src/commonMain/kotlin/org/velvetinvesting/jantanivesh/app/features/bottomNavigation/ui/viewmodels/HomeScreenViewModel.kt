package org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.trimTo
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.core.domain.usecase.GetUserDataUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.GetPortfolioUseCase

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

    data object OnDailySipClicked : HomeScreenEvent
    data object OnMonthlySipClicked : HomeScreenEvent
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
    data object NavigateToDailySip : HomeScreenSideEffect
    data object NavigateToMonthlySip : HomeScreenSideEffect
}

class HomeScreenViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getPortfolioUseCase: GetPortfolioUseCase
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
                HomeScreenEvent.OnDailySipClicked -> _sideEffect.send(HomeScreenSideEffect.NavigateToDailySip)
                HomeScreenEvent.OnMonthlySipClicked -> _sideEffect.send(HomeScreenSideEffect.NavigateToMonthlySip)
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    showError = false,
                    error = ""
                )
            }

            val userDeferred = async { getUserDataUseCase() }
            val portfolioDeferred = async { getPortfolioUseCase() }

            val userResult = userDeferred.await()
            val portfolioResult = portfolioDeferred.await()

            var errorMessage: String? = null

            userResult
                .onSuccess { userData ->
                    _uiState.update {
                        it.copy(
                            userName = userData.name,
                            goals = userData.goals,
                            kycVerified = userData.kycVerified,
                            tradingAccountVerified = userData.tradingAccountVerified,
                            email = userData.email
                        )
                    }
                }
                .onError {
                    errorMessage = it.message
                }

            portfolioResult
                .onSuccess { portfolio ->
                    _uiState.update {
                        it.copy(
                            portfolioValue = formatMoneyAfterL(portfolio.dashboard.investedAmount.toLong()),
                            fixedDepositsAmount = formatMoneyAfterL(
                                portfolio.totalInvestments.allocation.fixedDeposits.value.toLong()
                            ),
                            mutualFundsAmount = formatMoneyAfterL(
                                portfolio.totalInvestments.allocation.mutualFunds.value.toLong()
                            ),
                            pnlTrend = portfolio.investedAmountBreakdown.returnsPercent.trimTo(2)
                        )
                    }
                }
                .onError {
                    if (errorMessage == null) {
                        errorMessage = it.message
                    }
                }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    showError = errorMessage != null,
                    error = errorMessage.orEmpty()
                )
            }
        }
    }
}