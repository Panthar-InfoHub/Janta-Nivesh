package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.GetPANVerificationStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.InitiatePANVerificationUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.SkipPANVerificationUseCase

data class PanVerificationUiState(
    val pan: String = "",
    val isConsentChecked: Boolean = false,
    val isLoading: Boolean = false
) {
    val canSubmit: Boolean
        get() = isConsentChecked &&
                OnboardingInput.isValidPan(pan)
}

sealed interface PanVerificationEvent {
    data class OnPanChange(val pan: String) : PanVerificationEvent
    data class OnConsentChange(val isChecked: Boolean) : PanVerificationEvent
    data object OnTermsClick : PanVerificationEvent
    data object OnPrivacyClick : PanVerificationEvent
    data object OnReadMoreClick : PanVerificationEvent
    data object OnProceedClick : PanVerificationEvent
    data object OnSkipClick : PanVerificationEvent
}

sealed interface PanVerificationEffect {
    data object PanVerified : PanVerificationEffect
    data object NavigateToKycInitiate : PanVerificationEffect
    data object OpenTermsUrl : PanVerificationEffect
    data object OpenPrivacyUrl : PanVerificationEffect
    data object OpenReadMoreUrl : PanVerificationEffect
    data object SkipPanVerification : PanVerificationEffect
}

class PanVerificationViewModel(
    private val initiatePANVerification: InitiatePANVerificationUseCase,
    private val getPANVerificationStatus: GetPANVerificationStatusUseCase,
    private val skipPanVerificationCase: SkipPANVerificationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PanVerificationUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<PanVerificationEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: PanVerificationEvent) {
        when (event) {
            is PanVerificationEvent.OnPanChange -> onPanChange(event.pan)
            is PanVerificationEvent.OnConsentChange -> onConsentChange(event.isChecked)
            PanVerificationEvent.OnProceedClick -> onProceedClick()
            PanVerificationEvent.OnTermsClick -> onTermsClick()
            PanVerificationEvent.OnPrivacyClick -> onPrivacyClick()
            PanVerificationEvent.OnReadMoreClick -> onReadMoreClick()
            PanVerificationEvent.OnSkipClick -> onSkipClicked()
        }
    }

    private fun onPanChange(pan: String) {
        _uiState.update { it.copy(pan = OnboardingInput.sanitizePan(pan)) }
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
                pan = state.pan
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
                sendEffect(PanVerificationEffect.PanVerified)
            }

            is NetworkResponse.Error -> {
                setLoading(false)
                if (statusResult.error.isNewKycRequired) {
                    sendEffect(PanVerificationEffect.NavigateToKycInitiate)
                } else {
                    SnackBarController.showError(statusResult.error.message)
                }
            }
        }
    }

    private fun onSkipClicked() {
        viewModelScope.launch {
            setLoading(true)
            skipPanVerificationCase()
                .onSuccess {
                    setLoading(false)
                    sendEffect(PanVerificationEffect.SkipPanVerification)
                }
                .onError {
                    setLoading(false)
                    SnackBarController.showError(it.message)
                }
        }
    }

    private fun onTermsClick() {
        sendEffect(PanVerificationEffect.OpenTermsUrl)
    }

    private fun onPrivacyClick() {
        sendEffect(PanVerificationEffect.OpenPrivacyUrl)
    }

    private fun onReadMoreClick() {
        sendEffect(PanVerificationEffect.OpenReadMoreUrl)
    }

    private fun setLoading(loading: Boolean) {
        _uiState.update { it.copy(isLoading = loading) }
    }

    private fun sendEffect(effect: PanVerificationEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
