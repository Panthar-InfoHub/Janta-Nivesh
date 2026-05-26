package org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginWithPhoneNumberUiState(
    val phoneNumber: String = "",
    val isLoading: Boolean = false
)

sealed interface LoginWithPhoneNumberEvent {
    data class OnPhoneNumberChanged(val phoneNumber: String) : LoginWithPhoneNumberEvent
    object OnVerifyClicked : LoginWithPhoneNumberEvent
    object OnBackClicked : LoginWithPhoneNumberEvent
}

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
                if(event.phoneNumber.length <= 10)
                _uiState.update { it.copy(phoneNumber = event.phoneNumber) }
            }

            LoginWithPhoneNumberEvent.OnVerifyClicked -> verifyPhoneNumber()
            LoginWithPhoneNumberEvent.OnBackClicked -> sendEffect(LoginWithPhoneNumberEffect.NavigateBack)
        }
    }

    private fun verifyPhoneNumber() {
        val currentNumber = _uiState.value.phoneNumber

        // TODO: Add backend API call to request OTP here.
        if(currentNumber.length == 10)
        sendEffect(LoginWithPhoneNumberEffect.NavigateToOtpScreen)
    }

    private fun sendEffect(effect: LoginWithPhoneNumberEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}