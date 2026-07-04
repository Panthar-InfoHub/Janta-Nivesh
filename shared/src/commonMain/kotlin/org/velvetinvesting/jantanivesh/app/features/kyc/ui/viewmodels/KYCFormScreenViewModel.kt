package org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.core.domain.usecase.GetUserDataUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.model.KycFormDataDomain
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.GetDigiLockerDetailsUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.UploadKycFormDataUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.KycFormUiState

data class KYCFormScreenUiState(
    val isScreenLoading: Boolean = true,
    val isButtonLoading: Boolean = false,
    val formState: KycFormUiState = KycFormUiState()
)

sealed interface KYCFormScreenEvent {

    data class OnNameChanged(val name: String) : KYCFormScreenEvent
    data class OnFatherNameChanged(val name: String) : KYCFormScreenEvent
    data class OnMotherNameChanged(val name: String) : KYCFormScreenEvent
    data class OnOccupationChanged(val description: String, val code: String) : KYCFormScreenEvent
    data class OnMaritalStatusChanged(val status: String) : KYCFormScreenEvent
    data class OnGenderChanged(val gender: String) : KYCFormScreenEvent
    data class OnPanNumberChanged(val pan: String) : KYCFormScreenEvent
    data class OnPlaceOfBirthChanged(val place: String) : KYCFormScreenEvent
    data object OnSubmitClicked : KYCFormScreenEvent
    data object LoadInitialData : KYCFormScreenEvent
}

sealed interface KYCFormScreenEffect {
    data object NavigateToImageUpload : KYCFormScreenEffect
    data class ShowError(val message: String, val navigateBack: Boolean = false) : KYCFormScreenEffect
}

class KYCFormScreenViewModel(
    private val getDigiLockerDetailsUseCase: GetDigiLockerDetailsUseCase,
    private val uploadKycFormDataUseCase: UploadKycFormDataUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(KYCFormScreenUiState())
    val uiState: StateFlow<KYCFormScreenUiState> = _uiState.asStateFlow()

    private val _effect = Channel<KYCFormScreenEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        handleEvent(KYCFormScreenEvent.LoadInitialData)
    }

    fun handleEvent(event: KYCFormScreenEvent) {
        when (event) {
            is KYCFormScreenEvent.OnFatherNameChanged -> _uiState.update { 
                it.copy(formState = it.formState.copy(fatherName = event.name.toUpperCase(Locale.current)))
            }
            is KYCFormScreenEvent.OnMotherNameChanged -> _uiState.update { 
                it.copy(formState = it.formState.copy(motherName = event.name.toUpperCase(Locale.current)))
            }
            is KYCFormScreenEvent.OnOccupationChanged -> _uiState.update { 
                it.copy(formState = it.formState.copy(occupationDescription = event.description, occupationCode = event.code)) 
            }
            is KYCFormScreenEvent.OnMaritalStatusChanged -> _uiState.update { 
                it.copy(formState = it.formState.copy(maritalStatus = event.status)) 
            }
            is KYCFormScreenEvent.OnGenderChanged -> _uiState.update {
                it.copy(formState = it.formState.copy(gender = event.gender))
            }
            is KYCFormScreenEvent.OnPanNumberChanged -> _uiState.update {
                it.copy(formState = it.formState.copy(panNumber = event.pan.toUpperCase(Locale.current)))
            }
            is KYCFormScreenEvent.OnPlaceOfBirthChanged -> _uiState.update {
                it.copy(formState = it.formState.copy(placeOfBirth = event.place.toUpperCase(Locale.current)))
            }
            KYCFormScreenEvent.OnSubmitClicked -> submitForm()
            KYCFormScreenEvent.LoadInitialData -> loadInitialData()
            is KYCFormScreenEvent.OnNameChanged -> _uiState.update {
                it.copy(formState = it.formState.copy(name = event.name.toUpperCase(Locale.current)))
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isScreenLoading = true) }

            val digiLockerDeferred = async { getDigiLockerDetailsUseCase() }
            val userDataDeferred = async { getUserDataUseCase() }

            val digiLockerResult = digiLockerDeferred.await()
            val userDataResult = userDataDeferred.await()

            if (digiLockerResult is NetworkResponse.Error) {
                sendEffect(
                    KYCFormScreenEffect.ShowError(
                        digiLockerResult.error.message,
                        true
                    )
                )
                _uiState.update { it.copy(isScreenLoading = false) }
                return@launch
            }

            if (userDataResult is NetworkResponse.Error) {
                sendEffect(
                    KYCFormScreenEffect.ShowError(
                        userDataResult.error.message,
                        true
                    )
                )
                _uiState.update { it.copy(isScreenLoading = false) }
                return@launch
            }

            val digiLockerData =
                (digiLockerResult as NetworkResponse.Success).data

            val userData =
                (userDataResult as NetworkResponse.Success).data

            _uiState.update {
                it.copy(
                    formState = it.formState.copy(
                        name = digiLockerData.fullName,
                        dob = digiLockerData.dob,
                        gender = if (digiLockerData.gender == "MALE") "M" else "F",
                        aadhaarNumber = digiLockerData.uid,

                        emailId = userData.email,
                        mobileNumber = userData.mobile,
                    )
                )
            }

            _uiState.update {
                it.copy(isScreenLoading = false)
            }
        }
    }

    private fun submitForm() {
        viewModelScope.launch {
            val state = _uiState.value.formState
            if (!state.isValid()) {
                sendEffect(KYCFormScreenEffect.ShowError("Please fill all required fields"))
                return@launch
            }

            val formData = KycFormDataDomain(
                aadhaarNumber = state.aadhaarNumber,
                dob = state.dob,
                emailId = state.emailId,
                fatherName = state.fatherName,
                fatherTitle = state.fatherTitle,
                gender = state.gender,
                kycAccountCode = state.kycAccountCode,
                maritalStatus = state.maritalStatus,
                mobileNumber = state.mobileNumber,
                motherName = state.motherName,
                motherTitle = state.motherTitle,
                name = state.name,
                occupationCode = state.occupationCode,
                occupationDescription = state.occupationDescription,
                panNumber = state.panNumber,
                placeOfBirth = state.placeOfBirth,
                residentialStatus = state.residentialStatus
            )

            _uiState.update { it.copy(isButtonLoading = true) }
            when (val response = uploadKycFormDataUseCase(formData)) {
                is NetworkResponse.Success -> {
                    sendEffect(KYCFormScreenEffect.NavigateToImageUpload)
                }
                is NetworkResponse.Error -> {
                    sendEffect(KYCFormScreenEffect.ShowError(response.error.message))
                }
            }
            _uiState.update { it.copy(isButtonLoading = false) }
        }
    }

    private fun sendEffect(effect: KYCFormScreenEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
