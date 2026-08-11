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
import org.velvetinvesting.jantanivesh.app.features.login.domain.usecases.LoginWithNumberUseCase

data class LoginWithPhoneNumberUiState(
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val isOwnershipDeclared: Boolean = false,
    val areTermsAccepted: Boolean = false
) {
    /** Both declarations are the investor's own action, so neither starts ticked. */
    val isNextEnabled: Boolean
        get() = phoneNumber.length == PHONE_NUMBER_LENGTH &&
                isOwnershipDeclared &&
                areTermsAccepted

    companion object {
        const val PHONE_NUMBER_LENGTH = 10
    }
}

sealed interface LoginWithPhoneNumberEvent {
    data class OnPhoneNumberChanged(val phoneNumber: String) : LoginWithPhoneNumberEvent
    data class OnOwnershipDeclarationChanged(val isChecked: Boolean) : LoginWithPhoneNumberEvent
    data class OnTermsAcceptanceChanged(val isChecked: Boolean) : LoginWithPhoneNumberEvent
    object OnVerifyClicked : LoginWithPhoneNumberEvent
    object OnBackClicked : LoginWithPhoneNumberEvent
}

sealed interface LoginWithPhoneNumberEffect {
    object NavigateToOtpScreen : LoginWithPhoneNumberEffect
    object NavigateBack : LoginWithPhoneNumberEffect
}

class LoginWithPhoneNumberViewModel(
    private val loginUseCase: LoginWithNumberUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginWithPhoneNumberUiState())
    val uiState: StateFlow<LoginWithPhoneNumberUiState> = _uiState.asStateFlow()

    private val _effect = Channel<LoginWithPhoneNumberEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: LoginWithPhoneNumberEvent) {
        when (event) {
            is LoginWithPhoneNumberEvent.OnPhoneNumberChanged -> {
                val length = LoginWithPhoneNumberUiState.PHONE_NUMBER_LENGTH
                if (event.phoneNumber.all { it.isDigit() } && event.phoneNumber.length <= length) {
                    _uiState.update { it.copy(phoneNumber = event.phoneNumber) }
                }
            }

            is LoginWithPhoneNumberEvent.OnOwnershipDeclarationChanged ->
                _uiState.update { it.copy(isOwnershipDeclared = event.isChecked) }

            is LoginWithPhoneNumberEvent.OnTermsAcceptanceChanged ->
                _uiState.update { it.copy(areTermsAccepted = event.isChecked) }

            LoginWithPhoneNumberEvent.OnVerifyClicked -> verifyPhoneNumber()
            LoginWithPhoneNumberEvent.OnBackClicked -> sendEffect(LoginWithPhoneNumberEffect.NavigateBack)
        }
    }

    private fun verifyPhoneNumber() {
        val state = _uiState.value
        if (!state.isNextEnabled) return
        val currentNumber = state.phoneNumber

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            var currentRetry = 0
            val maxRetries = 2

            while (currentRetry <= maxRetries) {
                var success = false

                loginUseCase(currentNumber)
                    .onSuccess {
                        _uiState.update { it.copy(isLoading = false) }
                        sendEffect(LoginWithPhoneNumberEffect.NavigateToOtpScreen)
                        success = true
                    }
                    .onError { error ->
                        if (currentRetry == maxRetries) {
                            _uiState.update { it.copy(isLoading = false) }
                            SnackBarController.showError(error.message)
                        }
                    }

                if (success) break

                currentRetry++

                if (currentRetry <= maxRetries) {
                    delay(1000)
                }
            }
        }
    }

    private fun sendEffect(effect: LoginWithPhoneNumberEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}