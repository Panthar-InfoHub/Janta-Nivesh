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
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.PurchaseFDUseCase

data class FdDetailsUiState(
    val details: FDDetailsDomain? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface FdDetailsEvent {
    data class LoadDetails(val id: String) : FdDetailsEvent
    data object OnBackClicked : FdDetailsEvent
    data object OnShareClicked : FdDetailsEvent
    data object OnEditAmountClicked : FdDetailsEvent
    data object OnPayoutTypeClicked : FdDetailsEvent
    data object OnApplicantCategoryClicked : FdDetailsEvent
    data class OnTenureSelected(val tenureId: String) : FdDetailsEvent
    data object OnInvestNowClicked : FdDetailsEvent
}

sealed interface FdDetailsEffect {
    object NavigateBack : FdDetailsEffect
    data class NavigateToSetInvestmentDetails(val id: String) : FdDetailsEffect
    data class ShareFdDetails(val message: String) : FdDetailsEffect
}

class FdDetailsViewModel(
    private val getFDDetailsUseCase: GetFDDetailsUseCase,
    private val purchaseFDUseCase: PurchaseFDUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FdDetailsUiState())
    val uiState: StateFlow<FdDetailsUiState> = _uiState.asStateFlow()

    private val _effect = Channel<FdDetailsEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: FdDetailsEvent) {
        when (event) {
            is FdDetailsEvent.LoadDetails -> loadFdDetails(event.id)
            FdDetailsEvent.OnBackClicked -> sendEffect(FdDetailsEffect.NavigateBack)
            FdDetailsEvent.OnShareClicked -> {
                _uiState.value.details?.let {
                    sendEffect(FdDetailsEffect.ShareFdDetails("Check out this FD from ${it.bankName}"))
                }
            }
            FdDetailsEvent.OnEditAmountClicked -> {
                TODO("Handle edit amount")
            }
            FdDetailsEvent.OnPayoutTypeClicked -> {
                TODO("Handle payout type selection")
            }
            FdDetailsEvent.OnApplicantCategoryClicked -> {
                TODO("Handle applicant category selection")
            }
            is FdDetailsEvent.OnTenureSelected -> {
                TODO("Update selected tenure logic")
            }
            FdDetailsEvent.OnInvestNowClicked -> {
                _uiState.value.details?.let {
                    sendEffect(FdDetailsEffect.NavigateToSetInvestmentDetails(it.id))
                }
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
