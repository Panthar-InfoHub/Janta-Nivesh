package org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels

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
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MutualFundPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PendingOrderDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.GetPortfolioUseCase
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionGroup
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionHistoryItem
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionStatus
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionType
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.toTransactionItem

data class TransactionHistoryUiState(
    val isLoading: Boolean = false,
    val selectedTab: TransactionType = TransactionType.MUTUAL_FUND,
    val selectedFilter: TransactionFilter = TransactionFilter.ALL,
    val searchQuery: String = "",
    val transactionGroups: List<TransactionGroup> = emptyList(),
    val error: String? = null
)

enum class TransactionFilter {
    ALL, PENDING, COMPLETE, FAILED
}

sealed interface TransactionHistoryEvent {
    data object LoadData : TransactionHistoryEvent
    data class OnTabSelected(val type: TransactionType) : TransactionHistoryEvent
    data class OnFilterSelected(val filter: TransactionFilter) : TransactionHistoryEvent
    data class OnSearchQueryChanged(val query: String) : TransactionHistoryEvent
    data object OnBackClicked : TransactionHistoryEvent
}

sealed interface TransactionHistoryEffect {
    data object NavigateBack : TransactionHistoryEffect
}

/**
 * Transaction history, built from the same portfolio reads the portfolio screen uses.
 *
 * There is no transaction endpoint, so the rows come from what the portfolio does report: pending
 * orders and mutual-fund holdings on one tab, fixed deposits on the other. Both reads happen once
 * and the tab, filter and search then work on what is already in memory, so switching a tab does
 * not go back to the network.
 */
class TransactionHistoryViewModel(
    private val getPortfolioUseCase: GetPortfolioUseCase,
//    private val getPendingOrdersUseCase: GetPendingOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionHistoryUiState())
    val uiState: StateFlow<TransactionHistoryUiState> = _uiState.asStateFlow()

    private val _effect = Channel<TransactionHistoryEffect>()
    val effect = _effect.receiveAsFlow()

    private var pendingOrders: List<PendingOrderDomain> = emptyList()
    private var mutualFunds: List<MutualFundPortfolioDomain> = emptyList()
    private var fixedDeposits: List<FixedDepositPortfolioDomain> = emptyList()

    init {
        loadData()
    }

    fun handleEvent(event: TransactionHistoryEvent) {
        when (event) {
            TransactionHistoryEvent.LoadData -> loadData()

            is TransactionHistoryEvent.OnTabSelected ->
                _uiState.update { it.copy(selectedTab = event.type).withGroups() }

            is TransactionHistoryEvent.OnFilterSelected ->
                _uiState.update { it.copy(selectedFilter = event.filter).withGroups() }

            is TransactionHistoryEvent.OnSearchQueryChanged ->
                _uiState.update { it.copy(searchQuery = event.query).withGroups() }

            TransactionHistoryEvent.OnBackClicked ->
                viewModelScope.launch { _effect.send(TransactionHistoryEffect.NavigateBack) }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Pending orders are supplementary: the tab is still worth showing without them, so a
            // failure there is left silent rather than blanking the holdings behind an error.
//            getPendingOrdersUseCase().onSuccess { pendingOrders = it }

            getPortfolioUseCase()
                .onSuccess { portfolio ->
                    mutualFunds = portfolio.mutualFunds
                    fixedDeposits = portfolio.fixedDeposits
                    _uiState.update { it.copy(isLoading = false, error = null).withGroups() }
                }
                .onError { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    /** Rebuilds the visible rows from the cached reads for the current tab, filter and query. */
    private fun TransactionHistoryUiState.withGroups(): TransactionHistoryUiState {
        val groups = when (selectedTab) {
            TransactionType.MUTUAL_FUND -> listOf(
                "PENDING ORDERS" to pendingOrders.map { it.toTransactionItem() },
                "HOLDINGS" to mutualFunds.map { it.toTransactionItem() }
            )

            TransactionType.FIXED_DEPOSIT -> listOf(
                "FIXED DEPOSITS" to fixedDeposits.map { it.toTransactionItem() }
            )
        }

        val visible = groups.mapNotNull { (header, items) ->
            val kept = items.filter { it.matchesFilter(selectedFilter) && it.matches(searchQuery) }
            if (kept.isEmpty()) null else TransactionGroup(header, kept)
        }

        return copy(transactionGroups = visible)
    }

    /**
     * A row the source reports no state for — a settled holding — can only be answered for under
     * "All"; claiming it as complete would be reading something into the payload.
     */
    private fun TransactionHistoryItem.matchesFilter(filter: TransactionFilter): Boolean =
        when (filter) {
            TransactionFilter.ALL -> true
            TransactionFilter.PENDING -> status == TransactionStatus.PENDING
            TransactionFilter.COMPLETE -> status == TransactionStatus.SUCCESSFUL
            TransactionFilter.FAILED -> status == TransactionStatus.FAILED
        }

    private fun TransactionHistoryItem.matches(query: String): Boolean {
        if (query.isBlank()) return true
        return listOf(title, subtitle, amount, date, statusLabel)
            .any { it.contains(query.trim(), ignoreCase = true) }
    }
}
