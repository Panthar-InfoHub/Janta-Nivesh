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
data class EnterNameFromPanUiState(
    val fullName: String = "",
    val isLoading: Boolean = false
)

// --- EVENTS (UI to ViewModel) ---
sealed interface EnterNameFromPanEvent {
    data class OnNameChanged(val name: String) : EnterNameFromPanEvent
    object OnContinueClicked : EnterNameFromPanEvent
    object OnBackClicked : EnterNameFromPanEvent
}

// --- EFFECTS (ViewModel to Route/Navigation) ---
sealed interface EnterNameFromPanEffect {
    object NavigateToNextScreen : EnterNameFromPanEffect
    object NavigateBack : EnterNameFromPanEffect
}

// --- VIEWMODEL ---
class EnterNameFromPanViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EnterNameFromPanUiState())
    val uiState: StateFlow<EnterNameFromPanUiState> = _uiState.asStateFlow()

    private val _effect = Channel<EnterNameFromPanEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: EnterNameFromPanEvent) {
        when (event) {
            is EnterNameFromPanEvent.OnNameChanged -> {
                _uiState.update { it.copy(fullName = event.name) }
            }
            EnterNameFromPanEvent.OnContinueClicked -> saveNameAndContinue()
            EnterNameFromPanEvent.OnBackClicked -> sendEffect(EnterNameFromPanEffect.NavigateBack)
        }
    }

    private fun saveNameAndContinue() {
        val currentName = _uiState.value.fullName
        if (currentName.isNotBlank()) {
            // TODO: Add any required validation or backend logic here
            sendEffect(EnterNameFromPanEffect.NavigateToNextScreen)
        }
    }

    private fun sendEffect(effect: EnterNameFromPanEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}