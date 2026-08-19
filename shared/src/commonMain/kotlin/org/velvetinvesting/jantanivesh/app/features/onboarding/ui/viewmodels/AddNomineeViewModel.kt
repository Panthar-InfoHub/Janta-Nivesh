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
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.Nominee
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.NomineeAddress
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.SubmitNomineesUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.NomineeDocumentType
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.NomineeRelation
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.OnboardingInput

data class NomineeDetails(
    val name: String = "",
    val relationship: NomineeRelation? = null,
    val percentageAllocation: String = "",
    val dateOfBirth: String = "",
    val identityType: NomineeDocumentType? = null,
    val panCard: String = "",
    val email: String = "",
    val phone: String = "",
    val addressLine1: String = "",
    val addressLine2: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = ""
) {
    val isComplete: Boolean
        get() = OnboardingInput.isFilled(name) &&
                relationship != null &&
                (percentageAllocation.toIntOrNull() ?: 0) > 0 &&
                OnboardingInput.isValidIsoDate(dateOfBirth) &&
                identityType != null &&
                isDocumentNumberValid &&
                OnboardingInput.isValidEmail(email) &&
                OnboardingInput.isValidPhone(phone) &&
                OnboardingInput.isFilled(addressLine1) &&
                OnboardingInput.isFilled(city) &&
                OnboardingInput.isFilled(state) &&
                OnboardingInput.isValidPincode(postalCode)

    /** PAN has a checkable format and Aadhaar is exactly its last four digits. */
    val isDocumentNumberValid: Boolean
        get() = when (identityType) {
            NomineeDocumentType.PAN -> OnboardingInput.isValidPan(panCard)
            NomineeDocumentType.AADHAAR -> panCard.length == OnboardingInput.AADHAAR_SUFFIX_LENGTH
            else -> OnboardingInput.isFilled(panCard)
        }
}

data class AddNomineeUiState(
    val addLater: Boolean = false,
    val nominees: List<NomineeDetails> = listOf(NomineeDetails()),
    val isLoading: Boolean = false
) {
    private val totalAllocation: Int
        get() = nominees.sumOf { it.percentageAllocation.toIntOrNull() ?: 0 }

    /** Skipping needs no data at all; otherwise every nominee must be complete and add up to 100%. */
    val canSubmit: Boolean
        get() = addLater || (nominees.all { it.isComplete } && totalAllocation == TOTAL_ALLOCATION)

    /** Surfaced under the allocation fields so the 100% rule is visible before the button unlocks. */
    val allocationError: String?
        get() = when {
            addLater -> null
            totalAllocation == TOTAL_ALLOCATION -> null
            else -> "Allocation across nominees must total 100% (currently $totalAllocation%)"
        }

    private companion object {
        const val TOTAL_ALLOCATION = 100
    }
}

sealed interface AddNomineeEvent {
    data class OnAddLaterChanged(val isChecked: Boolean) : AddNomineeEvent
    data object OnAddAnotherNomineeClick : AddNomineeEvent
    data class OnDeleteNomineeClick(val index: Int) : AddNomineeEvent
    data class OnNameChanged(val index: Int, val name: String) : AddNomineeEvent
    data class OnRelationshipChanged(val index: Int, val relationship: NomineeRelation) : AddNomineeEvent
    data class OnPercentageAllocationChanged(val index: Int, val percentage: String) : AddNomineeEvent
    data class OnDateOfBirthChanged(val index: Int, val dob: String) : AddNomineeEvent
    data class OnIdentityTypeChanged(val index: Int, val type: NomineeDocumentType) : AddNomineeEvent
    data class OnPanCardChanged(val index: Int, val pan: String) : AddNomineeEvent
    data class OnEmailChanged(val index: Int, val email: String) : AddNomineeEvent
    data class OnPhoneChanged(val index: Int, val phone: String) : AddNomineeEvent
    data class OnAddressLine1Changed(val index: Int, val address: String) : AddNomineeEvent
    data class OnAddressLine2Changed(val index: Int, val address: String) : AddNomineeEvent
    data class OnCityChanged(val index: Int, val city: String) : AddNomineeEvent
    data class OnStateChanged(val index: Int, val state: String) : AddNomineeEvent
    data class OnPostalCodeChanged(val index: Int, val postalCode: String) : AddNomineeEvent
    data object OnConfirmAndProceedClick : AddNomineeEvent
}

sealed interface AddNomineeEffect {
    data object NomineesSubmitted : AddNomineeEffect
}

