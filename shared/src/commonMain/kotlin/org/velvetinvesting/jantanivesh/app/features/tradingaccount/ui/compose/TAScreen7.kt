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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
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
import jantanivesh.shared.generated.resources.info_filled_icon
import jantanivesh.shared.generated.resources.info_icon
import jantanivesh.shared.generated.resources.receipt_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.utils.tradingaccount.GuardianRelation
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
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

@Preview(showBackground = true, locale = "hi", heightDp = 1000)
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
            title = "Trading",
            stepCount = 2,
            totalSteps = 6,
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
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = Primary
                            )
                            Text(
                                "Please provide the details of the legal guardian responsible for this policy.",
                                style = MaterialTheme.typography.labelSmall,
                                color = GreyText
                            )
                        }
                    }

                    item { WhyThisNeeded() }

                    item {
                        TitledAppTextField(
                            title = "Guardian Name",
                            value = data.guardian_first_name,
                            onValueChange = {
                                handleEvent(
                                    TradingAccountEvent.OnGuardianFirstNameChange(
                                        it
                                    )
                                )
                            },
                            placeholder = "Enter full legal Name",
                            mandatory = true,
                            keyboardType = KeyboardType.Text
                        )
                    }
                    item {
                        DropDownSelector(
                            title = "Minor Relationship",
                            value = GuardianRelation.getDisplayName(data.guardian_relation),
                            onValueChange = {
                                handleEvent(
                                    TradingAccountEvent.OnGuardianRelationChange(
                                        it.code
                                    )
                                )
                            },
                            placeholder = "Select Relationship",
                            mandatory = true,
                            list = GuardianRelation.entries,
                            textConvertor = { it.displayName }
                        )
                    }
                    item {

                        OnBoardingDateField(
                            label = "Guardian Date of Birth",
                            value = data.guardian_dob,
                            placeholder = "DD/MM/YYYY",
                            mandatory = true,
                            onClick = { showDateSelector = true }
                        )
                    }
                    item {
                        Text(
                            "Guardian Mobile Number *",
                            style = MaterialTheme.typography.titleSmall,
                            color = GreyText,
                            modifier = Modifier.padding(bottom = Spacing.dp4)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DropDownSelector(
                                value = "+91 ",
                                onValueChange = { TODO() },
                                placeholder = "+91",
                                list = listOf("+91", "+92", "+93"),
                                textConvertor = { TODO() },
                                modifier = Modifier.weight(0.4f)
                            )
                            TitledAppTextField(
                                value = "", // TODO Implement phone number
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnGuardianPanChange(
                                            it.toUpperCase(Locale.current)
                                        )
                                    )
                                },
                                placeholder = "Mobile Number",
                                mandatory = true,
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    item{

                        TitledAppTextField(
                            title = "Guardian Email Address",
                            value = "", // TODO Implement email
                            onValueChange = {
                                handleEvent(
                                    TradingAccountEvent.OnGuardianPanChange(
                                        it.toUpperCase(Locale.current)
                                    )
                                )
                            },
                            placeholder = "Enter Email Address",
                            mandatory = true,
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    item { Spacer(modifier = Modifier.height(pv.calculateBottomPadding())) }
                }
                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    value = "Continue to step 3 →",
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
                            handleEvent(
                                TradingAccountEvent.OnGuardianDobChange(
                                    DateTimeUtils.epochMillisToSlashDate(
                                        it
                                    )
                                )
                            )
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
            .border(Spacing.dp1, shape = RoundedCornerShape(10.dp), color = Color(0x4dE4FEd3))
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color(0xffEFF4FF))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.info_filled_icon),
                contentDescription = "guardian icon",
                modifier = Modifier.clip(RoundedCornerShape(Spacing.dp8))
                    .size(36.dp)
                    .background(Color(0x1a376822)).padding(Spacing.dp8),
                tint = Primary
            )
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                Text(
                    "Why is this needed?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                    color = Primary,
                    fontSize = 16.sp
                )
                Text(
                    "Financial regulations require us to verify the identity of the guardian for minor-linked insurance accounts to ensure maximum protection and legal compliance.",
                    style = MaterialTheme.typography.titleSmall,
                    color = GreyText
                )
            }
        }
    }
}