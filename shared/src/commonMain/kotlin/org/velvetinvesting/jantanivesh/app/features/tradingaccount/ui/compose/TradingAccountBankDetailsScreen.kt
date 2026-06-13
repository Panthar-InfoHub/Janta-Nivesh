package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.payment_mode
import jantanivesh.shared.generated.resources.secure_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.utils.tradingaccount.AccountType
import org.velvetinvesting.jantanivesh.app.utils.tradingaccount.DividendPayMode
import org.velvetinvesting.jantanivesh.app.utils.tradingaccount.YesNo
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.dashedBorder
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.Holding
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.Data
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountEvent
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountUiState

@Preview(showBackground = true, locale = "hi")
@Composable
fun TradingAccountBankDetailsPreview() {
    JantaNiveshTheme {
        TradingAccountBankDetailsScreen(
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
fun TradingAccountBankDetailsScreen(
    pv: PaddingValues,
    uiState: TradingAccountUiState,
    handleEvent: (TradingAccountEvent) -> Unit,
    onClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LocalTopAppBarWithBackButtonAndStepCount(
            title = "Trading",
            stepCount = if (uiState.isMinor) 6 else 5,
            totalSteps = uiState.totalSteps,
            onBack = onBackClick,
            modifier = Modifier.padding(pv)
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
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
                    contentPadding = PaddingValues(top = Spacing.dp12)
                ) {

                    item { SecureNote() }

                    item {
                        DropDownSelector(
                            title = "Payment Mode/ (${stringResource(Res.string.payment_mode)})",
                            value = DividendPayMode.getDisplayName(data.div_pay_mode),
                            onValueChange = {
                                handleEvent(
                                    TradingAccountEvent.OnDivPayModeChange(it.code)
                                )
                            },
                            placeholder = "Payment Mode",
                            mandatory = true,
                            modifier = Modifier.fillMaxWidth(),
                            list = DividendPayMode.entries,
                            textConvertor = { it.displayName }
                        )
                    }

                    items(uiState.visibleBankAccounts) { index ->
                        BankAccountSection(
                            index = index,
                            data = data,
                            removable = index != 1,
                            onRemove = {
                                handleEvent(
                                    TradingAccountEvent.RemoveBankAccount(index)
                                )
                            },
                            handleEvent = handleEvent
                        )
                    }

                    if (uiState.visibleBankAccounts.size < 5) {
                        item {
                            Button(
                                onClick = {
                                    handleEvent(
                                        TradingAccountEvent.AddBankAccount
                                    )
                                },
                                shape = RoundedCornerShape(Spacing.dp8),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Black
                                ),
                                modifier = Modifier.fillMaxWidth().dashedBorder(
                                    strokeWidth = Spacing.dp2,
                                    color = SelectedBoxBorder,
                                    dashLength = Spacing.dp6,
                                    gapLength = Spacing.dp4
                                )
                            ) {
                                Text(
                                    "+  Add More Account",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(pv.calculateBottomPadding())) }
                }

                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    enabled = uiState.bankScreenButtonEnabled,
                )
            }
        }
    }
}

@Composable
fun SecureNote() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        modifier = Modifier
            .genericDropShadow(RoundedCornerShape(Spacing.dp8))
            .clip(RoundedCornerShape(Spacing.dp8))
            .background(Color(0xffEFF4FF))
            .border(1.dp, Color(0xffD3E4FE), RoundedCornerShape(Spacing.dp8))
            .padding(Spacing.dp20),
    ) {
        Icon(
            painter = painterResource(Res.drawable.secure_icon),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(Spacing.dp20)
        )
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
            Text(
                "Secure & Encrypted",
                style = MaterialTheme.typography.labelLarge,
                color = Black
            )
            Text(
                "Your bank details are encrypted and stored securely for automated payouts and verifications.",
                style = MaterialTheme.typography.labelSmall,
                color = GreyText,
            )
            Text(
                "आपके बैंक विवरण एन्क्रिप्टेड हैं और स्वचालित भुगतान और सत्यापन के लिए सुरक्षित रूप से संग्रहीत किए जाते हैं।",
                style = MaterialTheme.typography.labelSmall,
                color = GreyText,
            )
        }
    }
}

