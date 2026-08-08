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
    val isConsentChecked: Boolean = true
)

sealed interface EmailIdEvent {
    data class OnEmailChange(val email: String) : EmailIdEvent
    data object OnGmailSuffixClick : EmailIdEvent
    data class OnConsentChange(val isChecked: Boolean) : EmailIdEvent
    data object OnSubmitClick : EmailIdEvent
}

sealed interface EmailIdEffect {
    data object NavigateToNext : EmailIdEffect
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
        _uiState.update { it.copy(email = email) }
    }

    private fun onGmailSuffixClick() {
        _uiState.update { state ->
            val currentEmail = state.email

            val newEmail = if (currentEmail.contains("@")) {
                currentEmail
            } else {
                "$currentEmail@gmail.com"
            }

            state.copy(email = newEmail)
        }
    }

    private fun onConsentChange(isChecked: Boolean) {
        _uiState.update { it.copy(isConsentChecked = isChecked) }
    }

    private fun onSubmitClick() {
        sendEffect(EmailIdEffect.NavigateToNext)
    }

    private fun sendEffect(effect: EmailIdEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}