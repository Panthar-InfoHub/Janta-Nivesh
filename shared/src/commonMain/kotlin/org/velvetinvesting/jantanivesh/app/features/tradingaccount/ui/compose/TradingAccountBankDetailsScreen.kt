package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.receipt_icon
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.utils.tradingaccount.AccountType
import org.sharad.velvetinvestment.utils.tradingaccount.DividendPayMode
import org.sharad.velvetinvestment.utils.tradingaccount.YesNo
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.Holding
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.Data
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountEvent
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountUiState
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountViewModel

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
            title = "Trading Account",
            stepCount = 4,
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
                                "Bank Details",
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                "Provide your bank account information",
                                fontSize = 14.sp,
                                color = Color(0xff4A5565)
                            )
                        }
                    }

                    item { SecureNote() }

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
                                title = "Dividend Payment Mode",
                                value = DividendPayMode.getDisplayName(data.div_pay_mode),
                                onValueChange = { handleEvent(TradingAccountEvent.OnDivPayModeChange(it.code)) },
                                placeholder = "Payment Mode",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                list = DividendPayMode.entries,
                                textConvertor = { it.displayName }
                            )
                        }
                    }

                    items(uiState.visibleBankAccounts) { index ->
                        BankAccountSection(
                            index = index,
                            data = data,
                            removable = index != 1,
                            onRemove = { handleEvent(TradingAccountEvent.RemoveBankAccount(index)) },
                            handleEvent = handleEvent
                        )
                    }

                    if (uiState.visibleBankAccounts.size < 5) {
                        item {
                            Text(
                                text = "+ Add Another Bank Account",
                                color = Primary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .padding(vertical = Spacing.dp8)
                                    .clickable { handleEvent(TradingAccountEvent.AddBankAccount) }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(pv.calculateBottomPadding())) }
                }

                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    value = "Next",
                    enabled = uiState.bankScreenButtonEnabled
                )
            }
        }
    }
}

@Composable
fun SecureNote() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.7.dp, color = Color(0xffB9F8CF), shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color(0xffF0FDF4))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.receipt_icon),
                contentDescription = "security",
                tint = Color(0xff00A63E)
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xff016630),
                                fontWeight = FontWeight.SemiBold
                            )
                        ) { append("Secure & Encrypted: ") }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xff016630)
                            )
                        ) { append("Your bank details are encrypted and stored securely") }
                    }
                )
            }
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
            onValueChange = { handleEvent(TradingAccountEvent.OnAccountTypeChange(index, it.code)) },
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
            onValueChange = { handleEvent(TradingAccountEvent.OnDefaultBankChange(index, it.code)) },
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
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(5.dp)
                    .clip(CircleShape)
                    .background(Secondary, CircleShape)
            )

            Text(
                text = heading,
                style = MaterialTheme.typography.titleLarge,
                color = Primary,
                modifier = Modifier
                    .weight(1f)
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
private fun getAccountType(index: Int, data: Data): String = when (index) { 1 -> data.account_type_1; 2 -> data.account_type_2; 3 -> data.account_type_3; 4 -> data.account_type_4; 5 -> data.account_type_5; else -> "" }
private fun getAccountNumber(index: Int, data: Data): String = when (index) { 1 -> data.account_no_1; 2 -> data.account_no_2; 3 -> data.account_no_3; 4 -> data.account_no_4; 5 -> data.account_no_5; else -> "" }
private fun getIfsc(index: Int, data: Data): String = when (index) { 1 -> data.ifsc_code_1; 2 -> data.ifsc_code_2; 3 -> data.ifsc_code_3; 4 -> data.ifsc_code_4; 5 -> data.ifsc_code_5; else -> "" }
private fun getMicr(index: Int, data: Data): String = when (index) { 1 -> data.micr_no_1; 2 -> data.micr_no_2; 3 -> data.micr_no_3; 4 -> data.micr_no_4; 5 -> data.micr_no_5; else -> "" }
private fun getDefaultBank(index: Int, data: Data): String = when (index) { 1 -> data.default_bank_flag_1; 2 -> data.default_bank_flag_2; 3 -> data.default_bank_flag_3; 4 -> data.default_bank_flag_4; 5 -> data.default_bank_flag_5; else -> "" }