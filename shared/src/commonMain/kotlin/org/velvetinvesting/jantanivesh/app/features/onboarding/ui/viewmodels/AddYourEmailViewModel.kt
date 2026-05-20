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
data class AddYourEmailUiState(
    val email: String = "",
    val isLoading: Boolean = false
)

// --- EVENTS (UI to ViewModel) ---
sealed interface AddYourEmailEvent {
    data class OnEmailChanged(val email: String) : AddYourEmailEvent
    object OnVerifyClicked : AddYourEmailEvent
    object OnSkipClicked : AddYourEmailEvent
    object OnBackClicked : AddYourEmailEvent
}

// --- EFFECTS (ViewModel to Route/Navigation) ---
sealed interface AddYourEmailEffect {
    object NavigateToNextScreen : AddYourEmailEffect
    object NavigateBack : AddYourEmailEffect
}

// --- VIEWMODEL ---
class AddYourEmailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddYourEmailUiState())
    val uiState: StateFlow<AddYourEmailUiState> = _uiState.asStateFlow()

    private val _effect = Channel<AddYourEmailEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: AddYourEmailEvent) {
        when (event) {
            is AddYourEmailEvent.OnEmailChanged -> {
                _uiState.update { it.copy(email = event.email) }
            }
            AddYourEmailEvent.OnVerifyClicked -> verifyEmail()
            AddYourEmailEvent.OnSkipClicked -> skipEmail()
            AddYourEmailEvent.OnBackClicked -> sendEffect(AddYourEmailEffect.NavigateBack)
        }
    }

    private fun verifyEmail() {
        val currentEmail = _uiState.value.email
        if (currentEmail.isNotBlank() && currentEmail.contains("@")) {
            // TODO: Add backend verification/saving logic here
            sendEffect(AddYourEmailEffect.NavigateToNextScreen)
        }
    }

    private fun skipEmail() {
        // Since it's an optional step, skipping just moves them forward
        sendEffect(AddYourEmailEffect.NavigateToNextScreen)
    }

    private fun sendEffect(effect: AddYourEmailEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}