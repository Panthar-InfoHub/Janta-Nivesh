package org.velvetinvesting.jantanivesh.app.features.bundles.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.usecases.GetAllBundlesUseCase
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PurchaseMode
import org.velvetinvesting.jantanivesh.app.features.cart.CartInfo

data class AllBundlesUiState(
    val isLoading: Boolean = false,
    val bundles: List<BundleSummaryDomain> = emptyList(),
    val cartAmount: Int = 0,
    /** How the user means to invest, carried into whichever bundle they open. */
    val purchaseMode: PurchaseMode = PurchaseMode.MONTHLY,
    val error: String? = null
)

sealed interface AllBundlesEvent {
    data object Retry : AllBundlesEvent
    data class OnPurchaseModeSelected(val mode: PurchaseMode) : AllBundlesEvent
    data class OnBundleClicked(val bundleId: String) : AllBundlesEvent
    data object OnCartClicked : AllBundlesEvent
    data object OnBackClicked : AllBundlesEvent
}

sealed interface AllBundlesEffect {
    data object NavigateBack : AllBundlesEffect
    data object NavigateToCart : AllBundlesEffect
    data class NavigateToBundle(val bundleId: String, val purchaseMode: PurchaseMode) : AllBundlesEffect
}

class AllBundlesViewModel(
    private val getAllBundlesUseCase: GetAllBundlesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllBundlesUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<AllBundlesEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadBundles()
        viewModelScope.launch {
            CartInfo.fundAmount.collect { amount ->
                _uiState.update { it.copy(cartAmount = amount) }
            }
        }
    }

    fun handleEvent(event: AllBundlesEvent) {
        when (event) {
            AllBundlesEvent.Retry -> loadBundles()

            is AllBundlesEvent.OnPurchaseModeSelected -> _uiState.update { it.copy(purchaseMode = event.mode) }

            is AllBundlesEvent.OnBundleClicked -> sendEffect(
                AllBundlesEffect.NavigateToBundle(event.bundleId, _uiState.value.purchaseMode)
            )

            AllBundlesEvent.OnCartClicked -> sendEffect(AllBundlesEffect.NavigateToCart)

            AllBundlesEvent.OnBackClicked -> sendEffect(AllBundlesEffect.NavigateBack)
        }
    }

    private fun loadBundles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val response = getAllBundlesUseCase()) {
                is NetworkResponse.Success -> _uiState.update {
                    it.copy(isLoading = false, bundles = response.data)
                }

                is NetworkResponse.Error -> _uiState.update {
                    it.copy(isLoading = false, error = response.error.message)
                }
            }
        }
    }

    private fun sendEffect(effect: AllBundlesEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
