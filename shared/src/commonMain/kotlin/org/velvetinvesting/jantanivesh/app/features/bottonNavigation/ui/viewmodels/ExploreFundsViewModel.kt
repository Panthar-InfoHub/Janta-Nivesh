package org.velvetinvesting.jantanivesh.app.features.bottonNavigation.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.velvetinvesting.jantanivesh.app.features.bottonNavigation.domain.models.FixedTopPicksUiModel
import org.velvetinvesting.jantanivesh.app.features.bottonNavigation.domain.models.MutualFundTopPicksUiModel

data class ExploreFundsUiState(
    val isLoading: Boolean = false,
    val mutualFundList: List<MutualFundTopPicksUiModel> = emptyList(),
    val fixedDepositList: List<FixedTopPicksUiModel> = emptyList()
)

sealed interface ExploreFundsEvent {
    object LoadInitialData : ExploreFundsEvent
    object OnMutualFundsCategoryClick : ExploreFundsEvent
    object OnFixedDepositCategoryClick : ExploreFundsEvent
    data class OnMutualFundInvestClick(val fundId: String) : ExploreFundsEvent
    data class OnFixedDepositClick(val fdId: String) : ExploreFundsEvent
}

class ExploreFundsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreFundsUiState())
    val uiState: StateFlow<ExploreFundsUiState> = _uiState.asStateFlow()

    init {
        handleEvent(ExploreFundsEvent.LoadInitialData)
    }

    fun handleEvent(event: ExploreFundsEvent) {
        when (event) {
            ExploreFundsEvent.LoadInitialData -> loadMockData()

            ExploreFundsEvent.OnMutualFundsCategoryClick -> {
                // TODO: Handle navigation to Mutual Funds listing
            }

            ExploreFundsEvent.OnFixedDepositCategoryClick -> {
                // TODO: Handle navigation to Fixed Deposits listing
            }

            is ExploreFundsEvent.OnMutualFundInvestClick -> {
                // TODO: Handle navigation to specific Mutual Fund detail/invest screen
                // val id = event.fundId
            }

            is ExploreFundsEvent.OnFixedDepositClick -> {
                // TODO: Handle navigation to specific Fixed Deposit detail screen
                // val id = event.fdId
            }
        }
    }

    private fun loadMockData() {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = false,
                mutualFundList = listOf(
                    MutualFundTopPicksUiModel(
                        icon = "",
                        name = "SBI Gold Fund",
                        metadata = "Equity, Sectoral/Thematic, High Risk",
                        returnYears = 3,
                        percentage = 18.5,
                        id = "1"
                    )
                ),
                fixedDepositList = listOf(
                    FixedTopPicksUiModel(
                        icon = "",
                        name = "SBI Bank",
                        metadata = "LOW RISK",
                        percentage = 7.25,
                        id = "2"
                    )
                )
            )
        }
    }
}