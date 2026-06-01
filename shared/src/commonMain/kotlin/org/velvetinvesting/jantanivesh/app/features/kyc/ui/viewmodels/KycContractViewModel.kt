package org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.FinalizeKycUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.GetContractPdfUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.GetESignUrlUseCase

data class KycContractUiState(
    val isLoading: Boolean = false,
    val contractPdfUrl: String? = null,
    val eSignUrl: String? = null,
    val isKycFinalized: Boolean = false
)

sealed interface KycContractEvent {
    data object OnLoadContract : KycContractEvent
    data object OnStartESignClicked : KycContractEvent
    data object OnESignCompleted : KycContractEvent
}

sealed interface KycContractEffect {
    data class OpenBrowser(val url: String) : KycContractEffect
    data object NavigateToSuccess : KycContractEffect
    data class ShowError(val message: String) : KycContractEffect
}

class KycContractViewModel(
    private val getContractPdfUseCase: GetContractPdfUseCase,
    private val getESignUrlUseCase: GetESignUrlUseCase,
    private val finalizeKycUseCase: FinalizeKycUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(KycContractUiState())
    val uiState: StateFlow<KycContractUiState> = _uiState.asStateFlow()

    private val _effect = Channel<KycContractEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: KycContractEvent) {
        when (event) {
            KycContractEvent.OnLoadContract -> loadContractPdf()
            KycContractEvent.OnStartESignClicked -> loadESignUrl()
            KycContractEvent.OnESignCompleted -> finalizeKyc()
        }
    }

    private fun loadContractPdf() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val response = getContractPdfUseCase()) {
                is NetworkResponse.Success -> {
                    _uiState.update { it.copy(contractPdfUrl = response.data) }
                }
                is NetworkResponse.Error -> {
                    sendEffect(KycContractEffect.ShowError(response.error.message))
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun loadESignUrl() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val response = getESignUrlUseCase()) {
                is NetworkResponse.Success -> {
                    _uiState.update { it.copy(eSignUrl = response.data) }
                    sendEffect(KycContractEffect.OpenBrowser(response.data))
                }
                is NetworkResponse.Error -> {
                    sendEffect(KycContractEffect.ShowError(response.error.message))
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun finalizeKyc() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val response = finalizeKycUseCase()) {
                is NetworkResponse.Success -> {
                    _uiState.update { it.copy(isKycFinalized = true) }
                    sendEffect(KycContractEffect.NavigateToSuccess)
                }
                is NetworkResponse.Error -> {
                    sendEffect(KycContractEffect.ShowError(response.error.message))
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEffect(effect: KycContractEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
