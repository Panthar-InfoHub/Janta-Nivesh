package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import org.sharad.velvetinvestment.utils.tradingaccount.InvestorOnboarding
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.Holding
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.KycType
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.TaxStatus
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.Data
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountEvent
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountUiState

@Preview(showBackground = true, locale = "hi")
@Composable
fun TradingAccountAddressPreview() {
    JantaNiveshTheme {
        TradingAccountAddressScreen(
            pv = PaddingValues(16.dp),
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
    pv: PaddingValues,
    uiState: TradingAccountUiState,
    handleEvent: (TradingAccountEvent) -> Unit,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LocalTopAppBarWithBackButtonAndStepCount(
            title = "Trading Account",
            stepCount = 3,
            totalSteps = 7,
            onBack = onBackClick,
            modifier = Modifier.padding(pv)
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
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                ) {
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Address Details",
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                "Provide your residential and contact information",
                                fontSize = 14.sp,
                                color = Color(0xff4A5565)
                            )
                        }
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
                            if (!showForeignAddress) {
                                TitledAppTextField(
                                    title = "Address Line 1",
                                    value = data.address_1,
                                    onValueChange = { handleEvent(TradingAccountEvent.OnAddress1Change(it)) },
                                    placeholder = "House/Flat No., Building Name",
                                    mandatory = true
                                )
                                TitledAppTextField(
                                    title = "Address Line 2 (Optional)",
                                    value = data.address_2,
                                    onValueChange = { handleEvent(TradingAccountEvent.OnAddress2Change(it)) },
                                    placeholder = "Street Name, Area",
                                    mandatory = false
                                )
                                TitledAppTextField(
                                    title = "City",
                                    value = data.city,
                                    onValueChange = { handleEvent(TradingAccountEvent.OnCityChange(it)) },
                                    placeholder = "Enter City",
                                    mandatory = true
                                )
                                TitledAppTextField(
                                    title = "Pincode",
                                    value = data.pincode,
                                    onValueChange = { handleEvent(TradingAccountEvent.OnPincodeChange(it)) },
                                    placeholder = "6-digit pincode",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Number
                                )
                            } else {
                                TitledAppTextField(
                                    title = "Foreign Address Line 1",
                                    value = data.foreign_address_1,
                                    onValueChange = { handleEvent(TradingAccountEvent.OnForeignAddress1Change(it)) },
                                    placeholder = "Street Address",
                                    mandatory = true
                                )
                                TitledAppTextField(
                                    title = "City",
                                    value = data.foreign_address_city,
                                    onValueChange = { handleEvent(TradingAccountEvent.OnForeignCityChange(it)) },
                                    placeholder = "Enter City",
                                    mandatory = true
                                )
                                TitledAppTextField(
                                    title = "Postal Code",
                                    value = data.foreign_address_pincode,
                                    onValueChange = { handleEvent(TradingAccountEvent.OnForeignPincodeChange(it)) },
                                    placeholder = "Postal Code",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Number
                                )
                            }
                        }
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
                            DropDownSelector(
                                title = "KYC Type",
                                value = KycType.getDisplayName(data.primary_holder_kyc_type),
                                onValueChange = { handleEvent(TradingAccountEvent.OnPrimaryKycTypeChange(it.code)) },
                                placeholder = "KYC Type",
                                mandatory = true,
                                list = KycType.entries,
                                textConvertor = { it.displayName }
                            )

                            if (data.primary_holder_kyc_type == KycType.CKYC_COMPLIANT.code) {
                                TitledAppTextField(
                                    title = "CKYC No",
                                    value = data.primary_holder_ckyc_number,
                                    onValueChange = { handleEvent(TradingAccountEvent.OnPrimaryCkycChange(it)) },
                                    placeholder = "Enter CKYC no.",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Number
                                )
                            }

                            DropDownSelector(
                                title = "Investor Onboarding",
                                value = InvestorOnboarding.getDisplayName(data.paperless_flag),
                                onValueChange = { handleEvent(TradingAccountEvent.OnPaperlessFlagChange(it.code)) },
                                placeholder = "Investor Onboarding",
                                mandatory = true,
                                list = InvestorOnboarding.entries,
                                textConvertor = { it.displayName }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(pv.calculateBottomPadding())) }
                }

                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    value = "Next",
                    enabled = uiState.addressScreenButtonEnabled
                )
            }
        }
    }
}