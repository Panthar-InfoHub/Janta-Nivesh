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
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FixedDepositDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.GetFixedDepositsSearchResultUseCase

import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.InvestmentFilter
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.LabelFilter
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.ReturnYears
import org.velvetinvesting.jantanivesh.app.features.fd.domain.utils.FDFilterIds
import org.velvetinvesting.jantanivesh.app.features.fd.domain.utils.createInitialFDFilters
import org.velvetinvesting.jantanivesh.app.features.fd.domain.utils.getActiveFilterLabel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.ExploreFdEffect.*

data class ExploreFdUiState(
    val searchQuery: String = "",
    val totalFundsCount: String = "0",
    val selectedFilter: String = "Public Bank",
    val sortOptions: List<ReturnYears> = listOf(ReturnYears.Year1, ReturnYears.Year2, ReturnYears.Year3, ReturnYears.Year4, ReturnYears.Year5),
    val fundsList: List<FixedDepositDomain> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingNext: Boolean = false,
    val errorMessage: String? = null,
    val filterState: InvestmentFilter = createInitialFDFilters(),
    val selectedFilterLabel: String = "All FDs",
    val showFilterScreen: Boolean = false,
    val hasNextPage: Boolean = false,
)

sealed interface ExploreFdEvent {
    data class OnSortOptionClicked(val sortOption: ReturnYears) : ExploreFdEvent
    data class OnSearchQueryChanged(val query: String) : ExploreFdEvent

    data object OnSearchClick: ExploreFdEvent
    data class OnFilterChipClicked(val filter: LabelFilter) : ExploreFdEvent
    data class OnApplyFilter(val filter: InvestmentFilter) : ExploreFdEvent
    data object OnClearFilter : ExploreFdEvent
    data class OnFundItemClicked(val fundItem: FixedDepositDomain) : ExploreFdEvent
    data object OnFilterMenuClicked : ExploreFdEvent
    data class OnSortDropdownClicked(val option: ReturnYears) : ExploreFdEvent
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

    init {
        loadFunds()
    }

    fun handleEvent(event: ExploreFdEvent) {
        when (event) {
            is ExploreFdEvent.OnSearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }
            is ExploreFdEvent.OnFilterChipClicked -> {
                // Simplified chip logic from temp
                _uiState.update { it.copy(selectedFilter = event.filter.title) }
                loadFunds()
            }
            is ExploreFdEvent.OnApplyFilter -> {
                applyFilter(event.filter)
            }
            ExploreFdEvent.OnClearFilter -> {
                clearFilter()
            }
            is ExploreFdEvent.OnFundItemClicked -> {
                sendEffect(NavigateToFdDetails(event.fundItem.id))
            }
            is ExploreFdEvent.OnSortOptionClicked -> {
                sortByReturnDuration(event.sortOption)
            }
            is ExploreFdEvent.OnSortDropdownClicked -> {
                sortByReturnDuration(event.option)
            }
            ExploreFdEvent.OnFilterMenuClicked -> {
                _uiState.update { it.copy(showFilterScreen = !it.showFilterScreen) }
            }
            ExploreFdEvent.OnLoadMoreClicked -> {
                loadNext()
            }
            ExploreFdEvent.OnBackClicked -> {
                sendEffect(ExploreFdEffect.NavigateBack)
            }

            ExploreFdEvent.OnSearchClick -> {
                loadFunds()
            }
        }
    }

    private fun applyFilter(newFilter: InvestmentFilter) {
        _uiState.update { 
            it.copy(
                filterState = newFilter,
                selectedFilterLabel = newFilter.getActiveFilterLabel()
            )
        }
        currentPage = 1
        loadFunds()
    }

    private fun clearFilter() {
        _uiState.update { 
            it.copy(
                filterState = createInitialFDFilters(),
                selectedFilterLabel = "All FDs"
            )
        }
        currentPage = 1
        loadFunds()
    }

    private fun sortByReturnDuration(sortOption: ReturnYears){
      //  TODO("Handle sort by return duration")
    }
    private fun loadFunds() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val (tenure, payout) = getSelectedFilters()
            getFDSearchResult(
                page = 1,
                limit = 30,
                tenure = tenure,
                payoutFrequency = payout,
                search = _uiState.value.searchQuery
            )
                .onSuccess { data ->
                    currentPage = data.page
                    _uiState.update { 
                        it.copy(
                            fundsList = data.items,
                            totalFundsCount = data.totalItems.toString(),
                            isLoading = false,
                            errorMessage = null,
                            hasNextPage = data.hasNextPage
                        ) 
                    }
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
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
        if (!_uiState.value.hasNextPage || _uiState.value.isLoadingNext) return

        _uiState.update { it.copy(isLoadingNext = true) }
        viewModelScope.launch {
            val nextPage = currentPage + 1
            val (tenure, payout) = getSelectedFilters()
            getFDSearchResult(
                page = nextPage,
                limit = 30,
                tenure = tenure,
                payoutFrequency = payout,
                search = _uiState.value.searchQuery
            )
                .onSuccess { data ->
                    currentPage = data.page
                    _uiState.update {
                        it.copy(
                            fundsList = it.fundsList + data.items,
                            isLoadingNext = false,
                            hasNextPage = data.hasNextPage
                        ) 
                    }
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
                    _uiState.update { it.copy(isLoadingNext = false, errorMessage = error.message) }
                }
        }
    }

    private fun getSelectedFilters(): Pair<String?, String?> {
        val groups = _uiState.value.filterState.groups

        val tenure = groups
            .find { it.id == FDFilterIds.TENURE }
            ?.options
            ?.firstOrNull { it.isSelected }
            ?.id

        val payout = groups
            .find { it.id == FDFilterIds.PAYOUT_FREQUENCY }
            ?.options
            ?.firstOrNull { it.isSelected }
            ?.id

        return tenure to payout
    }

    private fun sendEffect(effect: ExploreFdEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
