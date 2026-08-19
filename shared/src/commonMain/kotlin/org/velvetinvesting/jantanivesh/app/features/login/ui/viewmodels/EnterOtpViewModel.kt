package org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.domain.model.OnboardingStage
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpController
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpUiState
import org.velvetinvesting.jantanivesh.app.features.login.domain.usecases.LoginWithNumberUseCase
import org.velvetinvesting.jantanivesh.app.features.login.domain.usecases.VerifyOTPUseCase

/**
 * [otp] holds everything the shared OTP screen draws; the rest of this state is what makes *this*
 * OTP screen the login one.
 */
data class EnterOtpUiState(
    val phoneNumber: String = "",
    val otp: OtpUiState = OtpUiState()
)

sealed interface EnterOtpEvent {
    data class OnOtpChanged(val otp: String) : EnterOtpEvent
    data object OnNextClicked : EnterOtpEvent
    data object OnBackClicked : EnterOtpEvent
    data object OnResendClicked : EnterOtpEvent

    data class OnPhoneNumberChanged(val phoneNumber: String) : EnterOtpEvent
}

sealed interface EnterOtpEffect {
    data class NavigateOnboardingFlow(
        val stage: OnboardingStage
    ) : EnterOtpEffect
    data object NavigateToMainAppFlow : EnterOtpEffect
    data object NavigateBack : EnterOtpEffect
     data class ShowToast(val message: String) : EnterOtpEffect
}

class EnterOtpViewModel(
    private val loginUseCase: LoginWithNumberUseCase,
    private val verifyOtpUseCase: VerifyOTPUseCase
) : ViewModel() {

    private val otpController = OtpController(viewModelScope)

    private val _phoneNumber = MutableStateFlow("")

    val uiState: StateFlow<EnterOtpUiState> =
        combine(_phoneNumber, otpController.state) { phoneNumber, otp ->
            EnterOtpUiState(phoneNumber = phoneNumber, otp = otp)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EnterOtpUiState()
        )

    private val _effect = Channel<EnterOtpEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: EnterOtpEvent) {
        when (event) {
            is EnterOtpEvent.OnOtpChanged -> otpController.onOtpChange(event.otp)
            EnterOtpEvent.OnNextClicked -> verifyOtp()
            EnterOtpEvent.OnBackClicked -> sendEffect(EnterOtpEffect.NavigateBack)
            EnterOtpEvent.OnResendClicked -> resendOtp()
            is EnterOtpEvent.OnPhoneNumberChanged -> {
                _phoneNumber.update { event.phoneNumber }
            }
        }
    }

    private fun verifyOtp() {
        val otpState = otpController.state.value
        if (!otpState.isComplete) return

        viewModelScope.launch {
            otpController.withLoading {
                verifyOtpUseCase(_phoneNumber.value, otpState.otpValue)
            }
                .onSuccess {
                    if (it.canEnterMainApp) {
                        sendEffect(EnterOtpEffect.NavigateToMainAppFlow)
                    } else {
                        sendEffect(EnterOtpEffect.NavigateOnboardingFlow(it.stage))
                    }
                }
                .onError {
                    SnackBarController.showError(it.message)
                }
        }
    }

    private fun resendOtp() {
        loginWithPhone()
    }

    private fun loginWithPhone() {
        val currentNumber = _phoneNumber.value
        if (currentNumber.length != 10) return

        viewModelScope.launch {
            otpController.withLoading { loginUseCase(currentNumber) }
                .onSuccess {
                    otpController.startResendTimer()
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
                }
        }
    }

    private fun sendEffect(effect: EnterOtpEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
