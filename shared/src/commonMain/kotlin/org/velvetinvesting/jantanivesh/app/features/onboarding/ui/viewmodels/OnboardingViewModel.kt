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
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.OnboardingDomain
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.OnboardUserUseCase

data class OnboardingUiState(
    // Name screen
    val fullName: String = "",
    val isNameNextEnabled: Boolean = false,
    
    // Email screen
    val email: String = "",
    val isEmailNextEnabled: Boolean = false,
    
    // DOB screen
    val dob: String = "",
    val showDatePicker: Boolean = false,
    val isDobNextEnabled: Boolean = false,
    
    // General
    val isLoading: Boolean = false
)

sealed interface OnboardingEvent {
    // Name events
    data class OnNameChanged(val name: String) : OnboardingEvent
    data object OnNameContinueClicked : OnboardingEvent
    data object OnNameBackClicked : OnboardingEvent
    
    // Email events
    data class OnEmailChanged(val email: String) : OnboardingEvent
    data object OnEmailVerifyClicked : OnboardingEvent
    data object OnEmailSkipClicked : OnboardingEvent
    data object OnEmailBackClicked : OnboardingEvent
    
    // DOB events
    data object OnDobFieldClicked : OnboardingEvent
    data class OnDobSelected(val selectedDob: String) : OnboardingEvent
    data object OnDatePickerDismissed : OnboardingEvent
    data object OnDobVerifyClicked : OnboardingEvent
    data object OnDobBackClicked : OnboardingEvent
}

sealed interface OnboardingEffect {
    // Name effects
    data object EnterNameFromPan_NavigateToNext : OnboardingEffect
    data object EnterNameFromPan_NavigateBack : OnboardingEffect
    
    // Email effects
    data object AddYourEmail_NavigateToNext : OnboardingEffect
    data object AddYourEmail_NavigateBack : OnboardingEffect
    
    // DOB effects
    data object EnterYourDOB_NavigateToNext : OnboardingEffect
    data object EnterYourDOB_NavigateBack : OnboardingEffect
    
    data class ShowError(val message: String) : OnboardingEffect
}

class OnboardingViewModel(
    private val onboardUserUseCase: OnboardUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _effect = Channel<OnboardingEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: OnboardingEvent) {
        when (event) {
            // Name Screen
            is OnboardingEvent.OnNameChanged -> {
                _uiState.update {
                    it.copy(
                        fullName = event.name,
                        isNameNextEnabled = isValidFullName(event.name)
                    )
                }
            }
            OnboardingEvent.OnNameContinueClicked -> {
                if (isValidFullName(_uiState.value.fullName)) {
                    sendEffect(OnboardingEffect.EnterNameFromPan_NavigateToNext)
                }
            }
            OnboardingEvent.OnNameBackClicked -> sendEffect(OnboardingEffect.EnterNameFromPan_NavigateBack)

            // Email Screen
            is OnboardingEvent.OnEmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = event.email,
                        isEmailNextEnabled = isValidEmail(event.email)
                    )
                }
            }
            OnboardingEvent.OnEmailVerifyClicked -> {
                if (isValidEmail(_uiState.value.email)) {
                    sendEffect(OnboardingEffect.AddYourEmail_NavigateToNext)
                }
            }
            OnboardingEvent.OnEmailSkipClicked -> sendEffect(OnboardingEffect.AddYourEmail_NavigateToNext)
            OnboardingEvent.OnEmailBackClicked -> sendEffect(OnboardingEffect.AddYourEmail_NavigateBack)

            // DOB Screen
            OnboardingEvent.OnDobFieldClicked -> _uiState.update { it.copy(showDatePicker = true) }
            is OnboardingEvent.OnDobSelected -> {
                _uiState.update {
                    it.copy(
                        dob = event.selectedDob,
                        showDatePicker = false,
                        isDobNextEnabled = isValidDob(event.selectedDob)
                    )
                }
            }
            OnboardingEvent.OnDatePickerDismissed -> _uiState.update { it.copy(showDatePicker = false) }
            OnboardingEvent.OnDobVerifyClicked -> verifyAndSubmitOnboarding()
            OnboardingEvent.OnDobBackClicked -> sendEffect(OnboardingEffect.EnterYourDOB_NavigateBack)
        }
    }

    private fun verifyAndSubmitOnboarding() {
        val currentDob = _uiState.value.dob
        if (isValidDob(currentDob)) {
            submitOnboarding()
        }
    }

    private fun submitOnboarding() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val domainModel = OnboardingDomain(
                fullName = _uiState.value.fullName,
                dob = _uiState.value.dob,
                email = _uiState.value.email.takeIf { it.isNotBlank() }
            )
            
            when (val response = onboardUserUseCase(domainModel)) {
                is NetworkResponse.Success -> {
                    sendEffect(OnboardingEffect.EnterYourDOB_NavigateToNext)
                }
                is NetworkResponse.Error -> {
                    sendEffect(OnboardingEffect.ShowError(response.error.message))
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    // Validation Logic
    private fun isValidFullName(name: String): Boolean {
        val trimmedName = name.trim()
        if (trimmedName.isBlank()) return false
        val nameRegex = "^[a-zA-Z\\s.'-]{2,100}$".toRegex()
        return nameRegex.matches(trimmedName)
    }

    private fun isValidDob(dob: String): Boolean {
        return dob.isNotBlank()
    }

    private fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()
        return emailRegex.matches(email)
    }

    private fun sendEffect(effect: OnboardingEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
