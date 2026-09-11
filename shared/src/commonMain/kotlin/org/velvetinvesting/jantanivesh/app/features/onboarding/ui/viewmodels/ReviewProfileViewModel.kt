package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels

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
import org.velvetinvesting.jantanivesh.app.core.utils.isoUtcToIsoDate
import org.velvetinvesting.jantanivesh.app.features.core.domain.usecase.GetUserDataUseCase
import org.velvetinvesting.jantanivesh.app.features.fd.domain.utils.trimDoubleTo
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.Gender
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.GeoLocation
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.IncomeSlab
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.Occupation
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.SourceOfFund
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.InvestorProfile
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.KYCError
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.MaritalStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.GetKycFormStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.SubmitInvestorProfileUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.OnboardingInput

/**
 * Falls back to these when permission is held but the device still cannot produce a fix: the API
 * rejects a profile without a geo stamp, so a permitted submission must always carry one.
 */
private val DEFAULT_COORDINATES = GeoCoordinates(latitude = 28.5355, longitude = 77.3910)

/** One initial attempt plus two retries before giving up on the device and using the default. */
private const val LOCATION_ATTEMPTS = 3

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
    /** Digits only — the user enters an amount and the slab is derived from it. */
    val annualIncome: String = "",
    val isPepConfirmed: Boolean = false,
    val isResidentConfirmed: Boolean = false,
    val isLoading: Boolean = false,
    /** Covers the initial `GET /user/` read that prefills the form — the whole screen is a loader. */
    val isScreenLoading: Boolean = true,
    val showError: Boolean = false,
    val error: String = "",
    /** True while the fix that precedes the submit call is being taken. */
    val isFetchingLocation: Boolean = false,
    /**
     * True once the email OTP has been verified: the address is then settled, so the field is
     * neither shown nor editable. Every other prefilled value stays editable here.
     */
    val isEmailLocked: Boolean = false
) {
    /** Null until the field holds a usable amount; this is what the request is built from. */
    val annualIncomeSlab: IncomeSlab?
        get() = annualIncome.toLongOrNull()?.takeIf { it >= 0 }?.let(IncomeSlab::forAmount)

    val canSubmit: Boolean
        get() = isPepConfirmed &&
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
                annualIncomeSlab != null
}

sealed interface ReviewProfileEvent {
    /** Retries the initial profile read after it failed. */
    data object OnRetryLoad : ReviewProfileEvent

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
    data class OnAnnualIncomeChange(val value: String) : ReviewProfileEvent
    data class OnPepConfirmChange(val isChecked: Boolean) : ReviewProfileEvent
    data class OnResidentConfirmChange(val isChecked: Boolean) : ReviewProfileEvent

