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

// --- STATE ---
data class LoginWithPhoneNumberUiState(
    val phoneNumber: String = "",
    val isLoading: Boolean = false
)

// --- EVENTS (UI to ViewModel) ---
sealed interface LoginWithPhoneNumberEvent {
    data class OnPhoneNumberChanged(val phoneNumber: String) : LoginWithPhoneNumberEvent
    object OnVerifyClicked : LoginWithPhoneNumberEvent
    object OnBackClicked : LoginWithPhoneNumberEvent
}

// --- EFFECTS (ViewModel to Route/Navigation) ---
sealed interface LoginWithPhoneNumberEffect {
    object NavigateToOtpScreen : LoginWithPhoneNumberEffect
    object NavigateBack : LoginWithPhoneNumberEffect
}

class LoginWithPhoneNumberViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginWithPhoneNumberUiState())
    val uiState: StateFlow<LoginWithPhoneNumberUiState> = _uiState.asStateFlow()

    private val _effect = Channel<LoginWithPhoneNumberEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: LoginWithPhoneNumberEvent) {
        when (event) {
            is LoginWithPhoneNumberEvent.OnPhoneNumberChanged -> {
                // You can add validation logic here (e.g., max 10 digits, numbers only)
                _uiState.update { it.copy(phoneNumber = event.phoneNumber) }
            }

            LoginWithPhoneNumberEvent.OnVerifyClicked -> verifyPhoneNumber()
            LoginWithPhoneNumberEvent.OnBackClicked -> sendEffect(LoginWithPhoneNumberEffect.NavigateBack)
        }
    }

    private fun verifyPhoneNumber() {
        val currentNumber = _uiState.value.phoneNumber

        // TODO: Add backend API call to request OTP here.
        // For now, we simulate success and trigger navigation
        sendEffect(LoginWithPhoneNumberEffect.NavigateToOtpScreen)
    }

    private fun sendEffect(effect: LoginWithPhoneNumberEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}