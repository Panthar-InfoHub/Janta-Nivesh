package org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCFormScreenEvent
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCFormScreenUiState
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.Gender
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.MaritalStatus
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.OccupationType

@Preview(heightDp = 1500)
@Composable
fun KycFormScreenPreview() {
    JantaNiveshTheme {
        KycFormScreen(
            state = KYCFormScreenUiState(),
            onEvent = {},
            onBack = {}
        )
    }
}

@Composable
fun KycFormScreen(
    state: KYCFormScreenUiState,
    onEvent: (KYCFormScreenEvent) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            BackHeader(
                title = "KYC Form",
                onBack=onBack
            )
        },
        bottomBar = {
            AppButton(
                text = "Continue",
                onClick = { onEvent(KYCFormScreenEvent.OnSubmitClicked) },
                loading = state.isLoading,
                enabled = state.formState.isValid(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.dp24)
            )
        }
    ) { pv ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
                .padding(horizontal = Spacing.dp24),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
        ) {
            item {
                Text(
                    text = "Personal Details",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = Spacing.dp12)
                )
            }

            item {
                AppTextField(
                    value = state.formState.name,
                    onValueChange = {},
                    label = { Text("Full Name") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                AppTextField(
                    value = state.formState.dob,
                    onValueChange = {},
                    label = { Text("Date of Birth") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                AppTextField(
                    value = maskAadhaar(state.formState.aadhaarNumber),
                    onValueChange = {},
                    label = { Text("Aadhaar Number") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                AppTextField(
                    value = state.formState.emailId,
                    onValueChange = {},
                    label = { Text("Email") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                AppTextField(
                    value = state.formState.mobileNumber,
                    onValueChange = {},
                    label = { Text("Mobile Number") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                DropDownSelector(
                    value = Gender.entries
                        .find { it.code == state.formState.gender }
                        ?.displayName
                        .orEmpty(),
                    onValueChange = {
                        onEvent(KYCFormScreenEvent.OnGenderChanged(it.code))
                    },
                    placeHolder = "Select Gender",
                    label = "Gender",
                    list = Gender.entries,
                    textConvertor = { it.displayName },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                DropDownSelector(
                    value = MaritalStatus.entries
                        .find { it.code == state.formState.maritalStatus }
                        ?.displayName
                        .orEmpty(),
                    onValueChange = {
                        onEvent(KYCFormScreenEvent.OnMaritalStatusChanged(it.code))
                    },
                    placeHolder = "Select Marital Status",
                    label = "Marital Status",
                    list = MaritalStatus.entries,
                    textConvertor = { it.displayName },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                AppTextField(
                    value = state.formState.panNumber,
                    onValueChange = {
                        onEvent(KYCFormScreenEvent.OnPanNumberChanged(it))
                    },
                    label = { Text("PAN Number") },
                    placeholder = { Text("Enter PAN Number") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                DropDownSelector(
                    value = OccupationType.entries
                        .find { it.code == state.formState.occupationCode }
                        ?.displayName
                        .orEmpty(),
                    onValueChange = {
                        onEvent(
                            KYCFormScreenEvent.OnOccupationChanged(
                                it.displayName,
                                it.code
                            )
                        )
                    },
                    placeHolder = "Select Occupation",
                    label = "Occupation",
                    list = OccupationType.entries,
                    textConvertor = { "${it.code}: ${it.displayName}" },
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                AppTextField(
                    value = state.formState.fatherName,
                    onValueChange = {
                        onEvent(KYCFormScreenEvent.OnFatherNameChanged(it))
                    },
                    label = { Text("Father Name") },
                    placeholder = { Text("Enter Father Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                AppTextField(
                    value = state.formState.motherName,
                    onValueChange = {
                        onEvent(KYCFormScreenEvent.OnMotherNameChanged(it))
                    },
                    label = { Text("Mother Name") },
                    placeholder = { Text("Enter Mother Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                AppTextField(
                    value = state.formState.placeOfBirth,
                    onValueChange = {
                        onEvent(KYCFormScreenEvent.OnPlaceOfBirthChanged(it))
                    },
                    label = { Text("Place of Birth") },
                    placeholder = { Text("Enter Place of Birth") },
                    modifier = Modifier.fillMaxWidth()
                )
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