    /**
     * Reports the outcome of the permission prompt the screen raises when the user taps submit.
     * There is no separate proceed event: the answer is what starts the submission.
     */
    data class OnLocationPermissionResult(val granted: Boolean) : ReviewProfileEvent

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
    private val submitInvestorProfile: SubmitInvestorProfileUseCase,
    private val getKycFormStatus: GetKycFormStatusUseCase,
    private val locationProvider: LocationProvider,
    private val getUserData: GetUserDataUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<ReviewProfileEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadUserData()
    }

    /**
     * Nothing on the form is usable until this lands, so the whole screen is a loader until it
     * does. Anything the backend does not have yet arrives blank and is typed in here.
     */
    private fun loadUserData() {
        viewModelScope.launch {
            update { it.copy(isScreenLoading = true, showError = false, error = "") }

            when (val result = getUserData()) {
                is NetworkResponse.Error -> update {
                    it.copy(
                        isScreenLoading = false,
                        showError = true,
                        error = result.error.message
                    )
                }

                is NetworkResponse.Success -> {
                    val user = result.data
                    update {
                        it.copy(
                            isScreenLoading = false,
                            showError = false,
                            error = "",
                            fullName = user.name,
                            // The API states the date of birth as a UTC timestamp; the form and
                            // the submit payload both want a plain `yyyy-MM-dd`.
                            dob = user.dob.isoUtcToIsoDate(),
                            email = user.email,
                            // A verified email is settled: the field is then neither shown nor
                            // editable.
                            isEmailLocked = user.onboarding.isEmailVerified &&
                                    user.email.isNotBlank()
                        )
                    }
                }
            }
        }
    }

    fun handleEvent(event: ReviewProfileEvent) {
        when (event) {
            is ReviewProfileEvent.OnFullNameChange ->
                update { it.copy(fullName = OnboardingInput.sanitizeName(event.value)) }

            is ReviewProfileEvent.OnEmailChange -> {
                if (_uiState.value.isEmailLocked) return
                update { it.copy(email = OnboardingInput.sanitizeEmail(event.value)) }
            }

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

            is ReviewProfileEvent.OnAnnualIncomeChange -> update {
                it.copy(
                    annualIncome = OnboardingInput.sanitizeDigits(
                        event.value,
                        OnboardingInput.ANNUAL_INCOME_MAX_DIGITS
                    )
                )
            }

            is ReviewProfileEvent.OnPepConfirmChange ->
                update { it.copy(isPepConfirmed = event.isChecked) }

            is ReviewProfileEvent.OnResidentConfirmChange ->
                update { it.copy(isResidentConfirmed = event.isChecked) }

            is ReviewProfileEvent.OnLocationPermissionResult ->
                onSubmit(isPermissionGranted = event.granted)

            ReviewProfileEvent.OnRetryLoad -> loadUserData()
            ReviewProfileEvent.OnESignReturned -> onESignReturned()
            is ReviewProfileEvent.OnSpouseNameChange -> {
                update { it.copy(spouseName = OnboardingInput.sanitizeName(event.value)) }
            }
        }
    }

    /**
     * The KYC stamp is taken as part of the submission rather than as its own step, so the fix is
     * always current and the user only presses one button. Permission is not optional — without
     * it the submission does not happen at all — but a device that cannot produce a fix despite
     * having permission must not block onboarding, hence the fallback to [DEFAULT_COORDINATES].
     */
    private fun onSubmit(isPermissionGranted: Boolean) {
        val state = _uiState.value
        if (state.isLoading || state.isFetchingLocation || !state.canSubmit) return

        if (!isPermissionGranted) {
            viewModelScope.launch {
                SnackBarController.showError(
                    "Location permission is needed to complete your profile. " +
                            "You can enable it in your device settings."
                )
            }
            return
        }

        viewModelScope.launch { submitProfile(resolveCoordinates()) }
    }

    /**
     * Only reached with permission in hand. A fix can still fail for transient reasons — a cold
     * radio, a provider that reports nothing on the first pass — so a failure is retried before
     * falling back. Anything that will not change between attempts falls back immediately.
     */
    private suspend fun resolveCoordinates(): GeoCoordinates {
        update { it.copy(isFetchingLocation = true) }
        try {
            repeat(LOCATION_ATTEMPTS) {
                when (val result = locationProvider.getCurrentLocation()) {
                    is LocationResult.Success -> return result.coordinates

                    LocationResult.PermissionDenied,
                    LocationResult.LocationDisabled -> return DEFAULT_COORDINATES

                    is LocationResult.Failed -> Unit
                }
            }
            return DEFAULT_COORDINATES
        } finally {
            update { it.copy(isFetchingLocation = false) }
        }
    }

    private suspend fun submitProfile(coordinates: GeoCoordinates) {
        val state = _uiState.value
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

    /** Only called once [ReviewProfileUiState.canSubmit] holds and a fix has been resolved. */
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
        // The API still speaks in slabs; the typed amount is only ever a means to pick one.
        incomeSlab = annualIncomeSlab?.apiValue.orEmpty(),
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
