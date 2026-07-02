package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.address_line_1
import jantanivesh.shared.generated.resources.address_line_2_optional
import jantanivesh.shared.generated.resources.address_line_3_optional
import jantanivesh.shared.generated.resources.city
import jantanivesh.shared.generated.resources.ckyc_no
import jantanivesh.shared.generated.resources.country
import jantanivesh.shared.generated.resources.email
import jantanivesh.shared.generated.resources.fax_number_optional
import jantanivesh.shared.generated.resources.foreign_address_line_1
import jantanivesh.shared.generated.resources.foreign_city
import jantanivesh.shared.generated.resources.foreign_country
import jantanivesh.shared.generated.resources.foreign_phone_optional
import jantanivesh.shared.generated.resources.info_icon
import jantanivesh.shared.generated.resources.investor_onboarding
import jantanivesh.shared.generated.resources.kyc_type
import jantanivesh.shared.generated.resources.mobile_optional
import jantanivesh.shared.generated.resources.office_fax_optional
import jantanivesh.shared.generated.resources.office_number
import jantanivesh.shared.generated.resources.pincode
import jantanivesh.shared.generated.resources.state
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.Country
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.Holding
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.KycType
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.StateCode
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.TaxStatus
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.Data
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountEvent
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountUiState
import org.velvetinvesting.jantanivesh.app.utils.tradingaccount.InvestorOnboarding

@Preview(showBackground = true, locale = "hi", heightDp = 2000)
@Composable
fun TradingAccountAddressPreview() {
    JantaNiveshTheme {
        TradingAccountAddressScreen(
            uiState = TradingAccountUiState(
                formState = UiState.Success(TradingAccountFormDomain(data = Data())),
                holderNature = Holding.SINGLE
            ),
            handleEvent = {},
            onClick = {},
            onBackClick = {}
        )
    }
}

