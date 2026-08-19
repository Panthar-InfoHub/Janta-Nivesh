package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.address_label
import jantanivesh.shared.generated.resources.annual_income_label
import jantanivesh.shared.generated.resources.city
import jantanivesh.shared.generated.resources.confirm_and_proceed
import jantanivesh.shared.generated.resources.date_of_birth
import jantanivesh.shared.generated.resources.kyc_form_full_name_label
import jantanivesh.shared.generated.resources.kyc_form_gender_label
import jantanivesh.shared.generated.resources.occupation
import jantanivesh.shared.generated.resources.pep_confirmation
import jantanivesh.shared.generated.resources.pincode
import jantanivesh.shared.generated.resources.resident_confirmation
import jantanivesh.shared.generated.resources.review_profile_subtitle
import jantanivesh.shared.generated.resources.review_profile_title
import jantanivesh.shared.generated.resources.source_of_fund_label
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.location.rememberLocationPermissionRequester
import org.velvetinvesting.jantanivesh.app.core.utils.formatMillisToIsoDate
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AgreementCheckBoxCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppDatePicker
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.InvertedAppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.Gender
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.MaritalStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.Occupation
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.SourceOfFund
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.OnboardingInput
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ReviewProfileEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ReviewProfileUiState

/** Shared by every free-text name-like field on this screen. */
private val nameKeyboardOptions = KeyboardOptions(
    capitalization = KeyboardCapitalization.Words,
    imeAction = ImeAction.Next
)

