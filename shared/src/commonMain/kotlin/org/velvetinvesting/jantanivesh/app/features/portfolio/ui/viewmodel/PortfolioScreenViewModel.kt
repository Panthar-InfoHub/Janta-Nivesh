package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.presentation.portfolio.models.SelectedPortfolio
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PendingOrderDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.CancelLumpSumOrderUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.CancelSipOrderUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.DownloadPdfByUrlUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.ExportCapitalReportUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.ExportPortfolioReportUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.ExportTaxReportUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.GetPendingOrdersUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.GetPortfolioUseCase

class PortfolioScreenViewModel(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val exportCapitalReportUseCase: ExportCapitalReportUseCase,
    private val exportTaxReportUseCase: ExportTaxReportUseCase,
    private val exportPortfolioReportUseCase: ExportPortfolioReportUseCase,
    private val downloadPdfByUrlUseCase: DownloadPdfByUrlUseCase,
    private val getPendingOrdersUseCase: GetPendingOrdersUseCase,
    private val cancelLumpSumOrderUseCase: CancelLumpSumOrderUseCase,
    private val cancelSipOrderUseCase: CancelSipOrderUseCase
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

    init {
        loadPortfolio()
        loadPendingOrders()
    }

    fun refresh(){
        loadPortfolio()
        loadPendingOrders()
    }

    fun loadPendingOrders() {
        viewModelScope.launch {
            getPendingOrdersUseCase()
                .onSuccess {
                    _pendingOrders.value = it
                }
        }
    }


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
            exportPortfolioReportUseCase()
                .onSuccess { url ->
                    downloadPdfByUrlUseCase(
                        url = url,
                        fileName = "Portfolio_Report",
                        onSuccess = {
                            _isExportingPortfolio.value = false
                            viewModelScope.launch {
                                SnackBarController.showSuccess("Portfolio report downloaded successfully")
                            }
                        },
                        onFailure = {
                            _isExportingPortfolio.value = false
                            viewModelScope.launch {
                                SnackBarController.showError("Failed to download portfolio report")
                            }
                        }
                    )
                }
                .onError {
                    _isExportingPortfolio.value = false
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

