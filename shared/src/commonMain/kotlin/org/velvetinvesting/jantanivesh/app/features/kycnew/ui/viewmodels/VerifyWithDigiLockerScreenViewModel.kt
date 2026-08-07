package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class VerifyWithDigilockerUiState(
    val isLoading: Boolean = false
)

sealed interface VerifyWithDigilockerEvent {
    data object OnProceedClick : VerifyWithDigilockerEvent
}

sealed interface VerifyWithDigilockerEffect {
    data object NavigateToDigilocker : VerifyWithDigilockerEffect
}

class VerifyWithDigilockerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(VerifyWithDigilockerUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<VerifyWithDigilockerEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: VerifyWithDigilockerEvent) {
        when (event) {
            VerifyWithDigilockerEvent.OnProceedClick -> onProceedClick()
        }
    }

    private fun onProceedClick() {
        // TODO: Add any state setup required before launching the Digilocker flow
        sendEffect(VerifyWithDigilockerEffect.NavigateToDigilocker)
    }

    private fun sendEffect(effect: VerifyWithDigilockerEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}