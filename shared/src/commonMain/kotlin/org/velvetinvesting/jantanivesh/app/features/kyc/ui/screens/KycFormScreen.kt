package org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.calendar_icon_desc
import jantanivesh.shared.generated.resources.dob_dropdown_icon
import jantanivesh.shared.generated.resources.kyc_form_aadhaar_number_label
import jantanivesh.shared.generated.resources.kyc_form_additional_details_title
import jantanivesh.shared.generated.resources.kyc_form_basic_info_title
import jantanivesh.shared.generated.resources.kyc_form_dob_label
import jantanivesh.shared.generated.resources.kyc_form_email_address_label
import jantanivesh.shared.generated.resources.kyc_form_father_name_label
import jantanivesh.shared.generated.resources.kyc_form_full_name_label
import jantanivesh.shared.generated.resources.kyc_form_gender_label
import jantanivesh.shared.generated.resources.kyc_form_identity_details_title
import jantanivesh.shared.generated.resources.kyc_form_marital_status_label
import jantanivesh.shared.generated.resources.kyc_form_mobile_number_label
import jantanivesh.shared.generated.resources.kyc_form_mother_name_label
import jantanivesh.shared.generated.resources.kyc_form_occupation_label
import jantanivesh.shared.generated.resources.kyc_form_pan_number_label
import jantanivesh.shared.generated.resources.kyc_form_place_of_birth_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoadingScreen
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCFormScreenEvent
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCFormScreenUiState
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.Gender
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.MaritalStatus
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.OccupationType

@Composable
fun KycFormScreen(
    state: KYCFormScreenUiState,
    onEvent: (KYCFormScreenEvent) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        bottomBar = {
            AppButton(
                text = "Continue",
                onClick = { onEvent(KYCFormScreenEvent.OnSubmitClicked) },
                loading = state.isButtonLoading,
                enabled = state.formState.isValid(),
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(Spacing.dp24)
            )
        },
        containerColor = White
    ) { pv ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(pv)
        ){
            BackHeader(
                title = "KYC Form",
                onBack = onBack,
                modifier = Modifier.padding(horizontal = Spacing.dp16)
            )
            if (state.isScreenLoading){
                LoadingScreen()
            }
            else{
                FormContent(
                    modifier = Modifier.weight(1f)
                        .fillMaxSize(),
                    state=state,
                    onEvent=onEvent
                )
            }
        }
    }
}

