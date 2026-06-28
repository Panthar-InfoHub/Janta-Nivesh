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
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.CancelSipOrderUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.ExportSoaReportUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.DownloadPdfByUrlUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens.RedemptionInputType
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens.RedemptionType
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.LoadingState
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.FullRedemptionRequestDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.PartialRedemptionRequestDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.RedeemFullFundUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.RedeemPartialFundUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.SIPDetailsDomain

sealed interface MFPortfolioSideEffects{
    data class openRedeemptionUrl(val url: String): MFPortfolioSideEffects
    data object OrderCancelled : MFPortfolioSideEffects
}
class MFPortfolioDetailsViewModel(
    private val partialRedemptionUseCase: RedeemPartialFundUseCase,
    private val redeemFullFundUseCase: RedeemFullFundUseCase,
    private val soaReportUseCase: ExportSoaReportUseCase,
    private val downloadPdfByUrlUseCase: DownloadPdfByUrlUseCase,
    private val cancelLumpSumOrderUseCase: CancelLumpSumOrderUseCase,
    private val cancelSipOrderUseCase: CancelSipOrderUseCase
): ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Success)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _sideEffects = MutableSharedFlow<MFPortfolioSideEffects>()
    val sideEffects = _sideEffects.asSharedFlow()

    private val _sipDetails = MutableStateFlow<SIPDetailsDomain?>(null)
    val sipDetails: StateFlow<SIPDetailsDomain?> = _sipDetails.asStateFlow()

    private val _showRedemptionSheet = MutableStateFlow(false)
    val showRedemptionSheet = _showRedemptionSheet.asStateFlow()

    private val _selectedRedemptionType = MutableStateFlow(RedemptionType.PARTIAL)
    val selectedRedemptionType = _selectedRedemptionType.asStateFlow()

    private val _selectedInputType = MutableStateFlow(RedemptionInputType.UNITS)
    val selectedInputType = _selectedInputType.asStateFlow()

    private val _redemptionUnits = MutableStateFlow("")
    val redemptionUnits = _redemptionUnits.asStateFlow()

    private val _redemptionAmount = MutableStateFlow("")
    val redemptionAmount = _redemptionAmount.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting = _isSubmitting.asStateFlow()

    private val _soaDownloading = MutableStateFlow(false)
    val soaDownloading = _soaDownloading.asStateFlow()


    fun onRedemptionTypeChange(type: RedemptionType) {
        _selectedRedemptionType.value = type
    }

    fun onInputTypeChange(type: RedemptionInputType) {
        _selectedInputType.value = type
    }

    fun onUnitsChange(units: String) {
        _redemptionUnits.value = units
    }

    fun onAmountChange(amount: String) {
        _redemptionAmount.value = amount
    }

    fun onDismissRedemptionSheet() {
        _showRedemptionSheet.value = false
    }

    fun onShowRedemptionSheet() {
        _showRedemptionSheet.value = true
    }

    fun submitRedemption(
        schemeId: Int,
        folioNo: String,
    ) {
        viewModelScope.launch {
            _isSubmitting.value = true
           when(_selectedRedemptionType.value){
               RedemptionType.FULL ->{
                   redeemFullFundUseCase(
                       data = FullRedemptionRequestDto(
                           schemeId = schemeId,
                           folioNo = folioNo
                       )
                   )
                       .onSuccess {url->
                           _sideEffects.emit(MFPortfolioSideEffects.openRedeemptionUrl(url))
                       }
                       .onError {
                           SnackBarController.showError(it.message)
                       }
               }
               RedemptionType.PARTIAL -> {

                   val units = _redemptionUnits.value.toDoubleOrNull()
                   val amount = _redemptionAmount.value.toIntOrNull()

                   when (_selectedInputType.value) {

                       RedemptionInputType.UNITS -> {
                           if (units == null) {
                               SnackBarController.showError(
                                   "Enter valid redemption units"
                               )
                               _isSubmitting.value = false
                               return@launch
                           }
                       }

                       RedemptionInputType.AMOUNT -> {
                           if (amount == null) {
                               SnackBarController.showError(
                                   "Enter valid redemption amount"
                               )
                               _isSubmitting.value = false
                               return@launch
                           }
                       }
                   }

                   partialRedemptionUseCase(
                       data = PartialRedemptionRequestDto(
                           schemeId = schemeId,
                           folioNo = folioNo,
                           redemptionUnits = if (
                               _selectedInputType.value == RedemptionInputType.UNITS
                           ) {
                               units
                           } else {
                               null
                           },
                           redemptionAmount = if (
                               _selectedInputType.value == RedemptionInputType.AMOUNT
                           ) {
                               amount
                           } else {
                               null
                           },
                       )
                   )
                       .onSuccess { url ->
                           _sideEffects.emit(
                               MFPortfolioSideEffects.openRedeemptionUrl(url)
                           )
                       }
                       .onError {
                           SnackBarController.showError(it.message)
                       }
               }
           }
            _isSubmitting.value = false
            _showRedemptionSheet.value = false
        }
    }
    fun downloadSOA(
        folio: String,
    ){
        viewModelScope.launch {
            _soaDownloading.value = true
            soaReportUseCase(
                folio = folio,
            )
                .onSuccess { url ->
                    downloadPdfByUrlUseCase(
                        url = url,
                        fileName = "SOA_$folio.pdf",
                        onSuccess = {
                            _soaDownloading.value = false
                            viewModelScope.launch{
                                SnackBarController.showSuccess("Statement downloaded successfully")
                            }                        },
                        onFailure = {
                            _soaDownloading.value = false
                            viewModelScope.launch{
                                SnackBarController.showError("Failed to download statement")
                            }                        }
                    )
                }
                .onError { 
                    _soaDownloading.value = false
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

    fun cancelSipOrder(xsipRegNo: String) {
        viewModelScope.launch {
            _isSubmitting.value = true
            cancelSipOrderUseCase(xsipRegNo)
                .onSuccess {
                    _isSubmitting.value = false
                    SnackBarController.showSuccess("SIP cancelled successfully")
                    _sideEffects.emit(MFPortfolioSideEffects.OrderCancelled)
                }
                .onError {
                    _isSubmitting.value = false
                    SnackBarController.showError(it.message)
                }
        }
    }
}
