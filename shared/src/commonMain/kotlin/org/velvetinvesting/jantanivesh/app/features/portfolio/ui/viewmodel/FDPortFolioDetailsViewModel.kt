package org.sharad.velvetinvestment.presentation.portfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.portfolio.FixedDepositTransactionDomain
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetFDPortfolioByIdUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetFDRedirectUrlUseCase
import org.sharad.velvetinvestment.utils.BrowserLauncher
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.BrowserLauncher
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositTransactionDomain

class FDPortFolioDetailsViewModel(
    private val getFDPortfolioByIdUseCase: GetFDPortfolioByIdUseCase,
    private val getFDRedirectUrlUseCase: GetFDRedirectUrlUseCase,
    private val openBrowserLauncher: BrowserLauncher,
    private val fdId: String
) : ViewModel() {

    private val _loadingState = MutableStateFlow<UiState<FixedDepositTransactionDomain>>(UiState.Loading)
    val loadingState = _loadingState.asStateFlow()

    init {
        loadFDDetails()
    }
    fun loadFDDetails() {
        viewModelScope.launch {
            _loadingState.value = UiState.Loading
            getFDPortfolioByIdUseCase(fdId)
                .onSuccess { domain ->
                    _loadingState.value = UiState.Success(domain)
                }
                .onError { error ->
                    _loadingState.value = UiState.Error(error.message)
                }
        }
    }

    fun onClick(){
        val data=loadingState.value
        if (_loadingState.value !is UiState.Success) return
        viewModelScope.launch {
            val currentData = (_loadingState.value as UiState.Success).data
            _loadingState.value = UiState.Loading
            getFDRedirectUrlUseCase(id=currentData.id, event = currentData.pendingAction.name)
                .onSuccess { url ->
                    _loadingState.value = data
                    openBrowserLauncher.launchBrowser(url)
                }
                .onError { error ->
                    _loadingState.value = data
                    SnackBarController.showError(error.message)
                }
        }
    }

}
