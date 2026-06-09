package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.OnBoardingDateField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.Gender
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.Holding
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.TaxStatus
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.Data
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountEvent
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountUiState

@Preview(showBackground = true, locale = "hi", heightDp = 2000)
@Composable
fun TradingAccountBasicDetailsPreview() {
    JantaNiveshTheme {
        TradingAccountBasicDetailsScreen(
            pv = PaddingValues(0.dp),
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
fun TradingAccountBasicDetailsScreen(
    pv: PaddingValues,
    uiState: TradingAccountUiState,
    handleEvent: (TradingAccountEvent) -> Unit,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(pv)) {
        LocalTopAppBarWithBackButtonAndStepCount(
            title = "Trading",
            stepCount = 1,
            totalSteps = uiState.totalSteps,
            onBack = onBackClick,
            modifier = Modifier
        )

        UiStateContainer(
            uiState = uiState.formState,
            onRetry = { handleEvent(TradingAccountEvent.GetUserData) },
            modifier = Modifier.fillMaxSize()
        ) { uiData ->
            val data = uiData.data

            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(horizontal = Spacing.dp16),
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                ) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp2)
                        ) {
                            Text(
                                "Basic Details",
                                style = MaterialTheme.typography.headlineMedium,
                            )
                            Text(
                                "Let's start with your basic information",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xff4A5565)
                            )
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .genericDropShadow(RoundedCornerShape(Spacing.dp16))
                                .clip(RoundedCornerShape(Spacing.dp24))
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(Spacing.dp10)
                                )
                                .padding(Spacing.dp16),
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                        ) {
                            TitledAppTextField(
                                title = "First Name",
                                value = data.primary_holder_first_name,
                                onValueChange = { handleEvent(TradingAccountEvent.OnFirstNameChange(it)) },
                                placeholder = "Enter First Name",
                                mandatory = true,
                                keyboardType = KeyboardType.Text
                            )

                            TitledAppTextField(
                                title = "Middle Name",
                                value = data.primary_holder_middle_name,
                                onValueChange = { handleEvent(TradingAccountEvent.OnMiddleNameChange(it)) },
                                placeholder = "Enter Middle Name",
                                mandatory = false,
                                keyboardType = KeyboardType.Text
                            )

                            TitledAppTextField(
                                title = "Last Name",
                                value = data.primary_holder_last_name,
                                onValueChange = { handleEvent(TradingAccountEvent.OnLastNameChange(it)) },
                                placeholder = "Enter Last Name",
                                mandatory = false,
                                keyboardType = KeyboardType.Text
                            )

                            TitledAppTextField(
                                title = "Place of Birth",
                                value = data.po_bir_inc,
                                onValueChange = { handleEvent(TradingAccountEvent.OnPlaceOfBirthChange(it)) },
                                placeholder = "Enter Place of Birth",
                                mandatory = true,
                                keyboardType = KeyboardType.Text
                            )

                            GenderBoxComposable(
                                label = "Gender",
                                selected = data.gender,
                                onSelect = { handleEvent(TradingAccountEvent.OnGenderChange(it)) }
                            )

                            DropDownSelector(
                                title = "Tax Status",
                                value = TaxStatus.fromCode(data.tax_status)?.displayName ?: "",
                                onValueChange = { handleEvent(TradingAccountEvent.OnTaxStatusChange(it.code)) },
                                placeholder = "Select Tax Status",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                list = TaxStatus.entries,
                                textConvertor = { it.displayName }
                            )

                            TitledAppTextField(
                                title = "Email Address",
                                value = data.email,
                                onValueChange = { handleEvent(TradingAccountEvent.OnEmailChange(it)) },
                                placeholder = "Enter Email",
                                mandatory = true,
                                keyboardType = KeyboardType.Email
                            )

                            TitledAppTextField(
                                title = "Phone Number",
                                value = data.indian_mobile_no,
                                onValueChange = { handleEvent(TradingAccountEvent.OnPhoneChange(it)) },
                                placeholder = "Enter Phone Number",
                                mandatory = true,
                                keyboardType = KeyboardType.Phone
                            )

                            OnBoardingDateField(
                                label = "DOB",
                                value = data.primary_holder_dob_incorporation,
                                placeholder = "DD/MM/YYYY",
                                mandatory = true,
                                onClick = {  },
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(pv.calculateBottomPadding())) }
                }

                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    value = "Next",
                    enabled = uiState.basicDetailsNextEnabled
                )
            }
        }
    }
}

@Composable
fun GenderBoxComposable(
    label: String, selected: String, onSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Text(
                text = label, style = MaterialTheme.typography.titleMedium, color = Black
            )
            Text(
                text = "*", color = Color.Red, style = MaterialTheme.typography.titleMedium
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Gender.entries.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { gender ->
                        GenderBox(
                            modifier = Modifier.weight(1f),
                            gender = gender.displayName,
                            isSelected = selected == gender.code,
                            onSelect = { onSelect(gender.code) }
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun GenderBox(
    modifier: Modifier = Modifier, gender: String, isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(0xFFC5A572) else Color.LightGray,
                shape = CircleShape
            )
            .background(Color.White)
            .clickable { onSelect() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = gender, modifier = Modifier.padding(vertical = 12.dp))
    }
}