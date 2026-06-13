package org.velvetinvesting.jantanivesh.app.features.bottonNavigation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.features.bottonNavigation.domain.models.FixedTopPicksUiModel
import org.velvetinvesting.jantanivesh.app.features.bottonNavigation.domain.models.MutualFundTopPicksUiModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetMutualFundTopPicksUseCase

data class ExploreFundsUiState(
    val isLoading: Boolean = false,
    val showError: Boolean = false,
    val error: String = "",
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

sealed interface ExploreFundsEffect {
    object NavigateToMutualFunds : ExploreFundsEffect
    object NavigateToFixedDeposits : ExploreFundsEffect

    data class NavigateToMutualFundDetail(
        val fundId: String
    ) : ExploreFundsEffect

    data class NavigateToFixedDepositDetail(
        val fdId: String
    ) : ExploreFundsEffect
}
class ExploreFundsViewModel(
    private val getMutualFundTopPicksUseCase: GetMutualFundTopPicksUseCase,
//    private val getFixedDepositTopPicksUseCase: GetTopPickFDUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreFundsUiState())
    val uiState: StateFlow<ExploreFundsUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ExploreFundsEffect>()
    val effect = _effect.asSharedFlow()

    init {
        handleEvent(ExploreFundsEvent.LoadInitialData)
    }

    fun handleEvent(event: ExploreFundsEvent) {
        when (event) {

            ExploreFundsEvent.LoadInitialData -> loadData()

            ExploreFundsEvent.OnMutualFundsCategoryClick -> {
                viewModelScope.launch {
                    _effect.emit(
                        ExploreFundsEffect.NavigateToMutualFunds
                    )
                }
            }

            ExploreFundsEvent.OnFixedDepositCategoryClick -> {
                viewModelScope.launch {
                    _effect.emit(
                        ExploreFundsEffect.NavigateToFixedDeposits
                    )
                }
            }

            is ExploreFundsEvent.OnMutualFundInvestClick -> {
                viewModelScope.launch {
                    _effect.emit(
                        ExploreFundsEffect.NavigateToMutualFundDetail(
                            fundId = event.fundId
                        )
                    )
                }
            }

            is ExploreFundsEvent.OnFixedDepositClick -> {
                viewModelScope.launch {
                    _effect.emit(
                        ExploreFundsEffect.NavigateToFixedDepositDetail(
                            fdId = event.fdId
                        )
                    )
                }
            }
        }
    }

    private fun loadData() {

        _uiState.update {
            it.copy(
                isLoading = true,
                showError = false,
                error = ""
            )
        }

        viewModelScope.launch {

            val mutualDeferred = async {
                getMutualFundTopPicksUseCase()
            }

            val fdDeferred = async {
//                getFixedDepositTopPicksUseCase()
            }

            val mutualResponse = mutualDeferred.await()
            val fdResponse = fdDeferred.await()

            var mutualFunds: List<MutualFundTopPicksUiModel> = emptyList()
            var fixedDeposits: List<FixedTopPicksUiModel> = emptyList()

            var hasAnySuccess = false
            var errorMessage = "Something went wrong"

            mutualResponse
                .onSuccess { data ->

                    hasAnySuccess = true

                    mutualFunds = data.map { fund ->
                        MutualFundTopPicksUiModel(
                            id = fund.id,
                            icon = fund.icon,
                            name = fund.name,
                            metadata =   fund.category +
                                    (fund.remark?.let { " • $it" }.orEmpty()) +
                                    (fund.riskText?.let { " • $it Risk" }.orEmpty()),
                            returnYears = 3,
                            percentage = fund.returnYearsRate.year3,
                        )
                    }
                }
                .onError {
                    errorMessage = it.message
                }

//            fdResponse
//                .onSuccess { data ->
//
//                    hasAnySuccess = true
//
//                    fixedDeposits = data.map { fd ->
//                        FixedTopPicksUiModel(
//                            id = fd.id,
//                            icon = fd.bankLogo,
//                            name = fd.bankName,
//                            metadata = fd.riskType,
//                            percentage = fd.interestRate
//                        )
//                    }
//                }
//                .onError {
//                    errorMessage = it.message
//                }

            if (hasAnySuccess) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        showError = false,
                        error = "",
                        mutualFundList = mutualFunds,
                        fixedDepositList = fixedDeposits
                    )
                }

            } else {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        showError = true,
                        error = errorMessage
                    )
                }
            }
        }
    }
}