package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class SetupAutopayUiState(
    val autopayType: String = "UPI Autopay",
    val autopayLimit: String = "₹1,00,000 / debit",
    val bankDetails: String = "HDFC BANK ••1193"
)

sealed interface SetupAutopayEvent {
    data object OnSetAutopayClick : SetupAutopayEvent
}

sealed interface SetupAutopayEffect {
    data object ProceedToAutopay : SetupAutopayEffect
}

class SetupAutopayViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SetupAutopayUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SetupAutopayEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: SetupAutopayEvent) {
        when (event) {
            SetupAutopayEvent.OnSetAutopayClick -> onSetAutopayClick()
        }
    }

    private fun onSetAutopayClick() {
        sendEffect(SetupAutopayEffect.ProceedToAutopay)
    }

    private fun sendEffect(effect: SetupAutopayEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}