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
import org.velvetinvesting.jantanivesh.app.features.fd.data.models.dto.PurchaseFDBodyDto
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FDDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FDTenureDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.PayoutType
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.GetFDDetailsUseCase
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.PurchaseFDUseCase

data class SetInvestmentDetailsUiState(
    val details: FDDetailsDomain? = null,
    val amount: String = "",
    val selectedTenure: FDTenureDomain? = null,
    val selectedPayoutMode: PayoutType? = null,
    val maturityAmount: String = "₹0",
    val totalInterest: String = "₹0",
    val interestRate: String = "0% p.a.",
    val maturityDate: String = "N/A",
    val isLoading: Boolean = false,
    val isPurchasing: Boolean = false,
    val errorMessage: String? = null
)

sealed interface SetInvestmentDetailsEvent {
    data class LoadDetails(val id: String) : SetInvestmentDetailsEvent
    data class OnAmountChanged(val amount: String) : SetInvestmentDetailsEvent
    data class OnTenureChanged(val tenure: FDTenureDomain) : SetInvestmentDetailsEvent
    data class OnPayoutModeChanged(val payout: PayoutType) : SetInvestmentDetailsEvent
    data object OnBackClicked : SetInvestmentDetailsEvent
    data object OnContinueClicked : SetInvestmentDetailsEvent
}

sealed interface SetInvestmentDetailsEffect {
    data object NavigateBack : SetInvestmentDetailsEffect
    data class NavigateToPurchaseUrl(val url: String) : SetInvestmentDetailsEffect
}

class SetInvestmentDetailsViewModel(
    private val getFDDetailsUseCase: GetFDDetailsUseCase,
    private val purchaseFDUseCase: PurchaseFDUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetInvestmentDetailsUiState())
    val uiState: StateFlow<SetInvestmentDetailsUiState> = _uiState.asStateFlow()

    private val _effect = Channel<SetInvestmentDetailsEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: SetInvestmentDetailsEvent) {
        when (event) {
            is SetInvestmentDetailsEvent.LoadDetails -> loadFdDetails(event.id)
            is SetInvestmentDetailsEvent.OnAmountChanged -> {
                _uiState.update { it.copy(amount = event.amount) }
                calculateReturns()
            }

            is SetInvestmentDetailsEvent.OnTenureChanged -> {
                _uiState.update { it.copy(selectedTenure = event.tenure) }
                calculateReturns()
            }

            is SetInvestmentDetailsEvent.OnPayoutModeChanged -> {
                _uiState.update { it.copy(selectedPayoutMode = event.payout) }
                TODO("filter tenures by payout frequency if needed")
            }

            SetInvestmentDetailsEvent.OnBackClicked -> {
                sendEffect(SetInvestmentDetailsEffect.NavigateBack)
            }

            SetInvestmentDetailsEvent.OnContinueClicked -> {
                purchaseFD()
            }
        }
    }

    private fun loadFdDetails(id: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getFDDetailsUseCase(id)
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            details = data,
                            amount = data.minDeposit.toString(),
                            selectedTenure = data.interestRates.find { it.isDefault }
                                ?: data.interestRates.firstOrNull(),
                            selectedPayoutMode = data.selectedPayout,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    calculateReturns()
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

    private fun calculateReturns() {
        val state = _uiState.value
        val principal = state.amount.toLongOrNull() ?: 0L
        val tenure = state.selectedTenure ?: return

        _uiState.update {
            it.copy(
                maturityAmount = "₹${principal + (principal * tenure.interestRate / 100).toLong()}",
                totalInterest = "₹${(principal * tenure.interestRate / 100).toLong()}",
                interestRate = "${tenure.interestRate}% p.a.",
                maturityDate = "Calculated"
            )
        }
    }

    private fun purchaseFD() {
        val state = _uiState.value
        val details = state.details ?: return
        val tenure = state.selectedTenure ?: return
        val amount = state.amount.toLongOrNull() ?: return
        val payout = state.selectedPayoutMode ?: return

        _uiState.update { it.copy(isPurchasing = true) }
        viewModelScope.launch {
            val body = PurchaseFDBodyDto(
                investment_amount = amount,
                investment_period = tenure.tenureDays,
                payout_frequency = payout.id,
                product_id = details.id,
                tenure = tenure.tenureDays
            )
            purchaseFDUseCase(body)
                .onSuccess { url ->
                    _uiState.update { it.copy(isPurchasing = false) }
                    sendEffect(SetInvestmentDetailsEffect.NavigateToPurchaseUrl(url))
                }
                .onError { error ->
                    _uiState.update {
                        it.copy(
                            isPurchasing = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    private fun sendEffect(effect: SetInvestmentDetailsEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
