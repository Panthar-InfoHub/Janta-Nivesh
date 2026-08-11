package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.location.GeoCoordinates
import org.velvetinvesting.jantanivesh.app.core.location.LocationProvider
import org.velvetinvesting.jantanivesh.app.core.location.LocationResult
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.fd.domain.utils.trimDoubleTo
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.Gender
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.MaritalStatus
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.GeoLocation
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.IncomeSlab
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.Occupation
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.SourceOfFund
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.InvestorProfile
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.KYCError
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.usecases.GetKycFormStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.usecases.SubmitInvestorProfileUseCase

/**
 * Six decimals is roughly 0.1 m — more precision than a phone GPS delivers, and enough to keep
 * the displayed value stable. Kotlin has no common `String.format`, hence the manual rounding.
 */
private fun Double.toDisplayCoordinate(): String {
    val scaled = kotlin.math.round(this * 1_000_000.0).toLong()
    val sign = if (scaled < 0) "-" else ""
    val magnitude = kotlin.math.abs(scaled)
    val whole = magnitude / 1_000_000
    val fraction = (magnitude % 1_000_000).toString().padStart(6, '0')
    return "$sign$whole.$fraction"
}

data class ReviewProfileUiState(
    val fullName: String = "",
    val email: String = "",
    val dob: String = "",
    val gender: Gender? = null,
    val address: String = "",
    val pincode: String = "",
    val city: String = "",
    val maritalStatus: MaritalStatus? = null,
    val fatherName: String = "",
    val spouseName: String = "",
    val placeOfBirth: String = "",
    val occupation: Occupation? = null,
    val sourceOfFund: SourceOfFund? = null,
    val annualIncome: IncomeSlab? = null,
    val isPepConfirmed: Boolean = false,
    val isResidentConfirmed: Boolean = false,
    val isLoading: Boolean = false,
    /** Only ever set from a GPS fix — the coordinate fields are display-only. */
    val coordinates: GeoCoordinates? = null,
    val isFetchingLocation: Boolean = false
) {
    val latitudeText: String
        get() = coordinates?.latitude?.toDisplayCoordinate().orEmpty()

    val longitudeText: String
        get() = coordinates?.longitude?.toDisplayCoordinate().orEmpty()

    val canSubmit: Boolean
        get() = coordinates != null &&
                !isFetchingLocation &&
                isPepConfirmed &&
                isResidentConfirmed &&
                OnboardingInput.isFilled(fullName) &&
                OnboardingInput.isValidEmail(email) &&
                OnboardingInput.isValidIsoDate(dob) &&
                gender != null &&
                OnboardingInput.isFilled(address) &&
                OnboardingInput.isValidPincode(pincode) &&
                OnboardingInput.isFilled(city) &&
                maritalStatus != null &&
                OnboardingInput.isFilled(fatherName) &&
                OnboardingInput.isFilled(placeOfBirth) &&
                occupation != null &&
                sourceOfFund != null &&
                annualIncome != null
}

sealed interface ReviewProfileEvent {
    data class OnFullNameChange(val value: String) : ReviewProfileEvent
    data class OnEmailChange(val value: String) : ReviewProfileEvent
    data class OnDobChange(val value: String) : ReviewProfileEvent
    data class OnGenderChange(val value: Gender) : ReviewProfileEvent
    data class OnAddressChange(val value: String) : ReviewProfileEvent
    data class OnPincodeChange(val value: String) : ReviewProfileEvent
    data class OnCityChange(val value: String) : ReviewProfileEvent
    data class OnMaritalStatusChange(val value: MaritalStatus) : ReviewProfileEvent
    data class OnFatherNameChange(val value: String) : ReviewProfileEvent
    data class OnSpouseNameChange(val value: String) : ReviewProfileEvent
    data class OnPlaceOfBirthChange(val value: String) : ReviewProfileEvent
    data class OnOccupationChange(val value: Occupation) : ReviewProfileEvent
    data class OnSourceOfFundChange(val value: SourceOfFund) : ReviewProfileEvent
    data class OnAnnualIncomeChange(val value: IncomeSlab) : ReviewProfileEvent
    data class OnPepConfirmChange(val isChecked: Boolean) : ReviewProfileEvent
    data class OnResidentConfirmChange(val isChecked: Boolean) : ReviewProfileEvent

