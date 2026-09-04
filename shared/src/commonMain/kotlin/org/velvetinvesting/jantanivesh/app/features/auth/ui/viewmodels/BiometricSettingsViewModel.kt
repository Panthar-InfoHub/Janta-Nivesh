package org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs

data class BiometricSettingsUiState(
    val enabled: Boolean = true
)

sealed interface BiometricSettingsEvent {
    data class OnEnabledChanged(val enabled: Boolean) : BiometricSettingsEvent
    data object OnBackClicked : BiometricSettingsEvent
}

sealed interface BiometricLoginEffect {
    data object NavigateBack : BiometricLoginEffect
}


class BiometricSettingsViewModel(
    private val authPrefs: AuthPrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        BiometricSettingsUiState(enabled = authPrefs.isBiometricLoginEnabled())
    )
    val uiState: StateFlow<BiometricSettingsUiState> = _uiState.asStateFlow()

    private val _effect = Channel<BiometricLoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: BiometricSettingsEvent) {
        when (event) {
            is BiometricSettingsEvent.OnEnabledChanged -> {
                authPrefs.setBiometricLoginEnabled(event.enabled)
                _uiState.update { it.copy(enabled = event.enabled) }
            }

            BiometricSettingsEvent.OnBackClicked ->
                viewModelScope.launch { _effect.send(BiometricLoginEffect.NavigateBack) }
        }
    }
}
