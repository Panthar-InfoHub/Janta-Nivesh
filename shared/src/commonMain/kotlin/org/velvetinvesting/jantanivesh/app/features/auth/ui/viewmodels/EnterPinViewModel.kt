package org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpUiState

data class EnterPinUiState(
    val userName: String = "Ankit Bose",
    val otp: OtpUiState = OtpUiState(otpLength = 4)
)

sealed interface EnterPinEvent {
    data class OnPinChanged(val pin: String) : EnterPinEvent
    data object OnUseBiometricsClicked : EnterPinEvent
}

class EnterPinViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EnterPinUiState())
    val uiState: StateFlow<EnterPinUiState> = _uiState.asStateFlow()

    fun handleEvent(event: EnterPinEvent) {
        when (event) {
            is EnterPinEvent.OnPinChanged -> {
                if (event.pin.length <= _uiState.value.otp.otpLength) {
                    _uiState.update { it.copy(otp = it.otp.copy(otpValue = event.pin)) }
                }
            }
            EnterPinEvent.OnUseBiometricsClicked -> {
                // Handle biometrics logic
            }
        }
    }
}
