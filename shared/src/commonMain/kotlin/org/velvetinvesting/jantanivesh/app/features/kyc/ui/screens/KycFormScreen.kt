package org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import jantanivesh.shared.generated.resources.arrowback_icon
import jantanivesh.shared.generated.resources.dob_dropdown_icon
import jantanivesh.shared.generated.resources.dropdown_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.TextBlack
import org.velvetinvesting.jantanivesh.app.core.theme.White
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

@Preview(heightDp = 2000)
@Composable
fun KycFormScreenPreview() {
    JantaNiveshTheme {
        KycFormScreen(
            state = KYCFormScreenUiState(),
            onEvent = {},
            onBack = {},
        )
    }
}
//TODO FIX SHADOWS ON THREE COLUMNS and Top app bar padding
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
                onBack = onBack,
                modifier = Modifier.padding(horizontal = Spacing.dp16)
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
            modifier = Modifier.fillMaxWidth().padding(pv).padding(vertical = Spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
        ) {
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
                        text = "Basic Information/" + "(मूल जानकारी)",
                        style = MaterialTheme.typography.labelLarge,
                        color = Primary
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                        Text(
                            "Full Name/" + "(पूरा नाम)",
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
                            "Date of Birth/ (जन्म तिथि)",
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
                                    onClick = { TODO() },
                                    modifier = Modifier.padding(end = Spacing.dp16)
                                ) {
                                    Icon(
                                        painterResource(Res.drawable.dob_dropdown_icon),
                                        contentDescription = "Calendar Icon",
                                        modifier = Modifier.size(Spacing.dp24)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                        Text(
                            "Place of Birth/ (जन्म स्थान)",
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
                            "Mobile Number/ (मोबाइल नंबर)",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        AppTextField(
                            value = maskAadhaar(state.formState.mobileNumber),
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
                            "Email Address/ (ई-मेल)",
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
                        text = "Identity Details/ पहचान विवरण",
                        style = MaterialTheme.typography.labelLarge,
                        color = Primary
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                        Text(
                            "PAN Number/ (पैन नंबर)",
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
                            "Aadhaar Number/ (आधार संख्या)",
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
                            "Gender/ (लिंग)",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        AppTextField(
                            value = state.formState.gender,
                            onValueChange = {},
                            placeholder = {
                                Text(
                                    "Select gender",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                    color = TextBlack
                                )
                            },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(
                                    onClick = { /* TODO: Open Gender Dropdown */ },
                                    modifier = Modifier.padding(end = Spacing.dp8)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.dropdown_icon),
                                        contentDescription = "Dropdown Icon",
                                        modifier = Modifier.size(Spacing.dp12)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                        Text(
                            "Marital Status/ (वैवाहिक स्थिति)",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        AppTextField(
                            value = state.formState.maritalStatus,
                            onValueChange = {},
                            placeholder = {
                                Text(
                                    "Select status",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                    color = TextBlack
                                )
                            },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(
                                    onClick = { /* TODO: Open Marital Status Dropdown */ },
                                    modifier = Modifier.padding(end = Spacing.dp8)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.dropdown_icon),
                                        contentDescription = "Dropdown Icon",
                                        modifier = Modifier.size(Spacing.dp12)
                                    )
                                }
                            },
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
                        text = "Additional Details/ अतिरिक्त विवरण",
                        style = MaterialTheme.typography.labelLarge,
                        color = Primary
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                        Text(
                            "Title/ (शीर्षक)",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        AppTextField(
                            value = state.formState.fatherTitle,
                            onValueChange = {},
                            placeholder = {
                                Text(
                                    "Select title",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                    color = TextBlack
                                )
                            },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(
                                    onClick = { /* TODO: Open Title Dropdown */ },
                                    modifier = Modifier.padding(end = Spacing.dp8)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.dropdown_icon),
                                        contentDescription = "Dropdown Icon",
                                        modifier = Modifier.size(Spacing.dp12)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                        Text(
                            "Father's Name/ (पिता का नाम)",
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
                            "Mother's Name/ (माता का नाम)",
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
                            "Occupation/ (पेशा)",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        AppTextField(
                            value = state.formState.occupationDescription,
                            onValueChange = {},
                            placeholder = {
                                Text(
                                    "Select occupation",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                    color = TextBlack
                                )
                            },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(
                                    onClick = { /* TODO: Open Occupation Dropdown */ },
                                    modifier = Modifier.padding(end = Spacing.dp8)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.dropdown_icon),
                                        contentDescription = "Dropdown Icon",
                                        modifier = Modifier.size(Spacing.dp12)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
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