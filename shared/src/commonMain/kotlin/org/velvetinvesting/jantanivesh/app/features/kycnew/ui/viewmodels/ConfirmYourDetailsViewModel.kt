package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.usecases.GetPANVerificationStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.usecases.InitiatePANVerificationUseCase

data class ConfirmYourDetailsUiState(
    val pan: String = "",
    val name: String = "",
    val dob: String = "",
    val isConsentChecked: Boolean = false,
    val isLoading: Boolean = false
) {
    val canSubmit: Boolean
        get() = isConsentChecked &&
                OnboardingInput.isValidPan(pan) &&
                OnboardingInput.isFilled(name) &&
                OnboardingInput.isValidIsoDate(dob)
}

sealed interface ConfirmYourDetailsEvent {
    data class OnPanChange(val pan: String) : ConfirmYourDetailsEvent
    data class OnNameChange(val name: String) : ConfirmYourDetailsEvent
    data class OnDobChange(val dob: String) : ConfirmYourDetailsEvent
    data class OnConsentChange(val isChecked: Boolean) : ConfirmYourDetailsEvent
    data object OnTermsClick : ConfirmYourDetailsEvent
    data object OnPrivacyClick : ConfirmYourDetailsEvent
    data object OnReadMoreClick : ConfirmYourDetailsEvent
    data object OnProceedClick : ConfirmYourDetailsEvent
}

sealed interface ConfirmYourDetailsEffect {
    data object PanVerified : ConfirmYourDetailsEffect
    data object NavigateToKycInitiate : ConfirmYourDetailsEffect
    data object OpenTermsUrl : ConfirmYourDetailsEffect
    data object OpenPrivacyUrl : ConfirmYourDetailsEffect
    data object OpenReadMoreUrl : ConfirmYourDetailsEffect
}

class ConfirmYourDetailsViewModel(
    private val initiatePANVerification: InitiatePANVerificationUseCase,
    private val getPANVerificationStatus: GetPANVerificationStatusUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConfirmYourDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<ConfirmYourDetailsEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: ConfirmYourDetailsEvent) {
        when (event) {
            is ConfirmYourDetailsEvent.OnPanChange -> onPanChange(event.pan)
            is ConfirmYourDetailsEvent.OnNameChange -> onNameChange(event.name)
            is ConfirmYourDetailsEvent.OnDobChange -> onDobChange(event.dob)
            is ConfirmYourDetailsEvent.OnConsentChange -> onConsentChange(event.isChecked)
            ConfirmYourDetailsEvent.OnProceedClick -> onProceedClick()
            ConfirmYourDetailsEvent.OnTermsClick -> onTermsClick()
            ConfirmYourDetailsEvent.OnPrivacyClick -> onPrivacyClick()
            ConfirmYourDetailsEvent.OnReadMoreClick -> onReadMoreClick()
        }
    }

    private fun onPanChange(pan: String) {
        _uiState.update { it.copy(pan = OnboardingInput.sanitizePan(pan)) }
    }

    private fun onNameChange(name: String) {
        _uiState.update { it.copy(name = OnboardingInput.sanitizeName(name)) }
    }

    /** Always arrives as `yyyy-MM-dd` from the date picker; the field itself is read-only. */
    private fun onDobChange(dob: String) {
        _uiState.update { it.copy(dob = dob) }
    }

    private fun onConsentChange(isChecked: Boolean) {
        _uiState.update { it.copy(isConsentChecked = isChecked) }
    }

    private fun onProceedClick() {
        val state = _uiState.value
        if (state.isLoading || !state.canSubmit) return

        viewModelScope.launch {
            setLoading(true)

            val initiateResult = initiatePANVerification(
                pan = state.pan,
                name = state.name,
                dob = state.dob
            )

            when (initiateResult) {
                is NetworkResponse.Error -> {
                    setLoading(false)
                    SnackBarController.showError(initiateResult.error.message)
                }

                is NetworkResponse.Success -> checkVerificationStatus()
            }
        }
    }

    private suspend fun checkVerificationStatus() {
        when (val statusResult = getPANVerificationStatus()) {
            is NetworkResponse.Success -> {
                setLoading(false)
                sendEffect(ConfirmYourDetailsEffect.PanVerified)
            }

            is NetworkResponse.Error -> {
                setLoading(false)
                if (statusResult.error.isNewKycRequired) {
                    sendEffect(ConfirmYourDetailsEffect.NavigateToKycInitiate)
                } else {
                    SnackBarController.showError(statusResult.error.message)
                }
            }
        }
    }

    private fun onTermsClick() {
        sendEffect(ConfirmYourDetailsEffect.OpenTermsUrl)
    }

    private fun onPrivacyClick() {
        sendEffect(ConfirmYourDetailsEffect.OpenPrivacyUrl)
    }

    private fun onReadMoreClick() {
        sendEffect(ConfirmYourDetailsEffect.OpenReadMoreUrl)
    }

    private fun setLoading(loading: Boolean) {
        _uiState.update { it.copy(isLoading = loading) }
    }

    private fun sendEffect(effect: ConfirmYourDetailsEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
