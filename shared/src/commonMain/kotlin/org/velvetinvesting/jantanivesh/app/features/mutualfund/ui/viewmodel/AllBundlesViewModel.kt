package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetAllBundledFundsUseCase

class AllBundlesViewModel(
    private val getAllBundledFundsUseCase: GetAllBundledFundsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<BundledMutualFundDomain>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<BundledMutualFundDomain>>> = _uiState.asStateFlow()

    init {
        loadBundles()
    }

    fun loadBundles() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getAllBundledFundsUseCase()
                .onSuccess { data ->
                    _uiState.value = UiState.Success(data)
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
                    _uiState.value = UiState.Error(error.message)
                }
        }
    }
}