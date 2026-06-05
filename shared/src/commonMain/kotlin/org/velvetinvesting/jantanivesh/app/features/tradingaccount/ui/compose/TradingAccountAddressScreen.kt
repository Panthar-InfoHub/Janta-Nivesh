package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.info_icon
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.utils.tradingaccount.InvestorOnboarding
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.OnBoardingDateField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
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

@Preview(showBackground = true, locale = "hi", heightDp = 2000)
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
            title = "Trading",
            stepCount = 5,
            totalSteps = 5,
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
                                title = "Address Line 1/ ",
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
                                title = "Address Line 2 (Optional)/ ",
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
                                title = "Address Line 3 (Optional)/ ",
                                value = data.city,
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnCityChange(
                                            it
                                        )
                                    )
                                },
                                placeholder = "Landmark",
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                            ) {
                                TitledAppTextField(
                                    title = "City/ ",
                                    value = data.pincode,
                                    modifier = Modifier.weight(1f),
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnPincodeChange(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = "City",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Number
                                )
                                DropDownSelector(
                                    title = "State/ ",
                                    value = StateCode.getDisplayName(data.state),
                                    placeholder = "Select State",
                                    onValueChange = {},
                                    modifier = Modifier.weight(1f),
                                    list = StateCode.entries,
                                    textConvertor = {
                                        it.displayName
                                    }
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                            ) {
                                TitledAppTextField(
                                    title = "Pincode/ ",
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
                                DropDownSelector(
                                    title = "Country/ ",
                                    value = StateCode.getDisplayName(data.country),
                                    placeholder = "India",
                                    onValueChange = {},
                                    modifier = Modifier.weight(1f),
                                    list = Country.entries,
                                    textConvertor = {
                                        it.displayName
                                    }
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
                                title = "Email/ ",
                                value = data.email,
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnCityChange(
                                            it
                                        )
                                    )
                                },
                                placeholder = "email@example.com",
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                            ) {
                                TitledAppTextField(
                                    title = "Mobile (Optional)/ ",
                                    value = "",
                                    modifier = Modifier.weight(1f),
                                    onValueChange = {
                                        TODO()
                                    },
                                    placeholder = "City",
                                    keyboardType = KeyboardType.Number
                                )
                                TitledAppTextField(
                                    title = "Fax Number (Optional)/ ",
                                    value = "",
                                    modifier = Modifier.weight(1f),
                                    onValueChange = {
                                        TODO()
                                    },
                                    placeholder = "Fax",
                                    keyboardType = KeyboardType.Number
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                            ) {
                                TitledAppTextField(
                                    title = "Office Number/ ",
                                    value = data.pincode,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnPincodeChange(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = "Work Number",
                                    modifier = Modifier.weight(1f),
                                    keyboardType = KeyboardType.Number
                                )
                                TitledAppTextField(
                                    title = "Office Fax (Optional)/ ",
                                    value = "",
                                    modifier = Modifier.weight(1f),
                                    onValueChange = {
                                        TODO()
                                    },
                                    placeholder = "Work Fax",
                                    keyboardType = KeyboardType.Number
                                )
                            }
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Foreign Address",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Switch(
                                checked = showForeignAddress,
                                onCheckedChange = { TODO() }) //TODO create a custom switch
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
                                    title = "Foreign Address Line 1/ ",
                                    value = data.email,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnCityChange(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = "House/ Apt Number",
                                    mandatory = true
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                                ) {
                                    TitledAppTextField(
                                        title = "City/ ",
                                        value = "",
                                        modifier = Modifier.weight(1f),
                                        onValueChange = {
                                            TODO()
                                        },
                                        placeholder = "City",
                                        keyboardType = KeyboardType.Number,
                                        mandatory = true
                                    )
                                    DropDownSelector(
                                        title = "Country/ ",
                                        value = StateCode.getDisplayName(data.country),
                                        placeholder = "Select Country",
                                        onValueChange = {},
                                        modifier = Modifier.weight(1f),
                                        list = Country.entries,
                                        textConvertor = {
                                            it.displayName
                                        }
                                    )
                                }
                                TitledAppTextField(
                                    title = "Foreign Phone (Optional)/ ",
                                    value = "",
                                    modifier = Modifier.weight(1f),
                                    onValueChange = {
                                        TODO()
                                    },
                                    placeholder = "+ (Country Code)",
                                    keyboardType = KeyboardType.Number,
                                    mandatory = true
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
                                    title = "KYC Type/ ",
                                    value = KycType.getDisplayName(data.primary_holder_kyc_type),
                                    placeholder = "Normal",
                                    onValueChange = {},
                                    modifier = Modifier.weight(1f),
                                    list = KycType.entries,
                                    textConvertor = {
                                        it.displayName
                                    },
                                    mandatory = true
                                )
                                TitledAppTextField(
                                    title = "CKYC No./ ",
                                    value = "",
                                    modifier = Modifier.weight(1f),
                                    onValueChange = {
                                        TODO()
                                    },
                                    placeholder = "14-digit No.",
                                    keyboardType = KeyboardType.Number,
                                    mandatory = true
                                )
                            }
                            CheckBoxComp(
                                heading = "PAN EXEMPT",
                                checked = true,
                                onCheckedChange = { TODO() }
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                            ) {
                                DropDownSelector(
                                    title = "Investor Onboarding/ ",
                                    value = InvestorOnboarding.getDisplayName(""), //TODO implement this
                                    placeholder = "Direct",
                                    onValueChange = {},
                                    modifier = Modifier.weight(1f),
                                    list = InvestorOnboarding.entries,
                                    textConvertor = {
                                        it.displayName
                                    },
                                    mandatory = true
                                )
                                OnBoardingDateField(
                                    value = "", //TODO implement this
                                    placeholder = "mm/dd/yyyy",
                                    label = "Lei Validity/ ",
                                    mandatory = true,
                                    onClick = {},
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            TitledAppTextField(
                                title = "Lei Number (Optional)/ ",
                                value = "",
                                modifier = Modifier.weight(1f),
                                onValueChange = {
                                    TODO()
                                },
                                placeholder = "Enter LEI",
                                keyboardType = KeyboardType.Number,
                                mandatory = true
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(pv.calculateBottomPadding())) }
                }

                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    value = "Continue →",
                    enabled = uiState.addressScreenButtonEnabled
                )
            }
        }
    }
}