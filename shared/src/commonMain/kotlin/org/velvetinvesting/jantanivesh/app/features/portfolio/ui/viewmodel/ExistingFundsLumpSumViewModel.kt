package org.sharad.velvetinvestment.presentation.portfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioDomain
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetPortfolioUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.InvestMoreLumpsumUseCase
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDomain

data class LumpSumAdd(
    val prod_Id: String,
    val folio: String,
    val amount:Long
)

sealed interface ExistingFundsLumpSumSideEffect{
    data class OpenBrowserLink(val link:String): ExistingFundsLumpSumSideEffect
}
class ExistingFundsLumpSumViewModel(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val investMoreLumpsumUseCase: InvestMoreLumpsumUseCase
) : ViewModel() {

    private val _loadingState =
        MutableStateFlow<UiState<PortfolioDomain>>(UiState.Loading)
    val uiState = _loadingState.asStateFlow()

    private val _buttonLoading = MutableStateFlow(false)
    val buttonLoading = _buttonLoading.asStateFlow()

    private val _addedFundList = MutableStateFlow<List<LumpSumAdd>>(emptyList())
    val addedFundList = _addedFundList.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ExistingFundsLumpSumSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()


    init {
        loadPortfolio()
    }

    fun loadPortfolio() {
        viewModelScope.launch {
            _loadingState.value= UiState.Loading
            getPortfolioUseCase()
                .onSuccess {
                    _loadingState.value= UiState.Success(it)
                }
                .onError {
                    _loadingState.value= UiState.Error(it.message)
                }
        }
    }

    fun addOrUpdateFund(
        prodId: String,
        folio: String,
        amount: Long
    ) {
        val index = _addedFundList.value.indexOfFirst { it.folio == folio }

        if (index >= 0) {
            _addedFundList.value = _addedFundList.value.toMutableList().apply {
                this[index] = this[index].copy(amount = amount)
            }
        } else {
            _addedFundList.value += LumpSumAdd(
                prod_Id = prodId,
                folio = folio,
                amount = amount
            )
        }
    }

    fun removeFund(folio: String) {
        _addedFundList.value = _addedFundList.value.filterNot {
            it.folio == folio
        }
    }

    fun investMoreLumpsum() {

        if (_addedFundList.value.isEmpty()) {
            viewModelScope.launch {
                SnackBarController.showError("Please select at least one fund")
            }
            return
        }


        viewModelScope.launch {
            _buttonLoading.value=true
            investMoreLumpsumUseCase(_addedFundList.value)
                .onSuccess {
                    _addedFundList.value = emptyList()
                    _buttonLoading.value=false
                    _sideEffect.emit(ExistingFundsLumpSumSideEffect.OpenBrowserLink(it))
                }
                .onError { error ->
                    _buttonLoading.value=false
                    SnackBarController.showError(
                        error.message
                    )
                }
        }
    }

}
