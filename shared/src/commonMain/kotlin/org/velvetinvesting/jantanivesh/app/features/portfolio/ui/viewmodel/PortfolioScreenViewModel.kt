package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.presentation.portfolio.models.SelectedPortfolio
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderDomain
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderFilter
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.matches
import org.velvetinvesting.jantanivesh.app.features.orders.domain.usecase.GetOrdersUseCase
import org.velvetinvesting.jantanivesh.app.features.orders.ui.viewmodel.MyOrdersUiState
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.ActiveSipDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PendingOrderDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.CancelLumpSumOrderUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.CancelSipOrderUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.DownloadPdfByUrlUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.ExportCapitalReportUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.ExportPortfolioReportUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.ExportTaxReportUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.GetActiveSipsUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.GetPendingOrdersUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.GetPortfolioUseCase

class PortfolioScreenViewModel(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val exportCapitalReportUseCase: ExportCapitalReportUseCase,
    private val exportTaxReportUseCase: ExportTaxReportUseCase,
    private val exportPortfolioReportUseCase: ExportPortfolioReportUseCase,
    private val downloadPdfByUrlUseCase: DownloadPdfByUrlUseCase,
    private val getPendingOrdersUseCase: GetPendingOrdersUseCase,
    private val getActiveSipsUseCase: GetActiveSipsUseCase,
    private val cancelLumpSumOrderUseCase: CancelLumpSumOrderUseCase,
    private val cancelSipOrderUseCase: CancelSipOrderUseCase,
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {



    private val _selectedTab =
        MutableStateFlow<SelectedPortfolio>(SelectedPortfolio.MutualFunds)
    val selectedTab = _selectedTab.asStateFlow()

    private val _loadingState =
        MutableStateFlow<UiState<PortfolioDomain>>(UiState.Loading)
    val uiState = _loadingState.asStateFlow()

    private val _isExportingCapital = MutableStateFlow(false)
    val isExportingCapital = _isExportingCapital.asStateFlow()

    private val _isExportingTax = MutableStateFlow(false)
    val isExportingTax = _isExportingTax.asStateFlow()

    private val _isExportingPortfolio = MutableStateFlow(false)
    val isExportingPortfolio = _isExportingPortfolio.asStateFlow()

    private val _pendingOrders = MutableStateFlow<List<PendingOrderDomain>>(emptyList())
    val pendingOrders = _pendingOrders.asStateFlow()

    private val _activeSips = MutableStateFlow(ActiveSipDomain.EMPTY)
    val activeSips = _activeSips.asStateFlow()

    /**
     * True only while the first read is in flight, so a refresh does not blank a tab that
     * already has SIPs on it.
     */
    private val _isLoadingActiveSips = MutableStateFlow(true)
    val isLoadingActiveSips = _isLoadingActiveSips.asStateFlow()

    /**
     * The Orders tab reuses My Orders' own state, so the two render from the same shape and the
     * shared cards can be handed it unchanged.
     */
    private val _ordersState = MutableStateFlow(MyOrdersUiState())
    val ordersState = _ordersState.asStateFlow()

    /** Every page read so far, unfiltered — the chips narrow this in memory, as My Orders does. */
    private var allOrders: List<OrderDomain> = emptyList()
    private var ordersPage = 1

    init {
        refresh()
    }

    /**
     * Every read the screen shows, in parallel.
     *
     * The three endpoints answer different questions — holdings, unpaid orders, standing SIPs —
     * and none depends on another, so they are launched together rather than chained: the
     * portfolio arrives as soon as it is ready instead of waiting on the SIP call.
     */
    fun refresh() {
        loadPortfolio()
        loadPendingOrders()
        loadActiveSips()
        loadOrders()
    }

    fun loadPendingOrders() {
        viewModelScope.launch {
            getPendingOrdersUseCase()
                .onSuccess {
                    _pendingOrders.value = it
                }
        }
    }

    /**
     * The SIP tab is supplementary, so a failure here is left silent: it empties its own tab and
     * leaves the portfolio behind it untouched, rather than putting the whole screen in error.
     */
    fun loadActiveSips() {
        viewModelScope.launch {
            getActiveSipsUseCase()
                .onSuccess { _activeSips.value = it }
                .onError { _activeSips.value = ActiveSipDomain.EMPTY }
            _isLoadingActiveSips.value = false
        }
    }


    /**
     * The order book behind the Orders tab. Read whole rather than per-chip: the endpoint's
     * `state` filter only knows SUCCESSFUL and ACTIVE, so pending and failed have no server-side
     * equivalent and the four chips are answered in memory instead.
     */
    fun loadOrders() {
        viewModelScope.launch {
            _ordersState.update { it.copy(isLoading = true, error = null) }
            ordersPage = 1

            getOrdersUseCase(page = 1)
                .onSuccess { data ->
                    allOrders = data.items
                    _ordersState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            hasNextPage = data.hasNextPage,
                            totalOrders = data.totalItems
                        ).withVisibleOrders()
                    }
                }
                .onError { error ->
                    _ordersState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun loadNextOrders() {
        val state = _ordersState.value
        if (state.isLoading || state.isLoadingNext || !state.hasNextPage) return

        viewModelScope.launch {
            _ordersState.update { it.copy(isLoadingNext = true) }

            getOrdersUseCase(page = ordersPage + 1)
                .onSuccess { data ->
                    ordersPage = data.page
                    allOrders = allOrders + data.items
                    _ordersState.update {
                        it.copy(
                            isLoadingNext = false,
                            hasNextPage = data.hasNextPage,
                            totalOrders = data.totalItems
                        ).withVisibleOrders()
                    }
                }
                // A failed next page leaves what is already listed alone: the user can scroll
                // again to retry, and blanking the list would lose the pages that did load.
                .onError {
                    _ordersState.update { it.copy(isLoadingNext = false, hasNextPage = false) }
                }
        }
    }

    fun onOrderFilterSelected(filter: OrderFilter) {
        _ordersState.update { it.copy(selectedFilter = filter).withVisibleOrders() }
    }

    private fun MyOrdersUiState.withVisibleOrders(): MyOrdersUiState =
        copy(orders = allOrders.filter { it.matches(selectedFilter) })

     fun loadPortfolio() {
        viewModelScope.launch {
            _loadingState.value= UiState.Loading
            getPortfolioUseCase()
                .onSuccess {
                    _loadingState.value= UiState.Success(it)
                }
                .onError {
                    _loadingState.value= UiState.Error(it.message)
                }
        }
    }

    fun onTabSelected(tab: SelectedPortfolio) {
        _selectedTab.value = tab
    }

    fun downloadCapitalReport() {
        viewModelScope.launch {
            _isExportingCapital.value = true
            exportCapitalReportUseCase()
                .onSuccess { url ->
                    downloadPdfByUrlUseCase(
                        url = url,
                        fileName = "Capital_Report",
                        onSuccess = {
                            _isExportingCapital.value = false
                            viewModelScope.launch {
                                SnackBarController.showSuccess("Capital report downloaded successfully")
                            }
                        },
                        onFailure = {
                            _isExportingCapital.value = false
                            viewModelScope.launch {
                                SnackBarController.showError("Failed to download capital report")
                            }
                        }
                    )
                }
                .onError {
                    _isExportingCapital.value = false
                    SnackBarController.showError(it.message)
                }
        }
    }

    fun downloadTaxReport() {
        val year = DateTimeUtils.getCurrentYear()
        viewModelScope.launch {
            _isExportingTax.value = true
            exportTaxReportUseCase(year = year)
                .onSuccess { url ->
                    downloadPdfByUrlUseCase(
                        url = url,
                        fileName = "Tax_Report_$year",
                        onSuccess = {
                            _isExportingTax.value = false
                            viewModelScope.launch {
                                SnackBarController.showSuccess("Tax report downloaded successfully")
                            }
                        },
                        onFailure = {
                            _isExportingTax.value = false
                            viewModelScope.launch {
                                SnackBarController.showError("Failed to download tax report")
                            }
                        }
                    )
                }
                .onError {
                    _isExportingTax.value = false
                    SnackBarController.showError(it.message)
                }
        }
    }

    fun downloadPortfolioReport() {
        viewModelScope.launch {
            _isExportingPortfolio.value = true
            exportPortfolioReportUseCase(
                onSuccess = {
                    _isExportingPortfolio.value = false
                    viewModelScope.launch { SnackBarController.showSuccess("Report Downloaded") }
                },
                onFailed = {
                    _isExportingPortfolio.value = false
                    viewModelScope.launch { SnackBarController.showError(it) }
                }
            ).onError {
                _isExportingPortfolio.value
                SnackBarController.showError(it.message)
            }
        }
    }

    fun cancelPendingOrder(order: PendingOrderDomain) {
        viewModelScope.launch {

            val cachedState = uiState.value
            _loadingState.value = UiState.Loading

            val result = when (order.type.uppercase()) {

                "LUMPSUM" -> cancelLumpSumOrderUseCase(order.id)

                "SIP" -> cancelSipOrderUseCase(order.id)

                else -> {
                    _loadingState.value = cachedState
                    SnackBarController.showError("Unsupported order type")
                    return@launch
                }
            }

            result
                .onSuccess {

                    SnackBarController.showSuccess(
                        "${order.type} order cancelled successfully"
                    )

                    refresh()
                }

                .onError {

                    _loadingState.value = cachedState

                    SnackBarController.showError(it.message)
                }
        }
    }

}

