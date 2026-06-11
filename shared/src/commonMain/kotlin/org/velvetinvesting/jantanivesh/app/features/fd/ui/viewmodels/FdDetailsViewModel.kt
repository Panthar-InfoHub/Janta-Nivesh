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
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FDDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.PayoutType
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.GetFDDetailsUseCase

data class FdDetailsUiState(
    val details: FDDetailsDomain? = null,
    val selectedPayoutMode: PayoutType? = null,
    val frequencies: List<PayoutType> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val activeSheet: FDModalType? = null,
    val isInvestmentEditable: Boolean = false
)

enum class FDModalType {
    PAYOUT,
    APPLICABLE,
    INVEST
}

sealed interface FdDetailsEvent {
    data object OnBackClicked : FdDetailsEvent
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
    data class ShareFdDetails(val message: String) : FdDetailsEffect
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
                    sendEffect(FdDetailsEffect.ShareFdDetails("Check out this FD from ${it.bankName}"))
                }
            }
            FdDetailsEvent.OnEditAmountClicked -> {
                _uiState.update { it.copy(activeSheet = FDModalType.INVEST) }
            }
            FdDetailsEvent.OnPayoutTypeClicked -> {
                _uiState.update { it.copy(activeSheet = FDModalType.PAYOUT) }
            }
            FdDetailsEvent.OnApplicantCategoryClicked -> {
                _uiState.update { it.copy(activeSheet = FDModalType.APPLICABLE) }
            }
            is FdDetailsEvent.OnUpdateInvest -> {
                val amountLong = event.amount.toLongOrNull()
                if (amountLong != null) {
                    updateInvest(amountLong)
                }
            }
            is FdDetailsEvent.OnUpdatePayout -> updateInterestPayout(event.payout)
            is FdDetailsEvent.OnUpdateApplicable -> updateApplicable(event.applicable)
            is FdDetailsEvent.OnTenureSelected -> {
                // Update selected tenure logic if needed in domain model or state
            }
            FdDetailsEvent.OnInvestNowClicked -> {
                _uiState.value.details?.let {
                    sendEffect(FdDetailsEffect.NavigateToSetInvestmentDetails(it.id))
                }
            }
            FdDetailsEvent.OnCloseSheet -> {
                _uiState.update { it.copy(activeSheet = null) }
            }
        }
    }

    private fun updateInvest(amount: Long) {
        _uiState.update { state ->
            state.copy(
                details = state.details?.copy(invest = amount),
                activeSheet = null
            )
        }
    }

    private fun updateInterestPayout(payout: PayoutType) {
        _uiState.update { state ->
            state.copy(
                details = state.details?.copy(selectedPayout = payout),
                activeSheet = null
            )
        }
    }

    private fun updateApplicable(applicable: String) {
        _uiState.update { state ->
            state.copy(
                details = state.details?.copy(applicable = applicable),
                activeSheet = null
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
                            isLoading = false,
                            errorMessage = null
                        )
                    }
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

    private fun sendEffect(effect: FdDetailsEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