    /** Reports the outcome of the permission prompt the screen raised. */
    data class OnLocationPermissionResult(val granted: Boolean) : ReviewProfileEvent
    data object OnProceedClick : ReviewProfileEvent

    /**
     * Raised once the user comes back from the eSign web view. Whether the document was actually
     * signed is only knowable from the KYC form status, so the return re-reads it.
     */
    data object OnESignReturned : ReviewProfileEvent
}

sealed interface ReviewProfileEffect {
    data class OpenESignWebView(val url: String) : ReviewProfileEffect

    /** Profile saved and the KYC form has nothing left to sign — onboarding can move on. */
    data object ProfileCompleted : ReviewProfileEffect
}

class ReviewProfileViewModel(
    /** Prefills the email field; the user can still correct it here. */
    email: String,
    private val submitInvestorProfile: SubmitInvestorProfileUseCase,
    private val getKycFormStatus: GetKycFormStatusUseCase,
    private val locationProvider: LocationProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ReviewProfileUiState(email = OnboardingInput.sanitizeEmail(email))
    )
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<ReviewProfileEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: ReviewProfileEvent) {
        when (event) {
            is ReviewProfileEvent.OnFullNameChange ->
                update { it.copy(fullName = OnboardingInput.sanitizeName(event.value)) }

            is ReviewProfileEvent.OnEmailChange ->
                update { it.copy(email = OnboardingInput.sanitizeEmail(event.value)) }

            // Always `yyyy-MM-dd` from the date picker; the field itself is read-only.
            is ReviewProfileEvent.OnDobChange -> update { it.copy(dob = event.value) }
            is ReviewProfileEvent.OnGenderChange -> update { it.copy(gender = event.value) }
            is ReviewProfileEvent.OnAddressChange ->
                update { it.copy(address = OnboardingInput.sanitizeText(event.value, 200)) }

            is ReviewProfileEvent.OnPincodeChange -> update {
                it.copy(
                    pincode = OnboardingInput.sanitizeDigits(
                        event.value,
                        OnboardingInput.PINCODE_LENGTH
                    )
                )
            }

            is ReviewProfileEvent.OnCityChange ->
                update { it.copy(city = OnboardingInput.sanitizeName(event.value)) }

            is ReviewProfileEvent.OnMaritalStatusChange -> {
                update { it.copy(maritalStatus = event.value) }
                if (event.value == MaritalStatus.UNMARRIED){
                    update { it.copy(spouseName = "") }
                }
            }

            is ReviewProfileEvent.OnFatherNameChange ->
                update { it.copy(fatherName = OnboardingInput.sanitizeName(event.value)) }

            is ReviewProfileEvent.OnPlaceOfBirthChange ->
                update { it.copy(placeOfBirth = OnboardingInput.sanitizeName(event.value)) }

            is ReviewProfileEvent.OnOccupationChange ->
                update { it.copy(occupation = event.value) }

            is ReviewProfileEvent.OnSourceOfFundChange ->
                update { it.copy(sourceOfFund = event.value) }

            is ReviewProfileEvent.OnAnnualIncomeChange ->
                update { it.copy(annualIncome = event.value) }

            is ReviewProfileEvent.OnPepConfirmChange ->
                update { it.copy(isPepConfirmed = event.isChecked) }

            is ReviewProfileEvent.OnResidentConfirmChange ->
                update { it.copy(isResidentConfirmed = event.isChecked) }

            is ReviewProfileEvent.OnLocationPermissionResult ->
                onLocationPermissionResult(event.granted)

            ReviewProfileEvent.OnProceedClick -> onProceedClick()
            ReviewProfileEvent.OnESignReturned -> onESignReturned()
            is ReviewProfileEvent.OnSpouseNameChange -> {
                update { it.copy(spouseName = OnboardingInput.sanitizeName(event.value)) }
            }
        }
    }

    private fun onLocationPermissionResult(granted: Boolean) {
        if (_uiState.value.isFetchingLocation) return

        if (!granted) {
            viewModelScope.launch {
                SnackBarController.showError(
                    "Location permission is needed to complete your profile. " +
                            "You can enable it in your device settings."
                )
            }
            return
        }

        viewModelScope.launch {
            update { it.copy(isFetchingLocation = true) }
            try {
                when (val result = locationProvider.getCurrentLocation()) {
                    is LocationResult.Success ->
                        update { it.copy(coordinates = result.coordinates) }

                    LocationResult.PermissionDenied -> SnackBarController.showError(
                        "Location permission is needed to complete your profile."
                    )

                    LocationResult.LocationDisabled -> SnackBarController.showError(
                        "Please turn on location services and try again."
                    )

                    is LocationResult.Failed -> SnackBarController.showError(result.message)
                }
            } finally {
                update { it.copy(isFetchingLocation = false) }
            }
        }
    }

    private fun onProceedClick() {
        val state = _uiState.value
        if (state.isLoading || !state.canSubmit) return
        val coordinates = state.coordinates ?: return

        viewModelScope.launch {
            update { it.copy(isLoading = true) }

            when (val result = submitInvestorProfile(state.toInvestorProfile(coordinates))) {
                is NetworkResponse.Error -> {
                    update { it.copy(isLoading = false) }
                    SnackBarController.showError(result.error.message)
                }

                // Saving the profile can be what unlocks the eSign, so the form status decides
                // whether the user still has a document to sign before moving on.
                is NetworkResponse.Success -> refreshESignStatus(isReturningFromWebView = false)
            }
        }
    }

    private fun onESignReturned() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            update { it.copy(isLoading = true) }
            refreshESignStatus(isReturningFromWebView = true)
        }
    }

    /**
     * A null eSign URL means there is nothing outstanding to sign, which is the only signal the
     * flow needs — before the web view it means "no eSign required", and after it means the
     * document has been signed and the link consumed.
     */
    private suspend fun refreshESignStatus(isReturningFromWebView: Boolean) {
        when (val statusResult = getKycFormStatus()) {
            is NetworkResponse.Error -> {
                if (statusResult.error.type == KYCError.KYC_FORM_NOT_FOUND){
                    _effect.send(ReviewProfileEffect.ProfileCompleted)
                }
                else{
                    update { it.copy(isLoading = false) }
                    SnackBarController.showError(statusResult.error.message)
                }

            }

            is NetworkResponse.Success -> {
                update { it.copy(isLoading = false) }

                val status = statusResult.data
                val esignUrl = status.esignUrl

                when {
                    status.isESignSuccessful || esignUrl.isNullOrBlank() ->
                        _effect.send(ReviewProfileEffect.ProfileCompleted)

                    isReturningFromWebView -> SnackBarController.showError(
                        "Your eSign is not complete yet. Please finish signing to continue."
                    )

                    else -> _effect.send(ReviewProfileEffect.OpenESignWebView(esignUrl))
                }
            }
        }
    }

    /** Only called once [ReviewProfileUiState.canSubmit] holds, which requires a GPS fix. */
    private fun ReviewProfileUiState.toInvestorProfile(
        coordinates: GeoCoordinates
    ) = InvestorProfile(
        email = email.trim(),
        fullName = fullName.trim(),
        dob = dob.trim(),
        gender = gender?.name?.lowercase().orEmpty(),
        address = address.trim(),
        pincode = pincode.trim(),
        city = city.trim(),
        maritalStatus = maritalStatus?.name?.lowercase().orEmpty(),
        fatherName = fatherName.trim(),
        spouseName = spouseName.trim(),
        placeOfBirth = placeOfBirth.trim(),
        occupation = occupation?.apiValue.orEmpty(),
        sourceOfFund = sourceOfFund?.apiValue.orEmpty(),
        incomeSlab = annualIncome?.apiValue.orEmpty(),
        pepConfirmed = isPepConfirmed,
        residencyConfirmed = isResidentConfirmed,
        geoLocation = GeoLocation(
            latitude = coordinates.latitude.trimDoubleTo(2),
            longitude = coordinates.longitude.trimDoubleTo(2)
        )
    )

    private inline fun update(block: (ReviewProfileUiState) -> ReviewProfileUiState) {
        _uiState.update(block)
    }
}