@Composable
fun BankAccountSection(
    index: Int,
    data: Data,
    removable: Boolean,
    onRemove: () -> Unit,
    handleEvent: (TradingAccountEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .genericDropShadow(RoundedCornerShape(Spacing.dp16))
            .clip(RoundedCornerShape(Spacing.dp24))
            .background(Color.White)
            .padding(Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        AccountHeader(
            heading = "Bank Account $index",
            showRemove = removable,
            onRemoveClick = onRemove
        )

        DropDownSelector(
            title = "Account Type",
            value = AccountType.getDisplayName(getAccountType(index, data)),
            onValueChange = {
                handleEvent(
                    TradingAccountEvent.OnAccountTypeChange(
                        index,
                        it.code
                    )
                )
            },
            placeholder = "Account Type",
            mandatory = true,
            list = AccountType.entries,
            textConvertor = { it.displayName }
        )

        TitledAppTextField(
            title = "Account Number",
            value = getAccountNumber(index, data),
            onValueChange = { handleEvent(TradingAccountEvent.OnAccountNumberChange(index, it)) },
            placeholder = "Enter Account Number",
            mandatory = true,
            keyboardType = KeyboardType.Number
        )

        TitledAppTextField(
            title = "IFSC",
            value = getIfsc(index, data),
            onValueChange = { handleEvent(TradingAccountEvent.OnIfscChange(index, it)) },
            placeholder = "IFSC",
            mandatory = true,
            keyboardType = KeyboardType.Text
        )

        TitledAppTextField(
            title = "MICR",
            value = getMicr(index, data),
            onValueChange = { handleEvent(TradingAccountEvent.OnMicrChange(index, it)) },
            placeholder = "MICR",
            mandatory = false,
            keyboardType = KeyboardType.Number
        )

        DropDownSelector(
            title = "Default Bank",
            value = YesNo.displayNameFromCode(getDefaultBank(index, data)),
            onValueChange = {
                handleEvent(
                    TradingAccountEvent.OnDefaultBankChange(
                        index,
                        it.code
                    )
                )
            },
            placeholder = "Y/N",
            mandatory = true,
            list = YesNo.entries,
            textConvertor = { it.displayName }
        )
    }
}

@Composable
private fun AccountHeader(
    modifier: Modifier = Modifier,
    heading: String,
    showRemove: Boolean = false,
    onRemoveClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = heading,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Primary,
                modifier = Modifier
                    .fillMaxWidth()
            )

            if (showRemove) {
                Text(
                    text = "Remove",
                    color = Color.Red,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onRemoveClick
                        )
                )
            }
        }
    }
}

// Helpers previously residing in the ViewModel
private fun getAccountType(index: Int, data: Data): String = when (index) {
    1 -> data.account_type_1; 2 -> data.account_type_2; 3 -> data.account_type_3; 4 -> data.account_type_4; 5 -> data.account_type_5; else -> ""
}

private fun getAccountNumber(index: Int, data: Data): String = when (index) {
    1 -> data.account_no_1; 2 -> data.account_no_2; 3 -> data.account_no_3; 4 -> data.account_no_4; 5 -> data.account_no_5; else -> ""
}

private fun getIfsc(index: Int, data: Data): String = when (index) {
    1 -> data.ifsc_code_1; 2 -> data.ifsc_code_2; 3 -> data.ifsc_code_3; 4 -> data.ifsc_code_4; 5 -> data.ifsc_code_5; else -> ""
}

private fun getMicr(index: Int, data: Data): String = when (index) {
    1 -> data.micr_no_1; 2 -> data.micr_no_2; 3 -> data.micr_no_3; 4 -> data.micr_no_4; 5 -> data.micr_no_5; else -> ""
}

private fun getDefaultBank(index: Int, data: Data): String = when (index) {
    1 -> data.default_bank_flag_1; 2 -> data.default_bank_flag_2; 3 -> data.default_bank_flag_3; 4 -> data.default_bank_flag_4; 5 -> data.default_bank_flag_5; else -> ""
}