@Composable
private fun FormContent(
    modifier: Modifier = Modifier,
    state: KYCFormScreenUiState,
    onEvent: (KYCFormScreenEvent) -> Unit
){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
        contentPadding = PaddingValues(vertical = Spacing.dp12)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp24)
                    .shadow(
                        elevation = Spacing.dp12,
                        shape = RoundedCornerShape(Spacing.dp24),
                        ambientColor = SelectedBoxBorder,
                        spotColor = Color.Black.copy(alpha = 0.7f)
                    )
                    .clip(RoundedCornerShape(Spacing.dp24))
                    .background(White)
                    .padding(all = Spacing.dp24),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
            ) {
                Text(
                    text = "Basic Information/ " + "("+stringResource(Res.string.kyc_form_basic_info_title)+")",
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary
                )

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        "Full Name/ " + "("+stringResource(Res.string.kyc_form_full_name_label)+")",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    AppTextField(
                        value = state.formState.name,
                        onValueChange = {},
                        placeholder = {
                            Text(
                                "Enter your full name",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                color = GreyText
                            )
                        },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        "Date of Birth/ " + "("+stringResource(Res.string.kyc_form_dob_label)+")",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    AppTextField(
                        value = state.formState.dob,
                        onValueChange = {},
                        placeholder = {
                            Text(
                                "mm/dd/yyyy",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                color = GreyText
                            )
                        },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(
                                onClick = { },
                                modifier = Modifier.padding(end = Spacing.dp16)
                            ) {
                                Icon(
                                    painterResource(Res.drawable.dob_dropdown_icon),
                                    contentDescription = "("+stringResource(Res.string.calendar_icon_desc)+")",
                                    modifier = Modifier.size(Spacing.dp24)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        "Place of Birth/ " + "("+stringResource(Res.string.kyc_form_place_of_birth_label)+")",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    AppTextField(
                        value = state.formState.placeOfBirth,
                        onValueChange = {
                            onEvent(KYCFormScreenEvent.OnPlaceOfBirthChanged(it))
                        },
                        placeholder = {
                            Text(
                                "City of birth",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                color = GreyText
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        "Mobile Number/ " + "("+stringResource(Res.string.kyc_form_mobile_number_label)+")",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    AppTextField(
                        value = state.formState.mobileNumber,
                        onValueChange = {},
                        leadingIcon = {
                            Text(
                                "+91",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                color = GreyText
                            )
                        },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        "Email Address/ " + "("+stringResource(Res.string.kyc_form_email_address_label)+")",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    AppTextField(
                        value = state.formState.emailId,
                        onValueChange = {},
                        placeholder = {
                            Text(
                                "name@example.com",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                color = GreyText
                            )
                        },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp24)
                    .shadow(
                        elevation = Spacing.dp16,
                        shape = RoundedCornerShape(Spacing.dp24),
                        ambientColor = SelectedBoxBorder,
                        spotColor = Color.Black
                    )
                    .clip(RoundedCornerShape(Spacing.dp24))
                    .background(White)
                    .padding(all = Spacing.dp24),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
            ) {
                Text(
                    text = "Identity Details/ " + "("+stringResource(Res.string.kyc_form_identity_details_title)+")",
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary
                )

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        "PAN Number/ " + "("+stringResource(Res.string.kyc_form_pan_number_label)+")",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    AppTextField(
                        value = state.formState.panNumber,
                        onValueChange = {
                            onEvent(KYCFormScreenEvent.OnPanNumberChanged(it))
                        },
                        placeholder = {
                            Text(
                                "ABCDE1234F",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                color = GreyText
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        "Aadhaar Number/ " + "("+stringResource(Res.string.kyc_form_aadhaar_number_label)+")",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    AppTextField(
                        value = maskAadhaar(state.formState.aadhaarNumber),
                        onValueChange = {},
                        placeholder = {
                            Text(
                                "XXXX XXXX XXXX",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                color = GreyText
                            )
                        },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        "Gender/ " + "("+stringResource(Res.string.kyc_form_gender_label)+")",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    DropDownSelector(
                        value = Gender.fromCode(state.formState.gender)?.displayName ?: "",
                        onValueChange = { onEvent(KYCFormScreenEvent.OnGenderChanged(it.code)) },
                        list = Gender.entries,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Select gender",
                        textConvertor = { it.displayName }
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        "Marital Status/ " + "("+stringResource(Res.string.kyc_form_marital_status_label)+")",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    DropDownSelector(
                        value = state.formState.maritalStatus,
                        onValueChange = {
                            onEvent(
                                KYCFormScreenEvent.OnMaritalStatusChanged(
                                    it.code
                                )
                            )
                        },
                        list = MaritalStatus.entries,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Select status",
                        textConvertor = { it.displayName }
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp24)
                    .shadow(
                        elevation = Spacing.dp16,
                        shape = RoundedCornerShape(Spacing.dp24),
                        ambientColor = SelectedBoxBorder,
                        spotColor = Color.Black
                    )
                    .clip(RoundedCornerShape(Spacing.dp24))
                    .background(White)
                    .padding(all = Spacing.dp24),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
            ) {
                Text(
                    text = "Additional Details/ " + "("+stringResource(Res.string.kyc_form_additional_details_title)+")",
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary
                )


                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        "Father's Name/ " + "("+stringResource(Res.string.kyc_form_father_name_label)+")",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    AppTextField(
                        value = state.formState.fatherName,
                        onValueChange = {
                            onEvent(KYCFormScreenEvent.OnFatherNameChanged(it))
                        },
                        placeholder = {
                            Text(
                                "Enter name",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                color = GreyText
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        "Mother's Name/ " + "("+stringResource(Res.string.kyc_form_mother_name_label)+")",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    AppTextField(
                        value = state.formState.motherName,
                        onValueChange = {
                            onEvent(KYCFormScreenEvent.OnMotherNameChanged(it))
                        },
                        placeholder = {
                            Text(
                                "Enter name",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                color = GreyText
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        "Occupation/ " + "("+stringResource(Res.string.kyc_form_occupation_label)+")",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    DropDownSelector(
                        value = state.formState.occupationDescription,
                        onValueChange = {
                            onEvent(
                                KYCFormScreenEvent.OnOccupationChanged(
                                    it.displayName,
                                    it.code
                                )
                            )
                        },
                        list = OccupationType.entries,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Select occupation",
                        textConvertor = { it.displayName }
                    )
                }
            }
        }
    }
}

private fun maskAadhaar(aadhaar: String): String {
    if (aadhaar.length <= 4) return aadhaar
    val visible = aadhaar.takeLast(4)
    val masked = "x".repeat(aadhaar.length - 4)
    return masked + visible
}

@Preview(heightDp = 2000, locale = "hi")
@Composable
fun KycFormScreenPreview() {
    JantaNiveshTheme {
        KycFormScreen(
            state = KYCFormScreenUiState(isScreenLoading = false),
            onEvent = {},
            onBack = {},
        )
    }
}