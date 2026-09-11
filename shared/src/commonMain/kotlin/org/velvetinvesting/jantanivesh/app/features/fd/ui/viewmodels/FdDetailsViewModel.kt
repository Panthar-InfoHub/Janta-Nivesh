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
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FDDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.PayoutType
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.GetFDDetailsUseCase
import org.velvetinvesting.jantanivesh.app.features.fd.domain.utils.calculateMaturity
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.FdDetailsEffect.*

data class FDTenureUiModel(
    val id: String,
    val tenureLabel: String,
    val tenureDays: Int,
    val interestRate: Double,
    val annualYield: Double,
    /**
     * Whether this slab pays the best rate on offer for its payout mode. Worked out from the rates
     * themselves rather than taken from the gateway's `is_default_selection`, which marks the slab
     * the issuer wants pre-picked and is not always the one that pays most. Ties are kept: when
     * several terms share the top rate, every one of them is flagged.
     */
    val isMaxReturn: Boolean,
    val payoutFrequency: PayoutType,
    val maturityAmount: Long
)

data class FdDetailsUiState(
    val details: FDDetailsDomain? = null,
    val calculatedTenures: List<FDTenureUiModel> = emptyList(),
    val selectedPayoutMode: PayoutType? = null,
    val frequencies: List<PayoutType> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val activeSheet: FDModalType? = null,
    val investInput: String = "",
){
    val filteredTenures: List<FDTenureUiModel>
        get() = calculatedTenures.filter { tenure ->
            selectedPayoutMode == null ||
                    tenure.payoutFrequency.id == selectedPayoutMode.id
        }.reversed()

    /**
     * The slab the header card quotes its interest and tenure from.
     *
     * It is read off the same list the table below shows, so the two can never disagree. Where
     * several terms tie at the best rate the shortest wins: the same return, reached sooner, is
     * the one worth advertising.
     */
    val headlineTenure: FDTenureUiModel?
        get() = filteredTenures.filter { it.isMaxReturn }.minByOrNull { it.tenureDays }
            ?: filteredTenures.maxByOrNull { it.interestRate }
}

enum class FDModalType {
    PAYOUT,
    APPLICABLE,
    INVEST
}

sealed interface FdDetailsEvent {
    data object OnBackClicked : FdDetailsEvent
    data object LoadData : FdDetailsEvent
    data object OnShareClicked : FdDetailsEvent
    data class OnUpdateInvest(val amount: String) : FdDetailsEvent
    data class OnUpdatePayout(val payout: PayoutType) : FdDetailsEvent
    data class OnUpdateApplicable(val applicable: String) : FdDetailsEvent
    data object OnEditAmountClicked : FdDetailsEvent
    data object OnPayoutTypeClicked : FdDetailsEvent
    data object OnApplicantCategoryClicked : FdDetailsEvent
    data class OnTenureSelected(val tenureId: String) : FdDetailsEvent
    data object OnInvestNowClicked : FdDetailsEvent
    data object OnCloseSheet : FdDetailsEvent
}

sealed interface FdDetailsEffect {
    object NavigateBack : FdDetailsEffect
    data class NavigateToSetInvestmentDetails(val id: String) : FdDetailsEffect
}

