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
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionGroup
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionHistoryItem
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionStatus
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionType
import org.velvetinvesting.jantanivesh.app.features.profile.domain.usecase.GetTransactionsUseCase

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

class TransactionHistoryViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionHistoryUiState())
    val uiState: StateFlow<TransactionHistoryUiState> = _uiState.asStateFlow()

    private val _effect = Channel<TransactionHistoryEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadMockData()
    }

    fun handleEvent(event: TransactionHistoryEvent) {
        when (event) {
            TransactionHistoryEvent.LoadData -> loadMockData()
            is TransactionHistoryEvent.OnTabSelected -> {
                _uiState.update { it.copy(selectedTab = event.type) }
                loadMockData()
            }
            is TransactionHistoryEvent.OnFilterSelected -> {
                _uiState.update { it.copy(selectedFilter = event.filter) }
                // In a real app, this might trigger a local filter or a new API call
            }
            is TransactionHistoryEvent.OnSearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }
            TransactionHistoryEvent.OnBackClicked -> {
                viewModelScope.launch { _effect.send(TransactionHistoryEffect.NavigateBack) }
            }
        }
    }

    private fun loadMockData() {
        val tab = _uiState.value.selectedTab
        val mockTransactions = if (tab == TransactionType.MUTUAL_FUND) {
            listOf(
                TransactionGroup(
                    dateHeader = "TODAY",
                    transactions = listOf(
                        TransactionHistoryItem(
                            id = "1",
                            title = "HDFC Small Cap Fund",
                            subtitle = "SIP • Direct Growth",
                            amount = "₹5,000",
                            date = "12 Oct 2023",
                            status = TransactionStatus.SUCCESSFUL,
                            type = TransactionType.MUTUAL_FUND
                        ),
                        TransactionHistoryItem(
                            id = "2",
                            title = "ICICI Pru Bluechip",
                            subtitle = "Lumpsum • Direct Growth",
                            amount = "₹25,000",
                            date = "14 Oct 2023",
                            status = TransactionStatus.PENDING,
                            type = TransactionType.MUTUAL_FUND
                        )
                    )
                ),
                TransactionGroup(
                    dateHeader = "TODAY",
                    transactions = listOf(
                        TransactionHistoryItem(
                            id = "3",
                            title = "Axis Midcap Fund",
                            subtitle = "SIP • Direct Growth",
                            amount = "₹3,000",
                            date = "10 Oct 2023",
                            status = TransactionStatus.FAILED,
                            type = TransactionType.MUTUAL_FUND
                        ),
                        TransactionHistoryItem(
                            id = "4",
                            title = "SBI Liquid Fund",
                            subtitle = "Withdrawal",
                            amount = "- ₹10,000",
                            date = "05 Oct 2023",
                            status = TransactionStatus.SUCCESSFUL,
                            type = TransactionType.MUTUAL_FUND
                        )
                    )
                )
            )
        } else {
            listOf(
                TransactionGroup(
                    dateHeader = "TODAY",
                    transactions = listOf(
                        TransactionHistoryItem(
                            id = "5",
                            title = "SBI Fixed Deposit",
                            subtitle = "FD Booking • 7.10% p.a.",
                            amount = "₹5,00,000",
                            date = "Today",
                            status = TransactionStatus.SUCCESSFUL,
                            type = TransactionType.FIXED_DEPOSIT
                        ),
                        TransactionHistoryItem(
                            id = "6",
                            title = "HDFC Bank FD",
                            subtitle = "Maturity Processing",
                            amount = "₹1,50,000",
                            date = "Today",
                            status = TransactionStatus.PENDING,
                            type = TransactionType.FIXED_DEPOSIT
                        )
                    )
                ),
                TransactionGroup(
                    dateHeader = "YESTERDAY, 12 OCT",
                    transactions = listOf(
                        TransactionHistoryItem(
                            id = "7",
                            title = "Bajaj Finance FD",
                            subtitle = "Interest Credit",
                            amount = "+₹12,450",
                            date = "12 Oct 2023",
                            status = TransactionStatus.SUCCESSFUL,
                            type = TransactionType.FIXED_DEPOSIT,
                            isCredit = true
                        ),
                        TransactionHistoryItem(
                            id = "8",
                            title = "ICICI Bank FD",
                            subtitle = "FD Booking - Auto Pay Failed",
                            amount = "₹2,00,000",
                            date = "12 Oct 2023",
                            status = TransactionStatus.FAILED,
                            type = TransactionType.FIXED_DEPOSIT
                        )
                    )
                ),
                TransactionGroup(
                    dateHeader = "05 OCT 2023",
                    transactions = listOf(
                        TransactionHistoryItem(
                            id = "9",
                            title = "Axis Bank FD",
                            subtitle = "FD Booking • 6.80% p.a.",
                            amount = "₹1,00,000",
                            date = "05 Oct 2023",
                            status = TransactionStatus.SUCCESSFUL,
                            type = TransactionType.FIXED_DEPOSIT
                        )
                    )
                )
            )
        }

        _uiState.update { it.copy(transactionGroups = mockTransactions, isLoading = false) }
    }
}
