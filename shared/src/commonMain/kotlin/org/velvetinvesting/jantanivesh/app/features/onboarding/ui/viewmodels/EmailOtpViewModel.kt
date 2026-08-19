package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpController
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpUiState
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.RequestEmailOtpUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.VerifyEmailOtpUseCase

data class EmailOtpUiState(
    val email: String = "",
    val otp: OtpUiState = OtpUiState()
)

sealed interface EmailOtpEvent {
    data class OnOtpChanged(val otp: String) : EmailOtpEvent
    data object OnVerifyClicked : EmailOtpEvent
    data object OnResendClicked : EmailOtpEvent
    data object OnBackClicked : EmailOtpEvent
}

sealed interface EmailOtpEffect {
    data object EmailVerified : EmailOtpEffect
    data object NavigateBack : EmailOtpEffect
}

/**
 * The email leg of onboarding's OTP step. [email] comes in on the route rather than being re-read
 * from the server, so a resend goes to exactly the address the previous screen submitted.
 *
 * The code itself is never sent back on resend — the server re-mails whatever address it is given,
 * which is why [RequestEmailOtpUseCase] is reused here rather than a dedicated resend call.
 */
class EmailOtpViewModel(
    private val email: String,
    private val requestEmailOtp: RequestEmailOtpUseCase,
    private val verifyEmailOtp: VerifyEmailOtpUseCase,
    private val authPrefs: AuthPrefs
) : ViewModel() {

    private val otpController = OtpController(viewModelScope)

    val uiState: StateFlow<EmailOtpUiState> = otpController.state
        .map { otp -> EmailOtpUiState(email = email, otp = otp) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EmailOtpUiState(email = email)
        )

    private val _effect = Channel<EmailOtpEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: EmailOtpEvent) {
        when (event) {
            is EmailOtpEvent.OnOtpChanged -> otpController.onOtpChange(event.otp)
            EmailOtpEvent.OnVerifyClicked -> verifyOtp()
            EmailOtpEvent.OnResendClicked -> resendOtp()
            EmailOtpEvent.OnBackClicked -> sendEffect(EmailOtpEffect.NavigateBack)
        }
    }

    private fun verifyOtp() {
        val otpState = otpController.state.value
        if (!otpState.isComplete) return

        viewModelScope.launch {
            otpController.withLoading { verifyEmailOtp(otpState.otpValue) }
                .onSuccess {
                    // From here on the address is settled — the review screen shows it as final
                    // rather than offering it for editing.
                    authPrefs.setEmail(email)
                    authPrefs.setEmailVerified(true)
                    sendEffect(EmailOtpEffect.EmailVerified)
                }
                .onError { error ->
                    // The code stays on screen so the user can correct a single mistyped digit.
                    SnackBarController.showError(error.message)
                }
        }
    }

    private fun resendOtp() {
        if (!otpController.state.value.canResend) return

        viewModelScope.launch {
            otpController.withLoading { requestEmailOtp(email) }
                .onSuccess {
                    otpController.clearOtp()
                    otpController.startResendTimer()
                    SnackBarController.showSuccess("A new code has been sent to $email")
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
                }
        }
    }

    private fun sendEffect(effect: EmailOtpEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
