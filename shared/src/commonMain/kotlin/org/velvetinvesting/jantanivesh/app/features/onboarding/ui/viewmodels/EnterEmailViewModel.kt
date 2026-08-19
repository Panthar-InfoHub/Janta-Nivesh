package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.RequestEmailOtpUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.OnboardingInput

data class EmailIdUiState(
    val email: String = "",
    val isConsentChecked: Boolean = false,
    val isLoading: Boolean = false
) {
    val canSubmit: Boolean
        get() = isConsentChecked && OnboardingInput.isValidEmail(email) && !isLoading
}

sealed interface EmailIdEvent {
    data class OnEmailChange(val email: String) : EmailIdEvent
    data object OnGmailSuffixClick : EmailIdEvent
    data class OnConsentChange(val isChecked: Boolean) : EmailIdEvent
    data object OnSubmitClick : EmailIdEvent
}

sealed interface EmailIdEffect {
    /**
     * The code is on its way. Carries the address forward so the OTP screen can show it back to
     * the user and resend to it.
     */
    data class OtpRequested(val email: String) : EmailIdEffect
}

class EmailIdViewModel(
    private val requestEmailOtp: RequestEmailOtpUseCase,
    private val authPrefs: AuthPrefs
) : ViewModel() {
    private val _uiState = MutableStateFlow(EmailIdUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<EmailIdEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: EmailIdEvent) {
        when (event) {
            is EmailIdEvent.OnEmailChange -> onEmailChange(event.email)
            EmailIdEvent.OnGmailSuffixClick -> onGmailSuffixClick()
            is EmailIdEvent.OnConsentChange -> onConsentChange(event.isChecked)
            EmailIdEvent.OnSubmitClick -> onSubmitClick()
        }
    }

    private fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = OnboardingInput.sanitizeEmail(email)) }
    }

    private fun onGmailSuffixClick() {
        _uiState.update { state ->
            val currentEmail = state.email

            val newEmail = if (currentEmail.contains("@") || currentEmail.isEmpty()) {
                currentEmail
            } else {
                "$currentEmail@gmail.com"
            }

            state.copy(email = OnboardingInput.sanitizeEmail(newEmail))
        }
    }

    private fun onConsentChange(isChecked: Boolean) {
        _uiState.update { it.copy(isConsentChecked = isChecked) }
    }

    private fun onSubmitClick() {
        val email = _uiState.value.email.trim()
        if (!_uiState.value.canSubmit) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            requestEmailOtp(email)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    // Remembered as soon as it is submitted; it only counts as verified once the
                    // OTP for this address goes through.
                    authPrefs.setEmail(email)
                    authPrefs.setEmailVerified(false)
                    sendEffect(EmailIdEffect.OtpRequested(email))
                }
                .onError { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    SnackBarController.showError(error.message)
                }
        }
    }

    private fun sendEffect(effect: EmailIdEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}