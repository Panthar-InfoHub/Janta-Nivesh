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
import org.velvetinvesting.jantanivesh.app.core.utils.BrowserLauncher
import org.velvetinvesting.jantanivesh.app.features.fd.data.models.dto.PurchaseFDBodyDto
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FDDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FDTenureDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.PayoutType
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.GetFDDetailsUseCase
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.PurchaseFDUseCase
import org.velvetinvesting.jantanivesh.app.features.fd.domain.utils.calculateMaturity
import org.velvetinvesting.jantanivesh.app.features.fd.domain.utils.trimTo
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController

data class SetInvestmentDetailsUiState(
    val details: FDDetailsDomain? = null,
    val amount: String = "",
    val selectedTenure: FDTenureDomain? = null,
    val selectedPayoutMode: PayoutType? = null,
    val frequencies: List<PayoutType> = emptyList(),
    val availableTenures: List<FDTenureDomain> = emptyList(),
    val maturityAmount: String = "₹0",
    val totalInterest: String = "₹0",
    val interestRate: String = "0% p.a.",
    val maturityDate: String = "N/A",
    val isLoading: Boolean = false,
    val isPurchasing: Boolean = false,
    val errorMessage: String? = null,
    val showError: Boolean = false,
    val errorText: String = "",
    val minAmount: Long = 0,
    val isButtonEnabled: Boolean = false
)

sealed interface SetInvestmentDetailsEvent {
    data object LoadDetails : SetInvestmentDetailsEvent
    data class OnAmountChanged(val amount: String) : SetInvestmentDetailsEvent
    data class OnTenureChanged(val tenure: FDTenureDomain) : SetInvestmentDetailsEvent
    data class OnPayoutModeChanged(val payout: PayoutType) : SetInvestmentDetailsEvent
    data object OnBackClicked : SetInvestmentDetailsEvent
    data object OnContinueClicked : SetInvestmentDetailsEvent
}

sealed interface SetInvestmentDetailsEffect {
    data object NavigateBack : SetInvestmentDetailsEffect
}

class SetInvestmentDetailsViewModel(
    private val id: String,
    private val getFDDetailsUseCase: GetFDDetailsUseCase,
    private val purchaseFDUseCase: PurchaseFDUseCase,
    private val browserLaunchUseCase: BrowserLauncher
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetInvestmentDetailsUiState())
    val uiState: StateFlow<SetInvestmentDetailsUiState> = _uiState.asStateFlow()

    private val _effect = Channel<SetInvestmentDetailsEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: SetInvestmentDetailsEvent) {
        when (event) {
            is SetInvestmentDetailsEvent.LoadDetails -> loadFdDetails()
            is SetInvestmentDetailsEvent.OnAmountChanged -> {
                updateAmount(event.amount)
            }

            is SetInvestmentDetailsEvent.OnTenureChanged -> updateTenure(event.tenure)

            is SetInvestmentDetailsEvent.OnPayoutModeChanged -> {
                updatePayoutMode(event.payout)
            }

            SetInvestmentDetailsEvent.OnBackClicked -> {
                sendEffect(SetInvestmentDetailsEffect.NavigateBack)
            }

            SetInvestmentDetailsEvent.OnContinueClicked -> {
                purchaseFD()
            }
        }
    }
    init{
        loadFdDetails()
    }
    private fun loadFdDetails() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getFDDetailsUseCase(id)
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            details = data,
                            amount = data.minDeposit.toString(),
                            minAmount = data.minDeposit,

                            frequencies = data.payoutOptions,

                            selectedPayoutMode = null,
                            selectedTenure = null,

                            availableTenures = emptyList(),

                            isLoading = false,
                            errorMessage = null
                        )
                    }

                    updateButtonState()
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

    private fun updateAmount(input: String) {
        _uiState.update { state ->
            val parsedAmount = input.toLongOrNull()

            val hasInvalidNumber = input.isNotBlank() && parsedAmount == null
            val isBelowMinAmount = parsedAmount != null && parsedAmount < state.minAmount

            val errorText = when {
                hasInvalidNumber -> "Please enter a valid amount"
                isBelowMinAmount -> "Minimum amount is ₹${state.minAmount}"
                else -> ""
            }

            state.copy(
                amount = input,
                showError = hasInvalidNumber || isBelowMinAmount,
                errorText = errorText
            )
        }
        calculateReturns()
        updateButtonState()
    }

    private fun updateTenure(tenure: FDTenureDomain) {
        _uiState.update {
            it.copy(
                selectedTenure = tenure
            )
        }

        calculateReturns()
        updateButtonState()
    }

    private fun updatePayoutMode(payout: PayoutType) {
        _uiState.update { state ->

            val filteredTenures = state.details
                ?.interestRates
                ?.filter { it.payoutFrequency == payout }
                ?: emptyList()

            state.copy(
                selectedPayoutMode = payout,
                availableTenures = filteredTenures,
                selectedTenure = null
            )
        }

        calculateReturns()
        updateButtonState()
    }

    private fun calculateReturns() {
        val state = _uiState.value
        val principal = state.amount.toLongOrNull() ?: 0L
        val tenure = state.selectedTenure
        val payout = state.selectedPayoutMode

        if (tenure == null || payout == null || principal == 0L) {
            _uiState.update {
                it.copy(
                    maturityAmount = "₹0",
                    totalInterest = "₹0",
                    interestRate = tenure?.let { rate -> "${rate.interestRate}% p.a." } ?: "0% p.a.",
                    maturityDate = "N/A"
                )
            }
            return
        }

        val maturityValue = calculateMaturity(
            principal = principal,
            rate = tenure.interestRate,
            days = tenure.tenureDays,
            frequency = payout
        )

        val totalInterest = maturityValue - principal
        val today = kotlin.time.Clock.System.todayIn(TimeZone.currentSystemDefault())
        val maturityDateObj = today.plus(tenure.tenureDays, DateTimeUnit.DAY)
        val formattedDate = dateFormatter.format(maturityDateObj)

        _uiState.update {
            it.copy(
                maturityAmount = "₹${maturityValue.trimTo(2)}",
                totalInterest = "₹${totalInterest.trimTo(2)}",
                interestRate = "${tenure.interestRate}% p.a.",
                maturityDate = formattedDate
            )
        }
    }
    private val dateFormatter = LocalDate.Format {
        this@Format.day(padding = Padding.ZERO)
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED) // e.g., "Jan", "Oct"
        char(' ')
        year()
    }

    private fun updateButtonState() {
        _uiState.update { state ->
            val amount = state.amount.toLongOrNull()

            state.copy(
                isButtonEnabled =
                    state.selectedTenure != null &&
                            amount != null &&
                            amount > 0 &&
                            amount >= state.minAmount &&
                            !state.showError
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
                    browserLaunchUseCase.launchBrowser(url)
                    sendEffect(SetInvestmentDetailsEffect.NavigateBack)
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
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
