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
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.GetFixedDepositsSearchResultUseCase

data class ExploreFdUiState(
    val searchQuery: String = "",
    val totalFundsCount: String = "0",
    val selectedFilter: String = "Public Bank",
    val sortOption: String = "Returns (3Y)",
    val fundsList: List<FixedDepositDomain> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingNext: Boolean = false,
    val errorMessage: String? = null
)

sealed interface ExploreFdEvent {
    data class OnSearchQueryChanged(val query: String) : ExploreFdEvent
    data class OnFilterChipClicked(val filter: String) : ExploreFdEvent
    data class OnFundItemClicked(val fundItem: FixedDepositDomain) : ExploreFdEvent
    data object OnFilterMenuClicked : ExploreFdEvent
    data object OnSortDropdownClicked : ExploreFdEvent
    data object OnLoadMoreClicked : ExploreFdEvent
    data object OnBackClicked : ExploreFdEvent
}

sealed interface ExploreFdEffect {
    data object NavigateBack : ExploreFdEffect
    data class NavigateToFdDetails(val id: String) : ExploreFdEffect
}

class ExploreFdViewModel(
    private val getFDSearchResult: GetFixedDepositsSearchResultUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreFdUiState())
    val uiState: StateFlow<ExploreFdUiState> = _uiState.asStateFlow()

    private val _effect = Channel<ExploreFdEffect>()
    val effect = _effect.receiveAsFlow()

    private var currentPage = 1
    private var hasNextPage = true

    init {
        loadFunds()
    }

    fun handleEvent(event: ExploreFdEvent) {
        when (event) {
            is ExploreFdEvent.OnSearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                loadFunds() // Reload on search
            }
            is ExploreFdEvent.OnFilterChipClicked -> {
                _uiState.update { it.copy(selectedFilter = event.filter) }
                loadFunds()
            }
            is ExploreFdEvent.OnFundItemClicked -> {
                sendEffect(ExploreFdEffect.NavigateToFdDetails(event.fundItem.id))
            }
            ExploreFdEvent.OnFilterMenuClicked -> {
                TODO("Handle filter menu")
            }
            ExploreFdEvent.OnSortDropdownClicked -> {
                TODO("Handle sort dropdown")
            }
            ExploreFdEvent.OnLoadMoreClicked -> {
                loadNext()
            }
            ExploreFdEvent.OnBackClicked -> {
                sendEffect(ExploreFdEffect.NavigateBack)
            }
        }
    }

    private fun loadFunds() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getFDSearchResult(
                page = 1,
                limit = 30,
                search = _uiState.value.searchQuery
            )
                .onSuccess { data ->
                    currentPage = data.page
                    hasNextPage = data.hasNextPage
                    _uiState.update { 
                        it.copy(
                            fundsList = data.items,
                            totalFundsCount = data.totalItems.toString(),
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

    private fun loadNext() {
        if (!hasNextPage || _uiState.value.isLoadingNext) return

        _uiState.update { it.copy(isLoadingNext = true) }
        viewModelScope.launch {
            val nextPage = currentPage + 1
            getFDSearchResult(
                page = nextPage,
                limit = 30,
                search = _uiState.value.searchQuery
            )
                .onSuccess { data ->
                    currentPage = data.page
                    hasNextPage = data.hasNextPage
                    _uiState.update { 
                        it.copy(
                            fundsList = it.fundsList + data.items,
                            isLoadingNext = false
                        ) 
                    }
                }
                .onError { error ->
                    _uiState.update { it.copy(isLoadingNext = false, errorMessage = error.message) }
                }
        }
    }

    private fun sendEffect(effect: ExploreFdEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