@Composable
fun TradingAccountAddressScreen(
    uiState: TradingAccountUiState,
    handleEvent: (TradingAccountEvent) -> Unit,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()
        .clearFocusOnTap()) {
        LocalTopAppBarWithBackButtonAndStepCount(
            title = "Trading",
            stepCount = if (uiState.isMinor) 7 else 6,
            totalSteps = uiState.totalSteps,
            onBack = onBackClick,
        )

        UiStateContainer(
            uiState = uiState.formState,
            onRetry = { handleEvent(TradingAccountEvent.GetUserData) },
            modifier = Modifier.fillMaxSize()
        ) { uiData ->
            val data = uiData.data
            val showForeignAddress = TaxStatus.fromCode(data.tax_status)?.isResident?.not() ?: false

            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(horizontal = Spacing.dp16),
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
                    contentPadding = PaddingValues(bottom = Spacing.dp16)
                ) {
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Address Details",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                "Provide your residential and contact information",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                color = Color(0xff44464F)
                            )
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .genericDropShadow(RoundedCornerShape(Spacing.dp12))
                                .clip(RoundedCornerShape(Spacing.dp12))
                                .border(1.dp, BoxBorder, RoundedCornerShape(Spacing.dp12))
                                .background(Color.White)
                                .padding(Spacing.dp16),
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                        ) {
                            TitledAppTextField(
                                title = "Address Line 1/ " + "(" + stringResource(Res.string.address_line_1) + ")",
                                value = data.address_1,
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnAddress1Change(
                                            it
                                        )
                                    )
                                },
                                placeholder = "House No, Building Name",
                                mandatory = true
                            )
                            TitledAppTextField(
                                title = "Address Line 2 (Optional)/ " + "(" + stringResource(Res.string.address_line_2_optional) + ")",
                                value = data.address_2,
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnAddress2Change(
                                            it
                                        )
                                    )
                                },
                                placeholder = "Street, Area",
                            )
                            TitledAppTextField(
                                title = "Address Line 3 (Optional)/ " + "(" + stringResource(Res.string.address_line_3_optional) + ")",
                                value = data.address_3,
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnAddress3Change(it)
                                    )
                                },
                                placeholder = "Landmark",
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                            ) {
                                TitledAppTextField(
                                    title = "City/ " + "(" + stringResource(Res.string.city) + ")",
                                    value = data.city,
                                    modifier = Modifier.weight(1f),
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnCityChange(it)
                                        )
                                    },
                                    placeholder = "City",
                                    mandatory = true,
                                )
                                TitledAppTextField(
                                    title = "State/ (${stringResource(Res.string.state)})",
                                    value = StateCode.getDisplayName(data.state),
                                    onValueChange = {},
                                    placeholder = "Select State",
                                    mandatory = true,
                                    enabled = false,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            handleEvent(TradingAccountEvent.ShowStateDialog)
                                        }
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                            ) {
                                TitledAppTextField(
                                    title = "Pincode/ " + "(" + stringResource(Res.string.pincode) + ")",
                                    value = data.pincode,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnPincodeChange(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = "6-digit PIN",
                                    mandatory = true,
                                    modifier = Modifier.weight(1f),
                                    keyboardType = KeyboardType.Number
                                )
                                TitledAppTextField(
                                    title = "Country/ (${stringResource(Res.string.country)})",
                                    value = data.country,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnCountryChange(it)
                                        )
                                    },
                                    placeholder = "Select Country",
                                    mandatory = true,
                                    enabled = true,
                                    modifier = Modifier
                                        .weight(1f)

                                )
                            }
                        }
                    }
                    item {
                        Text(
                            "Contact Information",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .genericDropShadow(RoundedCornerShape(Spacing.dp16))
                                .clip(RoundedCornerShape(Spacing.dp24))
                                .background(Color.White)
                                .padding(Spacing.dp16),
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                        ) {
                            TitledAppTextField(
                                title = "Email/ " + "(" + stringResource(Res.string.email) + ")",
                                value = data.email,
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnEmailChange(it)
                                    )
                                },
                                placeholder = "email@example.com",
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                            ) {
                                TitledAppTextField(
                                    title = "Mobile (Optional)/ " + "(" + stringResource(Res.string.mobile_optional) + ")",
                                    value = data.resi_phone,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnResiPhoneChange(it)
                                        )
                                    },
                                    placeholder = "Phone Number",
                                    keyboardType = KeyboardType.Phone
                                )
                            }
                            TitledAppTextField(
                                title = "Fax Number (Optional)/ " + "(" + stringResource(Res.string.fax_number_optional) + ")",
                                value = data.resi_fax,
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnResiFaxChange(it)
                                    )
                                },
                                placeholder = "Residential Fax",
                                keyboardType = KeyboardType.Number
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                            ) {
                                TitledAppTextField(
                                    title = "Office Number/ " + "(" + stringResource(Res.string.office_number) + ")",
                                    value = data.office_phone,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnOfficePhoneChange(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = "Work Number",
                                    modifier = Modifier.weight(1f),
                                    keyboardType = KeyboardType.Number
                                )
                                TitledAppTextField(
                                    title = "Office Fax (Optional)/ " + "(" + stringResource(Res.string.office_fax_optional) + ")",
                                    value = data.office_fax,
                                    modifier = Modifier.weight(1f),
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnOfficeFaxChange(it)
                                        )
                                    },
                                    placeholder = "Work Fax",
                                    keyboardType = KeyboardType.Number
                                )
                            }
                        }
                    }
                    item {
                        if (showForeignAddress) {
                            Column(
                                modifier = Modifier
                                    .genericDropShadow(RoundedCornerShape(Spacing.dp16))
                                    .clip(RoundedCornerShape(Spacing.dp24))
                                    .background(Color.White)
                                    .padding(Spacing.dp16),
                                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                            ) {
                                Text(
                                    "Foreign Address",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                        .clip(RoundedCornerShape(Spacing.dp8))
                                        .background(Color(0xffDCE9FF))
                                        .padding(horizontal = Spacing.dp8, vertical = Spacing.dp12),
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.info_icon),
                                        tint = Color(0xff8EA1D9),
                                        contentDescription = null,
                                        modifier = Modifier.size(Spacing.dp18)
                                    )
                                    Text(
                                        "Foreign address details are currently active.",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color(0xff8EA1D9)
                                    )
                                }
                                TitledAppTextField(
                                    title = "Foreign Address Line 1/ (${stringResource(Res.string.foreign_address_line_1)})",
                                    value = data.foreign_address_1,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnForeignAddress1Change(it)
                                        )
                                    },
                                    placeholder = "House/ Apt Number",
                                    mandatory = true
                                )
                                TitledAppTextField(
                                    title = "Foreign Address Line 2 (Optional)",
                                    value = data.foreign_address_2,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnForeignAddress2Change(it)
                                        )
                                    },
                                    placeholder = "Apartment, Suite, Unit"
                                )
                                TitledAppTextField(
                                    title = "Foreign Address Line 3 (Optional)",
                                    value = data.foreign_address_3,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnForeignAddress3Change(it)
                                        )
                                    },
                                    placeholder = "Additional Address Information"
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                                ) {

                                    TitledAppTextField(
                                        title = "City/ (${stringResource(Res.string.foreign_city)})",
                                        value = data.foreign_address_city,
                                        modifier = Modifier.weight(1f),
                                        onValueChange = {
                                            handleEvent(
                                                TradingAccountEvent.OnForeignCityChange(it)
                                            )
                                        },
                                        placeholder = "City",
                                        mandatory = true
                                    )

                                    TitledAppTextField(
                                        title = "State / Province",
                                        value = data.foreign_address_state,
                                        modifier = Modifier.weight(1f),
                                        onValueChange = {
                                            handleEvent(
                                                TradingAccountEvent.OnForeignStateChange(it)
                                            )
                                        },
                                        placeholder = "State",
                                        mandatory = true
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                                ) {

                                    TitledAppTextField(
                                        title = "Postal Code",
                                        value = data.foreign_address_pincode,
                                        modifier = Modifier.weight(1f),
                                        onValueChange = {
                                            handleEvent(
                                                TradingAccountEvent.OnForeignPincodeChange(it)
                                            )
                                        },
                                        placeholder = "Postal Code",
                                        mandatory = true
                                    )

                                    TitledAppTextField(
                                        title = "Country/ (${stringResource(Res.string.foreign_country)})",
                                        value = Country.getDisplayNameFromCode(data.foreign_address_country) ?: "",
                                        onValueChange = {},
                                        placeholder = "Select Country",
                                        mandatory = true,
                                        enabled = false,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                handleEvent(
                                                    TradingAccountEvent.ShowForeignCountryDialog
                                                )
                                            }
                                    )
                                }
                                TitledAppTextField(
                                    title = "Foreign Phone (Optional)/ (${stringResource(Res.string.foreign_phone_optional)})",
                                    value = data.foreign_address_resi_phone,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnForeignPhoneChange(it)
                                        )
                                    },
                                    placeholder = "+ (Country Code)",
                                    keyboardType = KeyboardType.Phone
                                )
                            }
                        }
                    }
                    item{
                        Text("KYC & regulatory Info", style = MaterialTheme.typography.headlineSmall)
                    }
                    item{
                        Column(
                            modifier = Modifier
                                .genericDropShadow(RoundedCornerShape(Spacing.dp16))
                                .clip(RoundedCornerShape(Spacing.dp24))
                                .background(Color.White)
                                .padding(Spacing.dp16),
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                            ) {

                                DropDownSelector(
                                    title = "KYC Type/ (${stringResource(Res.string.kyc_type)})",
                                    value = KycType.getDisplayName(data.primary_holder_kyc_type),
                                    placeholder = "KYC Type",
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnPrimaryKycTypeChange(
                                                it.code
                                            )
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    list = KycType.entries,
                                    textConvertor = {
                                        it.displayName
                                    },
                                    mandatory = true
                                )

                                if (data.primary_holder_kyc_type == KycType.CKYC_COMPLIANT.code) {
                                    TitledAppTextField(
                                        title = "CKYC No./ (${stringResource(Res.string.ckyc_no)})",
                                        value = data.primary_holder_ckyc_number,
                                        modifier = Modifier.weight(1f),
                                        onValueChange = {
                                            handleEvent(
                                                TradingAccountEvent.OnPrimaryCkycChange(
                                                    it
                                                )
                                            )
                                        },
                                        placeholder = "14-digit No.",
                                        keyboardType = KeyboardType.Number,
                                        mandatory = true
                                    )
                                }
                            }

                            DropDownSelector(
                                title = "Investor Onboarding/ (${stringResource(Res.string.investor_onboarding)})",
                                value = InvestorOnboarding.getDisplayName(data.paperless_flag),
                                placeholder = "Investor Onboarding",
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnPaperlessFlagChange(it.code)
                                    )
                                },
                                list = InvestorOnboarding.entries,
                                textConvertor = {
                                    it.displayName
                                },
                                mandatory = true
                            )

                        }
                    }
                }

                NextButtonFooter(
                    onClick = onClick,
                    value = "Submit Form",
                    enabled = uiState.addressScreenButtonEnabled,
                )
            }
            if (uiState.showStateDialog) {
                StatePickerDialog(
                    showDialog = true,
                    selectedState = data.state,
                    onDismiss = {
                        handleEvent(TradingAccountEvent.HideStateDialog)
                    },
                    onStateSelected = {
                        handleEvent(
                            TradingAccountEvent.OnStateChange(it.code)
                        )
                    }
                )
            }

            if (uiState.showForeignCountryDialog) {
                CountrySelectorDialog(
                    showDialog = true,
                    selectedCode = data.foreign_address_country,
                    onDismiss = {
                        handleEvent(TradingAccountEvent.HideForeignCountryDialog)
                    },
                    onCountrySelected = {
                        handleEvent(
                            TradingAccountEvent.OnForeignCountryChange(it.code)
                        )
                    }
                )
            }
        }
    }
}