class AddNomineeViewModel(
    private val submitNominees: SubmitNomineesUseCase
) : ViewModel() {
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
            is AddNomineeEvent.OnPostalCodeChanged ->
                onPostalCodeChanged(event.index, event.postalCode)

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

    private fun onNameChanged(index: Int, name: String) = updateNominee(index) {
        it.copy(name = OnboardingInput.sanitizeName(name))
    }

    private fun onRelationshipChanged(
        index: Int,
        relationship: NomineeRelation
    ) = updateNominee(index) {
        it.copy(relationship = relationship)
    }

    private fun onPercentageAllocationChanged(
        index: Int,
        percentage: String
    ) = updateNominee(index) {
        it.copy(percentageAllocation = OnboardingInput.sanitizePercentage(percentage))
    }

    /** Always `yyyy-MM-dd` from the date picker; the field itself is read-only. */
    private fun onDateOfBirthChanged(index: Int, dob: String) = updateNominee(index) {
        it.copy(dateOfBirth = dob)
    }

    /**
     * Switching the document type re-sanitises what is already typed, since PAN is capped at ten
     * characters while the other documents are not.
     */
    private fun onIdentityTypeChanged(
        index: Int,
        type: NomineeDocumentType
    ) = updateNominee(index) {
        it.copy(
            identityType = type,
            panCard = sanitizeDocument(type, it.panCard)
        )
    }

    private fun onPanCardChanged(index: Int, pan: String) = updateNominee(index) {
        it.copy(panCard = sanitizeDocument(it.identityType, pan))
    }

    private fun onEmailChanged(index: Int, email: String) = updateNominee(index) {
        it.copy(email = OnboardingInput.sanitizeEmail(email))
    }

    private fun onPhoneChanged(index: Int, phone: String) = updateNominee(index) {
        it.copy(phone = OnboardingInput.sanitizeDigits(phone, OnboardingInput.PHONE_LENGTH))
    }

    private fun onAddressLine1Changed(index: Int, address: String) = updateNominee(index) {
        it.copy(addressLine1 = OnboardingInput.sanitizeText(address, 200))
    }

    private fun onAddressLine2Changed(index: Int, address: String) = updateNominee(index) {
        it.copy(addressLine2 = OnboardingInput.sanitizeText(address, 200))
    }

    private fun onCityChanged(index: Int, city: String) = updateNominee(index) {
        it.copy(city = OnboardingInput.sanitizeName(city))
    }

    private fun onStateChanged(index: Int, stateValue: String) = updateNominee(index) {
        it.copy(state = OnboardingInput.sanitizeName(stateValue))
    }

    private fun onPostalCodeChanged(index: Int, postalCode: String) = updateNominee(index) {
        it.copy(
            postalCode = OnboardingInput.sanitizeDigits(postalCode, OnboardingInput.PINCODE_LENGTH)
        )
    }

    private fun sanitizeDocument(type: NomineeDocumentType?, value: String) = when (type) {
        NomineeDocumentType.PAN -> OnboardingInput.sanitizePan(value)
        NomineeDocumentType.AADHAAR ->
            OnboardingInput.sanitizeDigits(value, OnboardingInput.AADHAAR_SUFFIX_LENGTH)

        else -> OnboardingInput.sanitizeDocumentNumber(value)
    }

    private fun updateNominee(index: Int, transform: (NomineeDetails) -> NomineeDetails) {
        _uiState.update { state ->
            state.copy(
                nominees = state.nominees.mapIndexed { i, nominee ->
                    if (i == index) transform(nominee) else nominee
                }
            )
        }
    }

    private fun onConfirmAndProceedClick() {
        val state = _uiState.value
        if (state.isLoading || !state.canSubmit) return

        // "Add nominees later" is submitted as a skip, so the collected rows are dropped.
        val nominees = if (state.addLater) emptyList() else state.nominees.map { it.toNominee() }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                when (val result = submitNominees(nominees)) {
                    is NetworkResponse.Error -> SnackBarController.showError(result.error.message)
                    is NetworkResponse.Success -> _effect.send(AddNomineeEffect.NomineesSubmitted)
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun NomineeDetails.toNominee() = Nominee(
        name = name.trim(),
        relationship = relationship?.apiValue.orEmpty(),
        percentageAllocation = percentageAllocation.toIntOrNull() ?: 0,
        dob = dateOfBirth.trim(),
        documentType = identityType?.apiValue.orEmpty(),
        documentNumber = panCard.trim(),
        emailAddress = email.trim(),
        phoneIsd = DEFAULT_ISD,
        phoneNumber = phone.trim(),
        address = NomineeAddress(
            // The API takes a single address line; line 2 is appended when the user filled it in.
            line1 = listOf(addressLine1, addressLine2)
                .filter { it.isNotBlank() }
                .joinToString(", ") { it.trim() },
            city = city.trim(),
            state = state.trim(),
            postalCode = postalCode.trim(),
            country = DEFAULT_COUNTRY
        )
    )

    private companion object {
        const val DEFAULT_ISD = "91"
        const val DEFAULT_COUNTRY = "IN"
    }
}