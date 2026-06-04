package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.receipt_icon
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.utils.tradingaccount.GuardianRelation
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppDatePicker
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.OnBoardingDateField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.Holding
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.Data
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountEvent
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountUiState

@Preview(showBackground = true, locale = "hi")
@Composable
fun TradingAccountGuardianDetailPreview() {
    JantaNiveshTheme {
        TradingAccountGuardianDetailScreen(
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
fun TradingAccountGuardianDetailScreen(
    pv: PaddingValues,
    uiState: TradingAccountUiState,
    handleEvent: (TradingAccountEvent) -> Unit,
    onClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var showDateSelector by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        LocalTopAppBarWithBackButtonAndStepCount(
            title = "Trading Account",
            stepCount = 5,
            totalSteps = 7,
            onBack = onBackClick,
            modifier = Modifier.padding(pv)
        )

        UiStateContainer(
            uiState = uiState.formState,
            onRetry = { handleEvent(TradingAccountEvent.GetUserData) }
        ) { baseResponse ->
            val data = baseResponse.data

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
                                "Guardian Details",
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                "Provide guardian information for the minor account holder",
                                fontSize = 14.sp,
                                color = Color(0xff4A5565)
                            )
                        }
                    }

                    item { WhyThisNeeded() }

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
                                title = "Guardian Name",
                                value = data.guardian_first_name,
                                onValueChange = { handleEvent(TradingAccountEvent.OnGuardianFirstNameChange(it)) },
                                placeholder = "Enter Guardian Name",
                                mandatory = true,
                                keyboardType = KeyboardType.Text
                            )

                            DropDownSelector(
                                title = "Guardian Relationship",
                                value = GuardianRelation.getDisplayName(data.guardian_relation),
                                onValueChange = { handleEvent(TradingAccountEvent.OnGuardianRelationChange(it.code)) },
                                placeholder = "Select Relationship",
                                mandatory = true,
                                list = GuardianRelation.entries,
                                textConvertor = { it.displayName }
                            )

                            OnBoardingDateField(
                                label = "Guardian DOB",
                                value = data.guardian_dob,
                                placeholder = "DD/MM/YYYY",
                                mandatory = true,
                                onClick = { showDateSelector = true }
                            )

                            TitledAppTextField(
                                title = "Guardian PAN",
                                value = data.guardian_pan,
                                onValueChange = { handleEvent(TradingAccountEvent.OnGuardianPanChange(it.toUpperCase(Locale.current))) },
                                placeholder = "ABCDE1234F",
                                mandatory = true,
                                keyboardType = KeyboardType.Text
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(pv.calculateBottomPadding())) }
                }

                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    value = "Next",
                    enabled = uiState.guardianScreenButtonEnabled
                )
            }

            if (showDateSelector) {
                AppDatePicker(
                    show = showDateSelector,
                    selectedDate = DateTimeUtils.slashDateToEpochMillis(data.guardian_dob),
                    onDismiss = { showDateSelector = false },
                    onDateSelected = { dob ->
                        dob?.let {
                            handleEvent(TradingAccountEvent.OnGuardianDobChange(DateTimeUtils.epochMillisToSlashDate(it)))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun WhyThisNeeded() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.7.dp, shape = RoundedCornerShape(10.dp), color = Color(0xffBEDBFF))
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color(0xffEFF6FF))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.receipt_icon),
                contentDescription = "guardian icon",
                tint = Color(0xff155DFC)
            )
            Column {
                Text(
                    "Why is this needed?",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0B1C30),
                    fontSize = 16.sp
                )
                Text(
                    "Guardian details are required because the account holder is under 18 years of age. The guardian will have legal authority over the account until the minor turns 18.",
                    fontSize = 14.sp,
                    color = Color(0xff1447E6)
                )
            }
        }
    }
}