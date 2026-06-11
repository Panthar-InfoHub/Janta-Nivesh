package org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FixedDepositDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.GetTopPickFDUseCase

data class FixedDepositsUiState(
    val searchQuery: String = "",
    val bestRate: String = "7.40%",
    val bestRateBank: String = "Bajaj Finance",
    val activeFdsCount: Int = 0,
    val totalInvested: String = "₹0",
    val fdList: List<FixedDepositDomain> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface FixedDepositsEvent {
    data object NavigateToFdStatus: FixedDepositsEvent
    data class OnSearchQueryChanged(val query: String) : FixedDepositsEvent
    data class OnFdItemClicked(val fdItem: FixedDepositDomain) : FixedDepositsEvent
    data object OnBackClicked : FixedDepositsEvent
    data object OnFilterMenuClicked : FixedDepositsEvent
    data object OnExploreFdsArrowClicked : FixedDepositsEvent
}

sealed interface FixedDepositsEffect {
    data object NavigateToFdStatus : FixedDepositsEffect
    data object NavigateToFilters: FixedDepositsEffect
    data object NavigateBack : FixedDepositsEffect
    data class NavigateToFdDetails(val id: String) : FixedDepositsEffect
    data object NavigateToExploreFds : FixedDepositsEffect
}

class FixedDepositsViewModel(
    private val id: String,
    private val getTopPickFDUseCase: GetTopPickFDUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FixedDepositsUiState())
    val uiState: StateFlow<FixedDepositsUiState> = _uiState.asStateFlow()

    private val _effect = Channel<FixedDepositsEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadFixedDeposits()
    }

    fun handleEvent(event: FixedDepositsEvent) {
        when (event) {
            is FixedDepositsEvent.OnSearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }
            is FixedDepositsEvent.OnFdItemClicked -> {
                sendEffect(FixedDepositsEffect.NavigateToFdDetails(event.fdItem.id))
            }
            FixedDepositsEvent.OnBackClicked -> {
                sendEffect(FixedDepositsEffect.NavigateBack)
            }
            FixedDepositsEvent.OnFilterMenuClicked -> {
                sendEffect(FixedDepositsEffect.NavigateToFilters)
            }
            FixedDepositsEvent.OnExploreFdsArrowClicked -> {
                sendEffect(FixedDepositsEffect.NavigateToExploreFds)
            }
            FixedDepositsEvent.NavigateToFdStatus -> {
                sendEffect(FixedDepositsEffect.NavigateToFdStatus)
            }
        }
    }

    private fun loadFixedDeposits() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getTopPickFDUseCase()
                .onSuccess { items ->
                    _uiState.update {
                        it.copy(
                            fdList = items,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                .onError { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    private fun sendEffect(effect: FixedDepositsEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
