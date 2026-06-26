package org.sharad.velvetinvestment.presentation.portfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.portfolio.FolioFundDomain
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetFolioFundsUseCase
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FolioFundDomain

class FolioFundsMFViewModel(
    private val folioId: String,
    private val getFolioFundsUseCase: GetFolioFundsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<FolioFundDomain>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadFolioFunds()
    }

    fun loadFolioFunds() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getFolioFundsUseCase(folioId)
                .onSuccess {
                    _uiState.value = UiState.Success(it)
                }
                .onError {
                    _uiState.value = UiState.Error(it.message)
                }
        }
    }
}
