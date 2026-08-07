package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConfirmYourDetailsUiState(
    val pan: String = "",
    val name: String = "",
    val dob: String = "",
    val isConsentChecked: Boolean = true
)

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
    data object OnProceedClick : ConfirmYourDetailsEffect
    data object OpenTermsUrl : ConfirmYourDetailsEffect
    data object OpenPrivacyUrl : ConfirmYourDetailsEffect
    data object OpenReadMoreUrl : ConfirmYourDetailsEffect
}

class ConfirmYourDetailsViewModel : ViewModel() {
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
        // TODO: Add PAN validation or formatting here later
        _uiState.update { it.copy(pan = pan) }
    }

    private fun onNameChange(name: String) {
        // TODO: Add Name validation or formatting here later
        _uiState.update { it.copy(name = name) }
    }

    private fun onDobChange(dob: String) {
        // TODO: Add DOB validation or formatting here later
        _uiState.update { it.copy(dob = dob) }
    }

    private fun onConsentChange(isChecked: Boolean) {
        _uiState.update { it.copy(isConsentChecked = isChecked) }
    }

    private fun onProceedClick() {
        // TODO: Add any pre-proceed logic here
        sendEffect(ConfirmYourDetailsEffect.OnProceedClick)
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

    private fun sendEffect(effect: ConfirmYourDetailsEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}