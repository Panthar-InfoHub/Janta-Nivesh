package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class VerifyBankAccountUiState(
    val isLoading: Boolean = false
)

sealed interface VerifyBankAccountEvent {
    data object OnVerifyUpiClick : VerifyBankAccountEvent
}

sealed interface VerifyBankAccountEffect {
    data object InitiateUpiVerification : VerifyBankAccountEffect
}

class VerifyBankAccountViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(VerifyBankAccountUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<VerifyBankAccountEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: VerifyBankAccountEvent) {
        when (event) {
            VerifyBankAccountEvent.OnVerifyUpiClick -> onVerifyUpiClick()
        }
    }

    private fun onVerifyUpiClick() {
        // TODO: Add logic to initialize the ₹1 penny drop transaction
        sendEffect(VerifyBankAccountEffect.InitiateUpiVerification)
    }

    private fun sendEffect(effect: VerifyBankAccountEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}