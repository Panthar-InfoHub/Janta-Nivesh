package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetCategoryMutualFundsUseCase
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.LoadingState
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CategoryMutualFundDomain

class CategoryMutualFundViewModel(
    private val getCategoryMutualFundsUseCase: GetCategoryMutualFundsUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _mutualFunds =
        MutableStateFlow<List<CategoryMutualFundDomain>>(emptyList())
    val mutualFunds = _mutualFunds.asStateFlow()


    init {
        loadMutualFunds()
    }

    fun loadMutualFunds() {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading
            getCategoryMutualFundsUseCase()
                .onSuccess { data ->
                    _loadingState.value = LoadingState.Success
                    _mutualFunds.value = data
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
                    _loadingState.value =
                        LoadingState.Error(error.message)
                }
        }
    }

}
