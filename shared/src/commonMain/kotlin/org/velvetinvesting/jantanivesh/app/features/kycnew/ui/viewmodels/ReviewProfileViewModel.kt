package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.Gender

data class ReviewProfileUiState(
    val fullName: String = "",
    val dob: String = "",
    val gender: Gender? = null,
    val address: String = "",
    val pincode: String = "",
    val city: String = "",
    val occupation: String = "",
    val sourceOfFund: String = "",
    val annualIncome: String = "",
    val isPepConfirmed: Boolean = false,
    val isResidentConfirmed: Boolean = false
)

sealed interface ReviewProfileEvent {
    data class OnFullNameChange(val value: String) : ReviewProfileEvent
    data class OnDobChange(val value: String) : ReviewProfileEvent
    data class OnGenderChange(val value: Gender) : ReviewProfileEvent
    data class OnAddressChange(val value: String) : ReviewProfileEvent
    data class OnPincodeChange(val value: String) : ReviewProfileEvent
    data class OnCityChange(val value: String) : ReviewProfileEvent
    data class OnOccupationChange(val value: String) : ReviewProfileEvent
    data class OnSourceOfFundChange(val value: String) : ReviewProfileEvent
    data class OnAnnualIncomeChange(val value: String) : ReviewProfileEvent
    data class OnPepConfirmChange(val isChecked: Boolean) : ReviewProfileEvent
    data class OnResidentConfirmChange(val isChecked: Boolean) : ReviewProfileEvent
    data object OnProceedClick : ReviewProfileEvent
}

sealed interface ReviewProfileEffect {
    data object OnProceedClick : ReviewProfileEffect
}

class ReviewProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<ReviewProfileEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: ReviewProfileEvent) {
        when (event) {
            is ReviewProfileEvent.OnFullNameChange -> onFullNameChange(event.value)
            is ReviewProfileEvent.OnDobChange -> onDobChange(event.value)
            is ReviewProfileEvent.OnGenderChange -> onGenderChange(event.value)
            is ReviewProfileEvent.OnAddressChange -> onAddressChange(event.value)
            is ReviewProfileEvent.OnPincodeChange -> onPincodeChange(event.value)
            is ReviewProfileEvent.OnCityChange -> onCityChange(event.value)
            is ReviewProfileEvent.OnOccupationChange -> onOccupationChange(event.value)
            is ReviewProfileEvent.OnSourceOfFundChange -> onSourceOfFundChange(event.value)
            is ReviewProfileEvent.OnAnnualIncomeChange -> onAnnualIncomeChange(event.value)
            is ReviewProfileEvent.OnPepConfirmChange -> onPepConfirmChange(event.isChecked)
            is ReviewProfileEvent.OnResidentConfirmChange -> onResidentConfirmChange(event.isChecked)
            ReviewProfileEvent.OnProceedClick -> onProceedClick()
        }
    }

    private fun onFullNameChange(value: String) {
        _uiState.update { it.copy(fullName = value) }
    }

    private fun onDobChange(value: String) {
        _uiState.update { it.copy(dob = value) }
    }

    private fun onGenderChange(value: Gender) {
        _uiState.update { it.copy(gender = value) }
    }

    private fun onAddressChange(value: String) {
        _uiState.update { it.copy(address = value) }
    }

    private fun onPincodeChange(value: String) {
        if (value.isEmpty() || value.all { it.isDigit() }) {
            _uiState.update { it.copy(pincode = value) }
        }
    }

    private fun onCityChange(value: String) {
        _uiState.update { it.copy(city = value) }
    }

    private fun onOccupationChange(value: String) {
        _uiState.update { it.copy(occupation = value) }
    }

    private fun onSourceOfFundChange(value: String) {
        _uiState.update { it.copy(sourceOfFund = value) }
    }

    private fun onAnnualIncomeChange(value: String) {
        _uiState.update { it.copy(annualIncome = value) }
    }

    private fun onPepConfirmChange(checked: Boolean) {
        _uiState.update { it.copy(isPepConfirmed = checked) }
    }

    private fun onResidentConfirmChange(checked: Boolean) {
        _uiState.update { it.copy(isResidentConfirmed = checked) }
    }

    private fun onProceedClick() {
        sendEffect(ReviewProfileEffect.OnProceedClick)
    }

    private fun sendEffect(effect: ReviewProfileEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}