@Composable
fun ReviewProfileScreen(
    state: ReviewProfileUiState,
    handleEvent: (ReviewProfileEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // The prompt has to be raised from the UI layer, so the screen owns the requester and the
    // view model only ever hears the answer.
    val requestLocationPermission = rememberLocationPermissionRequester { granted ->
        handleEvent(ReviewProfileEvent.OnLocationPermissionResult(granted))
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }
    AppDatePicker(
        show = showDatePicker,
        selectedDate = null,
        onDismiss = { showDatePicker = false },
        onDateSelected = { millis ->
            handleEvent(ReviewProfileEvent.OnDobChange(formatMillisToIsoDate(millis)))
        },
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = White)
            .padding(horizontal=Spacing.dp20)
            .clearFocusOnTap()
            .imePadding()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
            contentPadding = PaddingValues(top = Spacing.dp24)

        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "Review Profile/ " + stringResource(Res.string.review_profile_title),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        text = "We'll use this to keep your account secure and send important updates./ " + stringResource(
                            Res.string.review_profile_subtitle
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gray444
                    )
                }
            }

            item {
                TitledAppTextField(
                    title = "Full Name/ " + stringResource(Res.string.kyc_form_full_name_label),
                    value = state.fullName,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnFullNameChange(it)) },
                    placeholder = "",
                    mandatory = true,
                    keyboardOptions = nameKeyboardOptions
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp16), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                    TitledDateField(
                        title = "Date of Birth/ " + stringResource(Res.string.date_of_birth),
                        value = state.dob,
                        onClick = { showDatePicker = true },
                        modifier = Modifier.weight(1f)
                    )
                    DropDownSelector(
                        title = "Gender/" + stringResource(Res.string.kyc_form_gender_label),
                        value = state.gender?.displayName ?: "",
                        onValueChange = { selectedValue -> handleEvent(ReviewProfileEvent.OnGenderChange(selectedValue)) },
                        list = Gender.entries,
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        placeholder = "Select gender",
                        mandatory = true,
                        textConvertor = { it.displayName },
                    )
                }
            }
            // A verified address is final: it is neither editable nor shown here.
            if (!state.isEmailLocked) item {
                TitledAppTextField(
                    title = "Email",
                    value = state.email,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnEmailChange(it)) },
                    placeholder = "name@example.com",
                    mandatory = true,
                    keyboardType = KeyboardType.Email,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        imeAction = ImeAction.Next
                    ),
                    isError = state.email.isNotEmpty() && !OnboardingInput.isValidEmail(state.email)
                )
            }
            item {
                TitledAppTextField(
                    title = "Address/ " + stringResource(Res.string.address_label),
                    value = state.address,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnAddressChange(it)) },
                    placeholder = "\n\n\n",
                    mandatory = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp16), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                    TitledAppTextField(
                        title = "Pincode/ " + stringResource(Res.string.pincode),
                        value = state.pincode,
                        onValueChange = { handleEvent(ReviewProfileEvent.OnPincodeChange(it)) },
                        placeholder = "XXXXXX",
                        mandatory = true,
                        keyboardType = KeyboardType.Number,
                        isError = state.pincode.isNotEmpty() &&
                                !OnboardingInput.isValidPincode(state.pincode),
                        modifier = Modifier.weight(1f)
                    )
                    TitledAppTextField(
                        title = "City/ " + stringResource(Res.string.city),
                        value = state.city,
                        onValueChange = { handleEvent(ReviewProfileEvent.OnCityChange(it)) },
                        placeholder = "",
                        mandatory = true,
                        keyboardOptions = nameKeyboardOptions,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                DropDownSelector(
                    title = "Marital Status",
                    value = state.maritalStatus?.displayName ?: "",
                    onValueChange = { handleEvent(ReviewProfileEvent.OnMaritalStatusChange(it)) },
                    list = MaritalStatus.entries,
                    placeholder = "Select marital status",
                    mandatory = true,
                    textConvertor = { it.displayName },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (state.maritalStatus == MaritalStatus.MARRIED){
                item{
                    TitledAppTextField(
                        title = "Spouse's Name",
                        value = state.spouseName,
                        onValueChange = { handleEvent(ReviewProfileEvent.OnSpouseNameChange(it)) },
                        placeholder = "",
                        mandatory = true,
                        keyboardOptions = nameKeyboardOptions
                    )
                }
            }
            item {
                TitledAppTextField(
                    title = "Father's Name",
                    value = state.fatherName,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnFatherNameChange(it)) },
                    placeholder = "",
                    mandatory = true,
                    keyboardOptions = nameKeyboardOptions
                )
            }
            item {
                TitledAppTextField(
                    title = "Place of Birth",
                    value = state.placeOfBirth,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnPlaceOfBirthChange(it)) },
                    placeholder = "",
                    mandatory = true,
                    keyboardOptions = nameKeyboardOptions
                )
            }
            item {
                DropDownSelector(
                    title = "Occupation/ " + stringResource(Res.string.occupation),
                    value = state.occupation?.displayName ?: "",
                    onValueChange = { handleEvent(ReviewProfileEvent.OnOccupationChange(it)) },
                    list = Occupation.entries,
                    placeholder = "Select occupation",
                    mandatory = true,
                    textConvertor = { it.displayName },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                DropDownSelector(
                    title = "Source of Fund/ " + stringResource(Res.string.source_of_fund_label),
                    value = state.sourceOfFund?.displayName ?: "",
                    onValueChange = { handleEvent(ReviewProfileEvent.OnSourceOfFundChange(it)) },
                    list = SourceOfFund.entries,
                    placeholder = "Select source of fund",
                    mandatory = true,
                    textConvertor = { it.displayName },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                // The user enters a plain amount; the slab the API wants is derived from it and
                // echoed back below the field so the mapping stays visible.
                TitledAppTextField(
                    title = "Annual Income/ " + stringResource(Res.string.annual_income_label),
                    value = state.annualIncome,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnAnnualIncomeChange(it)) },
                    placeholder = "e.g. 600000",
                    mandatory = true,
                    keyboardType = KeyboardType.Number,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    prefix = { Text("₹") },
                    supportingText = state.annualIncomeSlab?.let { slab ->
                        { Text(slab.displayName, style = MaterialTheme.typography.labelSmall) }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                LocationSection(
                    latitude = state.latitudeText,
                    longitude = state.longitudeText,
                    isFetching = state.isFetchingLocation,
                    onFetchClick = { requestLocationPermission.request() }
                )
            }
            item {
                AgreementCheckBoxCard(
                    text = "I confirm that I am not a Politically Exposed Person (PEP) or related to any PEP as defined under PMLA guidelines/" + stringResource(
                        Res.string.pep_confirmation
                    ),
                    isConsentChecked = state.isPepConfirmed,
                    onConsentChange = { handleEvent(ReviewProfileEvent.OnPepConfirmChange(it)) }
                )
            }
            item {
                AgreementCheckBoxCard(
                    text = "I confirm that I am a resident, citizen, born in, a national and a tax resident of India and all the information provided on this page is accurate to the best of my knowledge./" + stringResource(
                        Res.string.resident_confirmation
                    ),
                    isConsentChecked = state.isResidentConfirmed,
                    onConsentChange = { handleEvent(ReviewProfileEvent.OnResidentConfirmChange(it)) }
                )
            }
        }
        AppButton(
            text = stringResource(Res.string.confirm_and_proceed),
            onClick = { handleEvent(ReviewProfileEvent.OnProceedClick) },
            loading = state.isLoading,
            enabled = state.canSubmit,
            modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp24).genericDropShadow()
        )
    }
}

/**
 * Coordinates are display-only: they are filled in solely by a GPS fix, so both fields are
 * disabled and the button is the only way to populate them.
 */
@Composable
private fun LocationSection(
    latitude: String,
    longitude: String,
    isFetching: Boolean,
    onFetchClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TitledAppTextField(
                title = "Latitude",
                value = latitude,
                onValueChange = { },
                placeholder = "--",
                mandatory = true,
                enabled = false,
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
            TitledAppTextField(
                title = "Longitude",
                value = longitude,
                onValueChange = { },
                placeholder = "--",
                mandatory = true,
                enabled = false,
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
        }

        InvertedAppButton(
            text = if (latitude.isEmpty()) "Fetch current location" else "Refresh location",
            onClick = onFetchClick,
            loading = isFetching,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "We record your location once, as required for KYC verification.",
            style = MaterialTheme.typography.labelSmall,
            color = Gray444
        )
    }
}

@Preview(locale = "hi", heightDp = 2000)
@Composable
private fun ReviewProfileScreenPreview() {
    JantaNiveshTheme {
        ReviewProfileScreen(
            state = ReviewProfileUiState(),
            handleEvent = {}
        )
    }
}