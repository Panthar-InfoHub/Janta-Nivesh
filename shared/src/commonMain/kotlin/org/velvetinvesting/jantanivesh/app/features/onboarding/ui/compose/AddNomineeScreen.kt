package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.utils.formatMillisToIsoDate
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppDatePicker
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.CheckBoxCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddNomineeEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddNomineeUiState
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.OnboardingInput
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.NomineeDocumentType
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.NomineeRelation

/** Shared by every free-text name-like field on this screen. */
private val wordsKeyboardOptions = KeyboardOptions(
    capitalization = KeyboardCapitalization.Words,
    imeAction = ImeAction.Next
)

/**
 * Localized name of an identity document. Used for both the dropdown entries and the title of
 * the number field below them, so the two always read the same in every language.
 */
@Composable
private fun nomineeDocumentLabel(type: NomineeDocumentType?): String = stringResource(
    when (type) {
        NomineeDocumentType.PAN -> Res.string.nominee_document_pan
        NomineeDocumentType.AADHAAR -> Res.string.nominee_document_aadhaar
        NomineeDocumentType.DRIVING_LICENCE -> Res.string.nominee_document_driving_licence
        NomineeDocumentType.PASSPORT -> Res.string.nominee_document_oci_passport
        null -> Res.string.nominee_document_number
    }
)

