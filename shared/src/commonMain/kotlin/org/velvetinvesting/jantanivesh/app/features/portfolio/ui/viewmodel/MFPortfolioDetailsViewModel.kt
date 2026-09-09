package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.CancelLumpSumOrderUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.CancelPurchasePlanUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.models.SipCancellationReason
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.DownloadFolioSpecificReportUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.DownloadPdfByUrlUseCase
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.LoadingState
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.SIPDetailsDomain

sealed interface MFPortfolioSideEffects{
    data object OrderCancelled : MFPortfolioSideEffects
}

/**
 * Backs the order-details screen, which renders entirely from its route: all that is left here
 * is the statement download and cancelling the order. Redeeming moved to its own screen and its
 * own view model.
 */
class MFPortfolioDetailsViewModel(
    private val downloadFolioSpecificReportUseCase: DownloadFolioSpecificReportUseCase,
    private val downloadPdfByUrlUseCase: DownloadPdfByUrlUseCase,
    private val cancelLumpSumOrderUseCase: CancelLumpSumOrderUseCase,
    private val cancelPurchasePlanUseCase: CancelPurchasePlanUseCase
): ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Success)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _sideEffects = MutableSharedFlow<MFPortfolioSideEffects>()
    val sideEffects = _sideEffects.asSharedFlow()

    private val _sipDetails = MutableStateFlow<SIPDetailsDomain?>(null)
    val sipDetails: StateFlow<SIPDetailsDomain?> = _sipDetails.asStateFlow()

    /** Raised while a cancellation is in flight. */
    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting = _isSubmitting.asStateFlow()

    /**
     * The reason sheet. The gateway will not take a cancellation without one of its codes, so
     * the reason is collected before the request rather than after a bare "are you sure?".
     */
    private val _showCancelSheet = MutableStateFlow(false)
    val showCancelSheet = _showCancelSheet.asStateFlow()

    private val _selectedCancelReason = MutableStateFlow<SipCancellationReason?>(null)
    val selectedCancelReason = _selectedCancelReason.asStateFlow()

    private val _reportDownloading = MutableStateFlow(false)
    val soaDownloading = _reportDownloading.asStateFlow()


    fun downloadSOA(
        folio: String,
    ){
        viewModelScope.launch {
            _reportDownloading.value = true
            downloadFolioSpecificReportUseCase(
                folio = folio,
                onSuccess = {
                    _reportDownloading.value = false
                    viewModelScope.launch { SnackBarController.showSuccess("Report Downloaded") }
                },
                onFailed = {
                    _reportDownloading.value = false
                    viewModelScope.launch { SnackBarController.showError(it) }
                }
            )
                .onError { 
                    _reportDownloading.value = false
                    SnackBarController.showError(it.message)
                }
        }
    }

    fun cancelLumpSumOrder(orderId: String) {
        viewModelScope.launch {
            _isSubmitting.value = true
            cancelLumpSumOrderUseCase(orderId)
                .onSuccess {
                    _isSubmitting.value = false
                    SnackBarController.showSuccess("Order cancelled successfully")
                    _sideEffects.emit(MFPortfolioSideEffects.OrderCancelled)
                }
                .onError {
                    _isSubmitting.value = false
                    SnackBarController.showError(it.message)
                }
        }
    }

    fun onShowCancelSheet() {
        _showCancelSheet.value = true
    }

    fun onDismissCancelSheet() {
        _showCancelSheet.value = false
        // Reopening starts from nothing selected: a reason carried over from a sheet the user
        // backed out of is not a choice they made.
        _selectedCancelReason.value = null
    }

    fun onCancelReasonSelected(reason: SipCancellationReason) {
        _selectedCancelReason.value = reason
    }

    fun cancelSipPlan(planId: String) {
        val reason = _selectedCancelReason.value ?: return

        viewModelScope.launch {
            _isSubmitting.value = true
            cancelPurchasePlanUseCase(planId, reason.code)
                .onSuccess {
                    _isSubmitting.value = false
                    _showCancelSheet.value = false
                    _selectedCancelReason.value = null
                    SnackBarController.showSuccess("SIP cancelled successfully")
                    _sideEffects.emit(MFPortfolioSideEffects.OrderCancelled)
                }
                .onError {
                    // The sheet stays open on failure so the choice is not lost on a retry.
                    _isSubmitting.value = false
                    SnackBarController.showError(it.message)
                }
        }
    }
}
