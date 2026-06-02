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
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.GetDigiLockerDetailsUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.InitiateKycUseCase

data class KYCScreenUiState(
    val isLoading: Boolean = false,
    val digilockerUrl: String? = null
)

sealed interface KYCScreenEvent {
    data object OnStartKycClicked : KYCScreenEvent
}

sealed interface KYCScreenEffect {
    data class OpenBrowser(val url: String) : KYCScreenEffect
    data object NavigateToForm : KYCScreenEffect
    data class ShowError(val message: String) : KYCScreenEffect
}

class KYCScreenViewModel(
    private val initiateKycUseCase: InitiateKycUseCase,
    private val getDigiLockerDetailsUseCase: GetDigiLockerDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(KYCScreenUiState())
    val uiState: StateFlow<KYCScreenUiState> = _uiState.asStateFlow()

    private val _effect = Channel<KYCScreenEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: KYCScreenEvent) {
        when (event) {
            KYCScreenEvent.OnStartKycClicked -> initiateKyc()
        }
    }

    private fun initiateKyc() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val response = initiateKycUseCase()) {
                is NetworkResponse.Success -> {
                    _uiState.update { it.copy(digilockerUrl = response.data.digilockerUrl) }
                    sendEffect(KYCScreenEffect.OpenBrowser(response.data.digilockerUrl))
                }
                is NetworkResponse.Error -> {
                    sendEffect(KYCScreenEffect.ShowError(response.error.message))
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun loadDigiLockerDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val response = getDigiLockerDetailsUseCase()) {
                is NetworkResponse.Success -> {
                    sendEffect(KYCScreenEffect.NavigateToForm)
                }
                is NetworkResponse.Error -> {
                    sendEffect(KYCScreenEffect.ShowError(response.error.message))
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEffect(effect: KYCScreenEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