@Composable
fun AddNomineeScreen(
    state: AddNomineeUiState,
    handleEvent: (AddNomineeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Resolved up front because the dropdown's textConvertor is not a composable scope.
    val documentLabels = NomineeDocumentType.entries.associateWith { nomineeDocumentLabel(it) }

    // Which nominee's date of birth the picker is currently editing, if any.
    var datePickerIndex by remember { mutableStateOf<Int?>(null) }

    datePickerIndex?.let { index ->
        AppDatePicker(
            show = true,
            selectedDate = null,
            onDismiss = { datePickerIndex = null },
            onDateSelected = { millis ->
                handleEvent(
                    AddNomineeEvent.OnDateOfBirthChanged(index, formatMillisToIsoDate(millis))
                )
            }
        )
    }

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
            contentPadding = PaddingValues(top = Spacing.dp24, bottom = Spacing.dp12)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "Add your Nominee/ " + stringResource(Res.string.add_your_nominee_label),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        text = "Who should get your money if something happens? Make sure it is a trusted person or family member./ " + stringResource(
                            Res.string.nominee_subtitle
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gray444
                    )
                }
            }

            item {
                CheckBoxCard(
                    text = "I will add Nominees later",
                    isChecked = state.addLater,
                    onCheckedChange = { handleEvent(AddNomineeEvent.OnAddLaterChanged(it)) }
                )
            }

            // Nothing is collected when the user opts to add nominees later.
            itemsIndexed(
                if (state.addLater) emptyList() else state.nominees
            ) { index, nominee ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
                    modifier = Modifier
                        .genericDropShadow()
                        .clip(RoundedCornerShape(Spacing.dp24))
                        .background(White)
                        .padding(Spacing.dp24)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Nominee ${index + 1}",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            if (state.nominees.size > 1) {
                                Icon(
                                    painter = painterResource(Res.drawable.delete_icon),
                                    contentDescription = "Discard Nominee",
                                    tint = Black,
                                    modifier = Modifier
                                        .size(Spacing.dp18)
                                        .clickable { handleEvent(AddNomineeEvent.OnDeleteNomineeClick(index)) }
                                )
                            }
                        }
                        HorizontalDivider(
                            thickness = Spacing.dp1,
                            color = Primary,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    TitledAppTextField(
                        title = "Nominee Name/" + stringResource(Res.string.nominee_name),
                        value = nominee.name,
                        onValueChange = { handleEvent(AddNomineeEvent.OnNameChanged(index, it)) },
                        placeholder = "",
                        mandatory = true,
                        keyboardType = KeyboardType.Text,
                        keyboardOptions = wordsKeyboardOptions,
                        modifier = Modifier.padding(top = Spacing.dp16)
                    )
                    DropDownSelector(
                        title = "Relationship/ " + stringResource(Res.string.relation),
                        value = nominee.relationship?.displayName ?: "",
                        onValueChange = {
                                handleEvent(AddNomineeEvent.OnRelationshipChanged(index, it))
                        },
                        list = NomineeRelation.entries,
                        placeholder = "Select relationship",
                        mandatory = true,
                        textConvertor = { it.displayName }
                    )
                    TitledAppTextField(
                        title = "Percentage Allocation/" + stringResource(Res.string.percentage_allocation),
                        value = nominee.percentageAllocation,
                        onValueChange = { handleEvent(AddNomineeEvent.OnPercentageAllocationChanged(index, it)) },
                        placeholder = "100",
                        mandatory = true,
                        keyboardType = KeyboardType.Number,
                        isError = state.allocationError != null,
                        supportingText = state.allocationError?.takeIf {
                            index == state.nominees.lastIndex
                        }?.let { error ->
                            { Text(error, style = MaterialTheme.typography.labelSmall) }
                        },
                        trailingIcon = {
                            Text("%", style = MaterialTheme.typography.bodyLarge, color = Gray444)
                        }
                    )
                    TitledDateField(
                        title = "Date of Birth/ " + stringResource(Res.string.date_of_birth),
                        value = nominee.dateOfBirth,
                        onClick = { datePickerIndex = index },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropDownSelector(
                        title = stringResource(Res.string.identity_type),
                        value = nominee.identityType?.let { nomineeDocumentLabel(it) } ?: "",
                        onValueChange = {
                                handleEvent(AddNomineeEvent.OnIdentityTypeChanged(index, it))
                        },
                        list = NomineeDocumentType.entries,
                        placeholder = "Select identity type",
                        mandatory = true,
                        textConvertor = { documentLabels.getValue(it) }
                    )
                    TitledAppTextField(
                        title = nomineeDocumentLabel(nominee.identityType),
                        value = nominee.panCard,
                        onValueChange = { handleEvent(AddNomineeEvent.OnPanCardChanged(index, it)) },
                        // Format examples rather than prose, so they need no translation.
                        placeholder = when (nominee.identityType) {
                            NomineeDocumentType.PAN -> "ABCDE1234F"
                            NomineeDocumentType.AADHAAR -> "1234"
                            else -> ""
                        },
                        mandatory = true,
                        keyboardType = if (nominee.identityType == NomineeDocumentType.AADHAAR) {
                            KeyboardType.Number
                        } else {
                            KeyboardType.Text
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            autoCorrectEnabled = false,
                            imeAction = ImeAction.Next
                        ),
                        isError = nominee.panCard.isNotEmpty() && !nominee.isDocumentNumberValid
                    )
                    TitledAppTextField(
                        title = "Email/ " + stringResource(Res.string.email),
                        value = nominee.email,
                        onValueChange = { handleEvent(AddNomineeEvent.OnEmailChanged(index, it)) },
                        keyboardType = KeyboardType.Email,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = false,
                            imeAction = ImeAction.Next
                        ),
                        placeholder = "name@example.com",
                        mandatory = true,
                        isError = nominee.email.isNotEmpty() &&
                                !OnboardingInput.isValidEmail(nominee.email)
                    )
                    TitledAppTextField(
                        title = "Phone/ " + stringResource(Res.string.phone_label),
                        value = nominee.phone,
                        onValueChange = { handleEvent(AddNomineeEvent.OnPhoneChanged(index, it)) },
                        keyboardType = KeyboardType.Phone,
                        placeholder = "9876543210",
                        mandatory = true,
                        prefix = { Text("+91 ") },
                        isError = nominee.phone.isNotEmpty() &&
                                !OnboardingInput.isValidPhone(nominee.phone)
                    )
                    TitledAppTextField(
                        title = "Address Line 1/ " + stringResource(Res.string.address_line_1_nominee),
                        value = nominee.addressLine1,
                        onValueChange = { handleEvent(AddNomineeEvent.OnAddressLine1Changed(index, it)) },
                        placeholder = "\n\n\n",
                        mandatory = true,
                        keyboardOptions = wordsKeyboardOptions
                    )
                    TitledAppTextField(
                        title = "Address Line 2/ " + stringResource(Res.string.address_line_2_nominee),
                        value = nominee.addressLine2,
                        onValueChange = { handleEvent(AddNomineeEvent.OnAddressLine2Changed(index, it)) },
                        placeholder = "\n\n\n",
                        keyboardOptions = wordsKeyboardOptions
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TitledAppTextField(
                            title = "City/ " + stringResource(Res.string.city),
                            value = nominee.city,
                            onValueChange = { handleEvent(AddNomineeEvent.OnCityChanged(index, it)) },
                            placeholder = "",
                            mandatory = true,
                            keyboardOptions = wordsKeyboardOptions,
                            modifier = Modifier.weight(1f)
                        )
                        TitledAppTextField(
                            title = "State/ " + stringResource(Res.string.state),
                            value = nominee.state,
                            onValueChange = { handleEvent(AddNomineeEvent.OnStateChanged(index, it)) },
                            placeholder = "",
                            mandatory = true,
                            keyboardOptions = wordsKeyboardOptions,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    TitledAppTextField(
                        title = "Postal Code/ " + stringResource(Res.string.pincode),
                        value = nominee.postalCode,
                        onValueChange = {
                            handleEvent(AddNomineeEvent.OnPostalCodeChanged(index, it))
                        },
                        placeholder = "560001",
                        mandatory = true,
                        keyboardType = KeyboardType.Number,
                        isError = nominee.postalCode.isNotEmpty() &&
                                !OnboardingInput.isValidPincode(nominee.postalCode)
                    )

                    if (index == state.nominees.lastIndex) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { handleEvent(AddNomineeEvent.OnAddAnotherNomineeClick) }
                                .padding(top = Spacing.dp24)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.plus_inside_circle_icon),
                                contentDescription = "Add another nominee",
                                tint = Gray444,
                                modifier = Modifier.size(Spacing.dp20)
                            )
                            Text(
                                "Add another nominee/ " + stringResource(Res.string.add_another_nominee),
                                style = MaterialTheme.typography.labelSmall,
                                color = Gray444
                            )
                        }
                    }
                }
            }
        }
        AppButton(
            text = "Confirm and proceed",
            onClick = { handleEvent(AddNomineeEvent.OnConfirmAndProceedClick) },
            loading = state.isLoading,
            enabled = state.canSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.dp24)
                .genericDropShadow()
        )
    }
}

@Preview(locale = "hi", heightDp = 1800, showBackground = true)
@Composable
private fun AddNomineeScreenPreview() {
    JantaNiveshTheme {
        AddNomineeScreen(
            state = AddNomineeUiState(addLater = false),
            handleEvent = {}
        )
    }
}