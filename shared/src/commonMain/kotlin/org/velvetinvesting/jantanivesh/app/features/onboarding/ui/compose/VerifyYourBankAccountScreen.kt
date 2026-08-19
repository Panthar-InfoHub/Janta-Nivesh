package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.account_holder_label
import jantanivesh.shared.generated.resources.account_number_label
import jantanivesh.shared.generated.resources.account_type_label
import jantanivesh.shared.generated.resources.bank_details_subtitle
import jantanivesh.shared.generated.resources.bank_details_title
import jantanivesh.shared.generated.resources.bank_name_label
import jantanivesh.shared.generated.resources.check_circle_outline_icon
import jantanivesh.shared.generated.resources.confirm_proceed
import jantanivesh.shared.generated.resources.icon_arrow_right
import jantanivesh.shared.generated.resources.ifsc_code_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBoxDivider
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.tagColor
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.InvertedAppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.JantaNiveshAndVelvetLogo
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.AccountType
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.BankAccountDetails
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.VerifyBankAccountEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.VerifyBankAccountUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyBankAccountScreen(
    state: VerifyBankAccountUiState,
    handleEvent: (VerifyBankAccountEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (state.showConfirmBankAccountSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                handleEvent(VerifyBankAccountEvent.OnDismissConfirmBankAccountSheet)
            },
            sheetState = sheetState,
            containerColor = White,
            shape = RoundedCornerShape(topStart = Spacing.dp24, topEnd = Spacing.dp24)
        ) {
            ConfirmBankAccountSheetContent(
                details = state.bankAccountDetails,
                onConfirm = { handleEvent(VerifyBankAccountEvent.OnConfirmBankAccountClick) },
                onChange = { handleEvent(VerifyBankAccountEvent.OnChangeBankAccountClick) }
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.dp24)
            .clearFocusOnTap()
            .imePadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp18),
            contentPadding = PaddingValues(top = Spacing.dp24)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "Bank Details/ " + stringResource(Res.string.bank_details_title),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Black,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Enter your bank account details to securely receive payments and complete your financial information./ " + stringResource(
                            Res.string.bank_details_subtitle
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray444,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                TitledAppTextField(
                    title = "Bank Name/ " + stringResource(Res.string.bank_name_label),
                    value = state.bankName,
                    onValueChange = { handleEvent(VerifyBankAccountEvent.OnBankNameChange(it)) },
                    placeholder = "",
                    mandatory = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )
            }

            item {
                DropDownSelector(
                    title = "Account Type/ " + stringResource(Res.string.account_type_label),
                    value = state.accountType?.id.orEmpty(),
                    onValueChange = { handleEvent(VerifyBankAccountEvent.OnAccountTypeChange(it)) },
                    placeholder = "Select Type",
                    mandatory = true,
                    list = AccountType.entries,
                    textConvertor = { it.id.capitalize(Locale.current) }
                )
            }

            item {
                TitledAppTextField(
                    title = "Account Holder/ " + stringResource(Res.string.account_holder_label),
                    value = state.accountHolder,
                    onValueChange = { handleEvent(VerifyBankAccountEvent.OnAccountHolderChange(it)) },
                    placeholder = "",
                    mandatory = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )
            }

            item {
                TitledAppTextField(
                    title = "Account Number/ " + stringResource(Res.string.account_number_label),
                    value = state.accountNumber,
                    onValueChange = { handleEvent(VerifyBankAccountEvent.OnAccountNumberChange(it)) },
                    placeholder = "",
                    mandatory = true,
                    keyboardType = KeyboardType.Number,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            }

            item {
                TitledAppTextField(
                    title = "IFSC Code/ " + stringResource(Res.string.ifsc_code_label),
                    value = state.ifscCode,
                    onValueChange = { handleEvent(VerifyBankAccountEvent.OnIfscCodeChange(it)) },
                    placeholder = "",
                    mandatory = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Done
                    )
                )
            }
        }

        JantaNiveshAndVelvetLogo()

        AppButton(
            text = "Confirm & Proceed/ " + stringResource(Res.string.confirm_proceed),
            onClick = { handleEvent(VerifyBankAccountEvent.OnProceedClick) },
            loading = state.isLoading,
            enabled = state.canSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.dp24)
                .genericDropShadow()
        )
    }
}

@Composable
private fun ConfirmBankAccountSheetContent(
    details: BankAccountDetails,
    onConfirm: () -> Unit,
    onChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = Spacing.dp24,
                end = Spacing.dp24,
                bottom = Spacing.dp32
            ),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp48)
                    .clip(CircleShape)
                    .background(tagColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.check_circle_outline_icon),
                    contentDescription = "Confirm Icon",
                    tint = Primary,
                    modifier = Modifier.size(Spacing.dp24)
                )
            }
            Text(
                text = "Confirm bank account",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Black
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Spacing.dp16))
                .border(
                    width = Spacing.dp1,
                    color = Secondary,
                    shape = RoundedCornerShape(Spacing.dp16)
                )
                .padding(horizontal = Spacing.dp20)
        ) {
            BankDetailRow(label = "Bank Name", value = details.bankName)
            HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
            BankDetailRow(label = "Account Holder", value = details.accountHolder)
            HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
            BankDetailRow(label = "Account Number", value = details.maskedAccountNumber)
            HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
            BankDetailRow(label = "IFSC code", value = details.ifscCode)
        }

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
            AppButton(
                text = "Confirm",
                onClick = onConfirm,
                style = AppButtonDefaults.style(height = Spacing.dp58),
                trailingIcon = Res.drawable.icon_arrow_right,
                modifier = Modifier.fillMaxWidth()
            )
            InvertedAppButton(
                text = "Change",
                onClick = onChange,
                style = AppButtonDefaults.style(
                    containerColor = White,
                    contentColor = Secondary,
                    height = Spacing.dp58
                ),
                trailingIcon = Res.drawable.icon_arrow_right,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun BankDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.dp16),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Black
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = Secondary,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true, locale = "hi")
@Composable
private fun VerifyBankAccountScreenPreview() {
    JantaNiveshTheme {
        VerifyBankAccountScreen(
            state = VerifyBankAccountUiState(),
            handleEvent = {}
        )
    }
}
