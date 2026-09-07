package org.velvetinvesting.jantanivesh.app.features.orders.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderDomain
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderFilter
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.matches
import org.velvetinvesting.jantanivesh.app.features.orders.domain.usecase.GetOrdersUseCase

data class MyOrdersUiState(
    val isLoading: Boolean = false,
    val isLoadingNext: Boolean = false,
    val hasNextPage: Boolean = false,
    val selectedFilter: OrderFilter = OrderFilter.ALL,
    /** Already narrowed to [selectedFilter] — the screen renders this list as it stands. */
    val orders: List<OrderDomain> = emptyList(),
    val totalOrders: Int = 0,
    val error: String? = null
)

sealed interface MyOrdersEvent {
    data object Retry : MyOrdersEvent
    data object LoadNext : MyOrdersEvent
    data class OnFilterSelected(val filter: OrderFilter) : MyOrdersEvent
    data class OnOrderClicked(val order: OrderDomain) : MyOrdersEvent
    data object OnBackClicked : MyOrdersEvent
}

sealed interface MyOrdersEffect {
    data object NavigateBack : MyOrdersEffect
    data class NavigateToDetails(val order: OrderDomain) : MyOrdersEffect
}

/**
 * The order listing.
 *
 * Filtering happens in memory rather than over the wire: the endpoint's `state` parameter only
 * narrows to SUCCESSFUL or ACTIVE, so pending and failed have no server-side equivalent and
 * switching a chip would otherwise mean a read that cannot answer the question. One read of the
 * whole book therefore backs all four chips.
 */
class MyOrdersViewModel(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyOrdersUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<MyOrdersEffect>()
    val effect = _effect.receiveAsFlow()

    private var allOrders: List<OrderDomain> = emptyList()
    private var currentPage = 1

    init {
        loadOrders()
    }

    fun handleEvent(event: MyOrdersEvent) {
        when (event) {
            MyOrdersEvent.Retry -> loadOrders()

            MyOrdersEvent.LoadNext -> loadNext()

            is MyOrdersEvent.OnFilterSelected ->
                _uiState.update { it.copy(selectedFilter = event.filter).withVisibleOrders() }

            is MyOrdersEvent.OnOrderClicked -> viewModelScope.launch {
                _effect.send(MyOrdersEffect.NavigateToDetails(event.order))
            }

            MyOrdersEvent.OnBackClicked -> viewModelScope.launch {
                _effect.send(MyOrdersEffect.NavigateBack)
            }
        }
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            currentPage = 1

            when (val response = getOrdersUseCase(page = 1)) {
                is NetworkResponse.Success -> {
                    allOrders = response.data.items
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            hasNextPage = response.data.hasNextPage,
                            totalOrders = response.data.totalItems
                        ).withVisibleOrders()
                    }
                }

                is NetworkResponse.Error -> _uiState.update {
                    it.copy(isLoading = false, error = response.error.message)
                }
            }
        }
    }

    private fun loadNext() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingNext || !state.hasNextPage) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingNext = true) }

            when (val response = getOrdersUseCase(page = currentPage + 1)) {
                is NetworkResponse.Success -> {
                    currentPage = response.data.page
                    allOrders = allOrders + response.data.items
                    _uiState.update {
                        it.copy(
                            isLoadingNext = false,
                            hasNextPage = response.data.hasNextPage,
                            totalOrders = response.data.totalItems
                        ).withVisibleOrders()
                    }
                }

                // A failed next page leaves what is already listed alone: the user can scroll
                // again to retry, and blanking the list would lose the pages that did load.
                is NetworkResponse.Error -> _uiState.update {
                    it.copy(isLoadingNext = false, hasNextPage = false)
                }
            }
        }
    }

    private fun MyOrdersUiState.withVisibleOrders(): MyOrdersUiState =
        copy(orders = allOrders.filter { it.matches(selectedFilter) })
}
