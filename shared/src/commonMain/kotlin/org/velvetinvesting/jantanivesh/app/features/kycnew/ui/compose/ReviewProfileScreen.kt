package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.address_label
import jantanivesh.shared.generated.resources.annual_income_label
import jantanivesh.shared.generated.resources.city
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
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AgreementCheckBoxCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppDatePicker
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.Gender
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.ReviewProfileEvent
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.ReviewProfileUiState

@Composable
fun ReviewProfileScreen(
    state: ReviewProfileUiState,
    handleEvent: (ReviewProfileEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    if (showDatePicker) {
        AppDatePicker(
            show = showDatePicker,
            selectedDate = null,
            onDismiss = { showDatePicker = false },
            onDateSelected = {
                handleEvent(ReviewProfileEvent.OnDobChange(it.toString()))
                showDatePicker = false
            },
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = White)
            .padding(Spacing.dp20)
            .clearFocusOnTap()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20)
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
                    placeholder = ""
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp16), modifier = Modifier.fillMaxWidth()){
                    TitledAppTextField(
                        title = "Date of Birth/ " + stringResource(Res.string.date_of_birth),
                        value = state.dob,
                        onValueChange = { handleEvent(ReviewProfileEvent.OnDobChange(it)) },
                        placeholder = "",
                        modifier = Modifier.clickable(onClick = { showDatePicker = true }).weight(1f)
                    )
                    DropDownSelector(
                        title = "Gender/" + stringResource(Res.string.kyc_form_gender_label),
                        value = state.gender?.displayName ?: "",
                        onValueChange = { selectedValue -> handleEvent(ReviewProfileEvent.OnGenderChange(selectedValue)) },
                        list = Gender.entries,
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        placeholder = "Select gender",
                        textConvertor = { it.displayName },
                    )
                }
            }
            item {
                TitledAppTextField(
                    title = "Address/ " + stringResource(Res.string.address_label),
                    value = state.address,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnAddressChange(it)) },
                    placeholder = "\n\n\n",
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp16), modifier = Modifier.fillMaxWidth()){
                    TitledAppTextField(
                        title = "Pincode/ " + stringResource(Res.string.pincode),
                        value = state.pincode,
                        onValueChange = { handleEvent(ReviewProfileEvent.OnPincodeChange(it)) },
                        placeholder = "",
                        modifier = Modifier.weight(1f)
                    )
                    TitledAppTextField(
                        title = "City/ " + stringResource(Res.string.city),
                        value = state.city,
                        onValueChange = { handleEvent(ReviewProfileEvent.OnCityChange(it)) },
                        placeholder = "",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                TitledAppTextField(
                    title = "Occupation/ " + stringResource(Res.string.occupation),
                    value = state.occupation,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnOccupationChange(it)) },
                    placeholder = ""
                )
            }
            item {
                TitledAppTextField(
                    title = "Source of Fund/ " + stringResource(Res.string.source_of_fund_label),
                    value = state.sourceOfFund,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnSourceOfFundChange(it)) },
                    placeholder = ""
                )
            }
            item {
                TitledAppTextField(
                    title = "Annual Income/ " + stringResource(Res.string.annual_income_label),
                    value = state.annualIncome,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnAnnualIncomeChange(it)) },
                    placeholder = ""
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

            item {
            }
        }
        AppButton(
            text = "Confirm and proceed",
            onClick = { handleEvent(ReviewProfileEvent.OnProceedClick) },
            modifier = Modifier.fillMaxWidth().genericDropShadow()
        )
    }
}

@Preview(locale = "hi")
@Composable
private fun ReviewProfileScreenPreview() {
    JantaNiveshTheme {
        ReviewProfileScreen(
            state = ReviewProfileUiState(),
            handleEvent = {}
        )
    }
}