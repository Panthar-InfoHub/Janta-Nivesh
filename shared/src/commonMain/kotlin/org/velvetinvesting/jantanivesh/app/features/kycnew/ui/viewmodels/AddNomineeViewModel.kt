package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.utils.tradingaccount.NomineeIdentityType
import org.velvetinvesting.jantanivesh.app.utils.tradingaccount.NomineeRelationship

data class NomineeDetails(
    val name: String = "",
    val relationship: NomineeRelationship? = null,
    val percentageAllocation: String = "",
    val dateOfBirth: String = "",
    val identityType: NomineeIdentityType? = null,
    val panCard: String = "",
    val email: String = "",
    val phone: String = "",
    val addressLine1: String = "",
    val addressLine2: String = "",
    val city: String = "",
    val state: String = ""
)

data class AddNomineeUiState(
    val addLater: Boolean = false,
    val nominees: List<NomineeDetails> = listOf(NomineeDetails()),
    val isLoading: Boolean = false
)

sealed interface AddNomineeEvent {
    data class OnAddLaterChanged(val isChecked: Boolean) : AddNomineeEvent
    data object OnAddAnotherNomineeClick : AddNomineeEvent
    data class OnDeleteNomineeClick(val index: Int) : AddNomineeEvent
    data class OnNameChanged(val index: Int, val name: String) : AddNomineeEvent
    data class OnRelationshipChanged(val index: Int, val relationship: NomineeRelationship) : AddNomineeEvent
    data class OnPercentageAllocationChanged(val index: Int, val percentage: String) : AddNomineeEvent
    data class OnDateOfBirthChanged(val index: Int, val dob: String) : AddNomineeEvent
    data class OnIdentityTypeChanged(val index: Int, val type: NomineeIdentityType) : AddNomineeEvent
    data class OnPanCardChanged(val index: Int, val pan: String) : AddNomineeEvent
    data class OnEmailChanged(val index: Int, val email: String) : AddNomineeEvent
    data class OnPhoneChanged(val index: Int, val phone: String) : AddNomineeEvent
    data class OnAddressLine1Changed(val index: Int, val address: String) : AddNomineeEvent
    data class OnAddressLine2Changed(val index: Int, val address: String) : AddNomineeEvent
    data class OnCityChanged(val index: Int, val city: String) : AddNomineeEvent
    data class OnStateChanged(val index: Int, val state: String) : AddNomineeEvent
    data object OnConfirmAndProceedClick : AddNomineeEvent
}

sealed interface AddNomineeEffect {
    data object NavigateToNext : AddNomineeEffect
}

class AddNomineeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AddNomineeUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<AddNomineeEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: AddNomineeEvent) {
        when (event) {
            is AddNomineeEvent.OnAddLaterChanged -> onAddLaterChanged(event.isChecked)
            AddNomineeEvent.OnAddAnotherNomineeClick -> onAddAnotherNomineeClick()
            is AddNomineeEvent.OnDeleteNomineeClick -> onDeleteNomineeClick(event.index)
            is AddNomineeEvent.OnNameChanged -> onNameChanged(event.index, event.name)
            is AddNomineeEvent.OnRelationshipChanged -> onRelationshipChanged(event.index, event.relationship)
            is AddNomineeEvent.OnPercentageAllocationChanged -> onPercentageAllocationChanged(event.index, event.percentage)
            is AddNomineeEvent.OnDateOfBirthChanged -> onDateOfBirthChanged(event.index, event.dob)
            is AddNomineeEvent.OnIdentityTypeChanged -> onIdentityTypeChanged(event.index, event.type)
            is AddNomineeEvent.OnPanCardChanged -> onPanCardChanged(event.index, event.pan)
            is AddNomineeEvent.OnEmailChanged -> onEmailChanged(event.index, event.email)
            is AddNomineeEvent.OnPhoneChanged -> onPhoneChanged(event.index, event.phone)
            is AddNomineeEvent.OnAddressLine1Changed -> onAddressLine1Changed(event.index, event.address)
            is AddNomineeEvent.OnAddressLine2Changed -> onAddressLine2Changed(event.index, event.address)
            is AddNomineeEvent.OnCityChanged -> onCityChanged(event.index, event.city)
            is AddNomineeEvent.OnStateChanged -> onStateChanged(event.index, event.state)
            AddNomineeEvent.OnConfirmAndProceedClick -> onConfirmAndProceedClick()
        }
    }

    private fun onAddLaterChanged(isChecked: Boolean) {
        _uiState.update { it.copy(addLater = isChecked) }
    }

    private fun onAddAnotherNomineeClick() {
        _uiState.update { state ->
            state.copy(nominees = state.nominees + NomineeDetails())
        }
    }

    private fun onDeleteNomineeClick(index: Int) {
        _uiState.update { state ->
            val updatedList = state.nominees.toMutableList().apply {
                if (size > 1) removeAt(index)
            }
            state.copy(nominees = updatedList)
        }
    }

    private fun onNameChanged(index: Int, name: String) {
        _uiState.update { state ->
            val updatedList = state.nominees.toMutableList()
            updatedList[index] = updatedList[index].copy(name = name)
            state.copy(nominees = updatedList)
        }
    }

    private fun onRelationshipChanged(index: Int, relationship: NomineeRelationship) {
        _uiState.update { state ->
            val updatedList = state.nominees.toMutableList()
            updatedList[index] = updatedList[index].copy(relationship = relationship)
            state.copy(nominees = updatedList)
        }
    }

    private fun onPercentageAllocationChanged(index: Int, percentage: String) {
        if (percentage.isEmpty() || percentage.all { it.isDigit() }) {
            _uiState.update { state ->
                val updatedList = state.nominees.toMutableList()
                updatedList[index] = updatedList[index].copy(percentageAllocation = percentage)
                state.copy(nominees = updatedList)
            }
        }
    }

    private fun onDateOfBirthChanged(index: Int, dob: String) {
        _uiState.update { state ->
            val updatedList = state.nominees.toMutableList()
            updatedList[index] = updatedList[index].copy(dateOfBirth = dob)
            state.copy(nominees = updatedList)
        }
    }

    private fun onIdentityTypeChanged(index: Int, type: NomineeIdentityType) {
        _uiState.update { state ->
            val updatedList = state.nominees.toMutableList()
            updatedList[index] = updatedList[index].copy(identityType = type)
            state.copy(nominees = updatedList)
        }
    }

    private fun onPanCardChanged(index: Int, pan: String) {
        _uiState.update { state ->
            val updatedList = state.nominees.toMutableList()
            updatedList[index] = updatedList[index].copy(panCard = pan)
            state.copy(nominees = updatedList)
        }
    }

    private fun onEmailChanged(index: Int, email: String) {
        _uiState.update { state ->
            val updatedList = state.nominees.toMutableList()
            updatedList[index] = updatedList[index].copy(email = email)
            state.copy(nominees = updatedList)
        }
    }

    private fun onPhoneChanged(index: Int, phone: String) {
        if (phone.isEmpty() || phone.all { it.isDigit() }) {
            _uiState.update { state ->
                val updatedList = state.nominees.toMutableList()
                updatedList[index] = updatedList[index].copy(phone = phone)
                state.copy(nominees = updatedList)
            }
        }
    }

    private fun onAddressLine1Changed(index: Int, address: String) {
        _uiState.update { state ->
            val updatedList = state.nominees.toMutableList()
            updatedList[index] = updatedList[index].copy(addressLine1 = address)
            state.copy(nominees = updatedList)
        }
    }

    private fun onAddressLine2Changed(index: Int, address: String) {
        _uiState.update { state ->
            val updatedList = state.nominees.toMutableList()
            updatedList[index] = updatedList[index].copy(addressLine2 = address)
            state.copy(nominees = updatedList)
        }
    }

    private fun onCityChanged(index: Int, city: String) {
        _uiState.update { state ->
            val updatedList = state.nominees.toMutableList()
            updatedList[index] = updatedList[index].copy(city = city)
            state.copy(nominees = updatedList)
        }
    }

    private fun onStateChanged(index: Int, stateValue: String) {
        _uiState.update { state ->
            val updatedList = state.nominees.toMutableList()
            updatedList[index] = updatedList[index].copy(state = stateValue)
            state.copy(nominees = updatedList)
        }
    }

    private fun onConfirmAndProceedClick() {
        _uiState.update { it.copy(isLoading = true) }
        sendEffect(AddNomineeEffect.NavigateToNext)
    }

    private fun sendEffect(effect: AddNomineeEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}