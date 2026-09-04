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
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.trimTo
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.core.domain.usecase.GetUserDataUseCase

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val showError: Boolean = false,
    val error: String = "",
    val userName: String = "User",
    val email: String = "",
    /**
     * Placeholders rather than zeroes: until the first load lands, "0" would read as a real
     * balance, which is worse than visibly having nothing yet.
     */
    val portfolioValue: String = "XXXXX",
    val fixedDepositsAmount: String = "XXXXX",
    val mutualFundsAmount: String = "XXXXX",
    val pnlTrend: String = "XX",
    val goals: List<GoalsSummaryDomain> = emptyList(),
    val kycVerified: Boolean = false,
    /**
     * Whether to offer the "complete your KYC" card. Driven by whether onboarding was skipped or
     * left unfinished, not by the KYC step alone — a user who skipped in has every step pending.
     */
    val showKycPrompt: Boolean = false,
    /**
     * `GET /user/` no longer reports a trading account, so this always reads false. The profile
     * screen still renders a row for it; the field stays until that row is retired with the rest
     * of the trading-account flow.
     */
    val tradingAccountVerified: Boolean = false,
    val onboardingStage: String = ""
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
    data class NavigateToKycVerification(val onboardingStage: String) : HomeScreenSideEffect
    data object NavigateToTradingVerification : HomeScreenSideEffect
    data object NavigateToGoals : HomeScreenSideEffect
    data class NavigateToSpecificGoal(val goalId: String) : HomeScreenSideEffect
    data object NavigateToDailySip : HomeScreenSideEffect
    data object NavigateToMonthlySip : HomeScreenSideEffect
}

/**
 * `GET /user/` returns the dashboard totals alongside the profile, so the home screen is one
 * call: there is no separate portfolio read to fan out to anymore.
 */
class HomeScreenViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val authPrefs: AuthPrefs
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
                HomeScreenEvent.OnVerifyKycClicked -> _sideEffect.send(HomeScreenSideEffect.NavigateToKycVerification(uiState.value.onboardingStage))
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
                it.copy(isLoading = true, showError = false, error = "")
            }

            when (val result = getUserDataUseCase()) {
                is NetworkResponse.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        showError = true,
                        error = result.error.message
                    )
                }

                is NetworkResponse.Success -> {
                    val user = result.data
                    val dashboard = user.dashboard

                    authPrefs.setMpinEnabled(user.mpinEnabled)
                    authPrefs.setMpinSetup(user.mpinIsSetup)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            showError = false,
                            error = "",
                            // A blank name would leave the greeting reading "Hello ,".
                            userName = user.name.ifBlank { it.userName },
                            email = user.email,
                            portfolioValue = formatMoneyAfterL(dashboard.portfolioValue.toLong()),
                            fixedDepositsAmount = formatMoneyAfterL(
                                dashboard.fixedDeposits.toLong()
                            ),
                            mutualFundsAmount = formatMoneyAfterL(dashboard.mutualFunds.toLong()),
                            pnlTrend = dashboard.returnPercent.trimTo(2),
                            goals = user.goals,
                            kycVerified = user.kycVerified,
                            showKycPrompt = !user.kycVerified,
                            onboardingStage = user.onboarding.currentStage,
                        )
                    }
                }
            }
        }
    }
}