class FdDetailsViewModel(
    private val id: String,
    private val getFDDetailsUseCase: GetFDDetailsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FdDetailsUiState())
    val uiState: StateFlow<FdDetailsUiState> = _uiState.asStateFlow()

    private val _effect = Channel<FdDetailsEffect>()
    val effect = _effect.receiveAsFlow()
    init{
        loadFdDetails()
    }

    fun handleEvent(event: FdDetailsEvent) {
        when (event) {
            FdDetailsEvent.OnBackClicked -> sendEffect(FdDetailsEffect.NavigateBack)
            FdDetailsEvent.OnShareClicked -> {
                _uiState.value.details?.let {
//                    sendEffect(FdDetailsEffect.ShareFdDetails("Check out this FD from ${it.bankName}"))
                }
            }
            FdDetailsEvent.OnEditAmountClicked -> {
//                _uiState.update { it.copy(activeSheet = FDModalType.INVEST) }
            }
            FdDetailsEvent.OnPayoutTypeClicked -> {
//                _uiState.update { it.copy(activeSheet = FDModalType.PAYOUT) }
            }
            FdDetailsEvent.OnApplicantCategoryClicked -> {
//                _uiState.update { it.copy(activeSheet = FDModalType.APPLICABLE) }
            }
            is FdDetailsEvent.OnUpdateInvest -> {
                val cleanAmount = event.amount.filter { it.isDigit() }
                _uiState.update { it.copy(investInput = cleanAmount) }
                val amountLong = cleanAmount.toLongOrNull()
                if (amountLong != null) {
                    updateInvest(amountLong)
                } else if (cleanAmount.isEmpty()) {
                    updateInvest(0L)
                }
            }
            is FdDetailsEvent.OnUpdatePayout -> updateInterestPayout(event.payout)
            is FdDetailsEvent.OnUpdateApplicable -> updateApplicable(event.applicable)
            is FdDetailsEvent.OnTenureSelected -> {
                // Update selected tenure logic if needed in domain model or state
            }
            FdDetailsEvent.OnInvestNowClicked -> {
                _uiState.value.details?.let {
                    sendEffect(NavigateToSetInvestmentDetails(it.id))
                }
            }
            FdDetailsEvent.OnCloseSheet -> {
                _uiState.update { it.copy(activeSheet = null) }
            }

            FdDetailsEvent.LoadData -> loadFdDetails()
        }
    }

    private fun updateInvest(amount: Long) {
        _uiState.update { state ->
            val updatedDetails = state.details?.copy(invest = amount)
            state.copy(
                details = updatedDetails,
                calculatedTenures = calculateTenureReturns(updatedDetails, state.selectedPayoutMode)
            )
        }
    }

    private fun updateInterestPayout(payout: PayoutType) {
        _uiState.update { state ->
            val updatedDetails = state.details?.copy(selectedPayout = payout)
            state.copy(
                details = updatedDetails,
                selectedPayoutMode = payout,
                calculatedTenures = calculateTenureReturns(updatedDetails, payout)
            )
        }
    }

    private fun updateApplicable(applicable: String) {
        _uiState.update { state ->
            state.copy(
                details = state.details?.copy(applicable = applicable),
            )
        }
    }

    private fun calculateTenureReturns(
        details: FDDetailsDomain?,
        payoutType: PayoutType?
    ): List<FDTenureUiModel> {
        if (details == null) return emptyList()
        val currentPayout = payoutType ?: details.selectedPayout ?: PayoutType.Cumulative

        // The table is narrowed to a single payout mode before it is shown, so the best rate is
        // decided within each mode — whichever list the user is looking at names its own best.
        val bestRateByPayout = details.interestRates
            .groupBy { it.payoutFrequency.id }
            .mapValues { (_, rates) -> rates.maxOf { it.interestRate } }

        return details.interestRates.map { tenure ->
            val maturity = calculateMaturity(
                principal = details.invest,
                rate = tenure.interestRate,
                days = tenure.tenureDays,
                frequency = currentPayout
            )
            FDTenureUiModel(
                id = tenure.id,
                tenureLabel = tenure.tenureLabel,
                tenureDays = tenure.tenureDays,
                interestRate = tenure.interestRate,
                annualYield = tenure.annualYield,
                // Rates are parsed from strings, so equal slabs land on the same Double; the
                // tolerance only guards against a value that arrives fractionally off.
                isMaxReturn = bestRateByPayout[tenure.payoutFrequency.id]
                    ?.let { best -> tenure.interestRate >= best - RATE_EQUALITY_TOLERANCE }
                    ?: false,
                payoutFrequency = tenure.payoutFrequency,
                maturityAmount = maturity.toLong()
            )
        }
    }

    private fun loadFdDetails() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getFDDetailsUseCase(id)
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            details = data,
                            calculatedTenures = calculateTenureReturns(data, data.selectedPayout),
                            selectedPayoutMode = data.selectedPayout,
                            frequencies = data.payoutOptions,
                            investInput = data.invest.toString(),
                            isLoading = false,
                            errorMessage = null
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

    private fun sendEffect(effect: FdDetailsEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private companion object {
        /** Two rates within this of each other are the same rate as far as the badge is concerned. */
        const val RATE_EQUALITY_TOLERANCE = 0.0001
    }
}
