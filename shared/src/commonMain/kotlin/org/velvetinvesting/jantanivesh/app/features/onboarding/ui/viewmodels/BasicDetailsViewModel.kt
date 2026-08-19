package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.SubmitBasicDetailsUseCase

data class BasicDetailsUiState(
    val name: String = "",
    val dob: String = "",
    val isLoading: Boolean = false
) {
    val canSubmit: Boolean
        get() = OnboardingInput.isFilled(name) &&
                OnboardingInput.isValidIsoDate(dob)
}

sealed interface BasicDetailsEvent {
    data class OnNameChange(val name: String) : BasicDetailsEvent
    data class OnDobChange(val dob: String) : BasicDetailsEvent
    data object OnProceedClick : BasicDetailsEvent
}

sealed interface BasicDetailsEffect {
    data object NavigateToPanInitiate : BasicDetailsEffect
}

class BasicDetailsViewModel(
    private val basicDetailsUseCase: SubmitBasicDetailsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(BasicDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<BasicDetailsEffect>()
    val effect = _effect.asSharedFlow()

    fun handleEvent(event: BasicDetailsEvent) {
        when (event) {
            is BasicDetailsEvent.OnNameChange -> onNameChange(event.name)
            is BasicDetailsEvent.OnDobChange -> onDobChange(event.dob)
            BasicDetailsEvent.OnProceedClick -> onProceedClick()
        }
    }


    private fun onNameChange(name: String) {
        _uiState.update { it.copy(name = OnboardingInput.sanitizeName(name)) }
    }

    /** Always arrives as `yyyy-MM-dd` from the date picker; the field itself is read-only. */
    private fun onDobChange(dob: String) {
        _uiState.update { it.copy(dob = dob) }
    }


    private fun onProceedClick() {
        val state = _uiState.value
        if (state.isLoading || !state.canSubmit) return

        viewModelScope.launch {
            setLoading(true)

            basicDetailsUseCase(
                fullName = _uiState.value.name,
                dob = _uiState.value.dob
            )
                .onError {
                setLoading(false)
                SnackBarController.showError(it.message)
            }
                .onSuccess {
                    setLoading(false)
                    sendEffect(BasicDetailsEffect.NavigateToPanInitiate)
                }

        }
    }


    private fun setLoading(loading: Boolean) {
        _uiState.update { it.copy(isLoading = loading) }
    }

    private fun sendEffect(effect: BasicDetailsEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
