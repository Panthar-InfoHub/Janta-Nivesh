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
import kotlinx.datetime.LocalDate

data class EnterYourDOBUiState(
    val dob: String = "",
    val showDatePicker: Boolean = false,
    val isLoading: Boolean = false,
    val isNextEnabled: Boolean = false
)

sealed interface EnterYourDOBEvent {
    object OnDobFieldClicked : EnterYourDOBEvent
    data class OnDobSelected(val selectedDob: String) : EnterYourDOBEvent
    object OnDatePickerDismissed : EnterYourDOBEvent
    object OnVerifyClicked : EnterYourDOBEvent
    object OnBackClicked : EnterYourDOBEvent
}

sealed interface EnterYourDOBEffect {
    object NavigateToNextScreen : EnterYourDOBEffect
    object NavigateBack : EnterYourDOBEffect
}

class EnterYourDOBViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EnterYourDOBUiState())
    val uiState: StateFlow<EnterYourDOBUiState> = _uiState.asStateFlow()

    private val _effect = Channel<EnterYourDOBEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: EnterYourDOBEvent) {
        when (event) {
            EnterYourDOBEvent.OnDobFieldClicked -> {
                _uiState.update { it.copy(showDatePicker = true) }
            }
            is EnterYourDOBEvent.OnDobSelected -> {
                _uiState.update {
                    it.copy(
                        dob = event.selectedDob,
                        showDatePicker = false,
                        isNextEnabled = event.selectedDob.isNotBlank()
                    )
                }
            }
            EnterYourDOBEvent.OnDatePickerDismissed -> {
                _uiState.update { it.copy(showDatePicker = false) }
            }
            EnterYourDOBEvent.OnVerifyClicked -> verifyAndContinue()
            EnterYourDOBEvent.OnBackClicked -> sendEffect(EnterYourDOBEffect.NavigateBack)
        }
    }

    private fun verifyAndContinue() {
        val currentDob = _uiState.value.dob
        if (isValidDob(currentDob)) {
        // TODO: Add age validation or backend API calls here
        sendEffect(EnterYourDOBEffect.NavigateToNextScreen)
        }
    }

    fun isValidDob(dob: String): Boolean {
        val regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$".toRegex()
        if (!dob.matches(regex)) return false
        return try {
            val parts = dob.split("/")
            val isoFormat = "${parts[2]}-${parts[1]}-${parts[0]}"
            LocalDate.parse(isoFormat)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun sendEffect(effect: EnterYourDOBEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}