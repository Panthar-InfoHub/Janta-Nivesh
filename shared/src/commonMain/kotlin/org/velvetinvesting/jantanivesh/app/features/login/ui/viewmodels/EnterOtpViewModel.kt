package org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.login.domain.usecases.VerifyOTPUseCase

data class EnterOtpUiState(
    val otpValue: String = "",
    val phoneNumber: String = "",
    val resendTimerSeconds: Int = 30,
    val isLoading: Boolean = false,
    val isNextEnabled: Boolean = false
)

sealed interface EnterOtpEvent {
    data class OnOtpChanged(val otp: String) : EnterOtpEvent
    data object OnNextClicked : EnterOtpEvent
    data object OnBackClicked : EnterOtpEvent
    data object OnResendClicked : EnterOtpEvent

    data class OnPhoneNumberChanged(val phoneNumber: String) : EnterOtpEvent
}

sealed interface EnterOtpEffect {
    data object NavigateOnboardingFlow : EnterOtpEffect
    data object NavigateToMainAppFlow : EnterOtpEffect
    data object NavigateBack : EnterOtpEffect
     data class ShowToast(val message: String) : EnterOtpEffect
}

class EnterOtpViewModel(
    private val verifyOtpUseCase: VerifyOTPUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EnterOtpUiState())
    val uiState: StateFlow<EnterOtpUiState> = _uiState.asStateFlow()

    private val _effect = Channel<EnterOtpEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            for (i in 30 downTo 0) {
                _uiState.update { it.copy(resendTimerSeconds = i) }
                delay(1000)
            }
        }
    }

    fun handleEvent(event: EnterOtpEvent) {
        when (event) {
            is EnterOtpEvent.OnOtpChanged -> {
                if (event.otp.length <= 5 && event.otp.all { it.isDigit() }) {
                    _uiState.update {
                        it.copy(
                            otpValue = event.otp,
                            isNextEnabled = event.otp.length == 4
                        )
                    }
                }
            }
            EnterOtpEvent.OnNextClicked -> verifyOtp()
            EnterOtpEvent.OnBackClicked -> sendEffect(EnterOtpEffect.NavigateBack)
            EnterOtpEvent.OnResendClicked -> resendOtp()
            is EnterOtpEvent.OnPhoneNumberChanged -> {
                _uiState.update { it.copy(phoneNumber = event.phoneNumber) }
            }
        }
    }

    private fun verifyOtp() {
        val currentOtp = _uiState.value.otpValue
        if (currentOtp.length != 4) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            verifyOtpUseCase(
                _uiState.value.phoneNumber,
                currentOtp
            )
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    if (it.onboarded) {
                        sendEffect(EnterOtpEffect.NavigateToMainAppFlow)
                    } else {
                        sendEffect(EnterOtpEffect.NavigateOnboardingFlow)
                    }
                }
                .onError {
                    _uiState.update { it.copy(isLoading = false) }
                    SnackBarController.showError(it.message)
                }
        }
    }

    private fun resendOtp() {
        startTimer()
    }

    private fun sendEffect(effect: EnterOtpEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}