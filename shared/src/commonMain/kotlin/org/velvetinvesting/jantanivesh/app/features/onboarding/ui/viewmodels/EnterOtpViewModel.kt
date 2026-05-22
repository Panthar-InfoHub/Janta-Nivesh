package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EnterOtpUiState(
    val otpValue: String = "",
    val phoneNumber: String = "+971 1 123 123 1234",
    val resendTimerSeconds: Int = 24,
    val isLoading: Boolean = false
)

sealed interface EnterOtpEvent {
    data class OnOtpChanged(val otp: String) : EnterOtpEvent
    object OnNextClicked : EnterOtpEvent
    object OnBackClicked : EnterOtpEvent
    object OnResendClicked : EnterOtpEvent
}

sealed interface EnterOtpEffect {
    object NavigateToNextScreen : EnterOtpEffect
    object NavigateBack : EnterOtpEffect
     data class ShowToast(val message: String) : EnterOtpEffect
}

class EnterOtpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EnterOtpUiState())
    val uiState: StateFlow<EnterOtpUiState> = _uiState.asStateFlow()

    private val _effect = Channel<EnterOtpEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: EnterOtpEvent) {
        when (event) {
            is EnterOtpEvent.OnOtpChanged -> {
                if (event.otp.length <= 5) {
                    _uiState.update { it.copy(otpValue = event.otp) }
                }
            }
            EnterOtpEvent.OnNextClicked -> verifyOtp()
            EnterOtpEvent.OnBackClicked -> sendEffect(EnterOtpEffect.NavigateBack)
            EnterOtpEvent.OnResendClicked -> resendOtp()
        }
    }

    private fun verifyOtp() {
        val currentOtp = _uiState.value.otpValue
        if (currentOtp.length == 5) {
            // TODO: Implement backend verification here
            sendEffect(EnterOtpEffect.NavigateToNextScreen)
        }
    }

    private fun resendOtp() {
        // TODO: Call API to resend OTP and restart the timer
    }

    private fun sendEffect(effect: EnterOtpEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}