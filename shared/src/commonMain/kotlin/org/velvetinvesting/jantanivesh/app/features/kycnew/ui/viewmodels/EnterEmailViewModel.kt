package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EmailIdUiState(
    val email: String = "",
    val isConsentChecked: Boolean = false
) {
    val canSubmit: Boolean
        get() = isConsentChecked && OnboardingInput.isValidEmail(email)
}

sealed interface EmailIdEvent {
    data class OnEmailChange(val email: String) : EmailIdEvent
    data object OnGmailSuffixClick : EmailIdEvent
    data class OnConsentChange(val isChecked: Boolean) : EmailIdEvent
    data object OnSubmitClick : EmailIdEvent
}

sealed interface EmailIdEffect {
    /** Carries the address forward — the next screen shows it back to the user. */
    data class EmailSubmitted(val email: String) : EmailIdEffect
}

class EmailIdViewModel : ViewModel() {
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
        val state = _uiState.value
        if (!state.canSubmit) return
        sendEffect(EmailIdEffect.EmailSubmitted(state.email.trim()))
    }

    private fun sendEffect(effect: EmailIdEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}