package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.check_circle_outline_icon
import jantanivesh.shared.generated.resources.icon_arrow_right
import jantanivesh.shared.generated.resources.info_filled_icon
import jantanivesh.shared.generated.resources.monument_icon
import jantanivesh.shared.generated.resources.verification_amount_kyc
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
import org.velvetinvesting.jantanivesh.app.core.theme.lightBlue
import org.velvetinvesting.jantanivesh.app.core.theme.tagColor
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.InvertedAppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.BankAccountDetails
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.VerifyBankAccountEvent
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.VerifyBankAccountUiState

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
            .background(White)
            .padding(horizontal=Spacing.dp20)
            .imePadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
            contentPadding = PaddingValues(top = Spacing.dp24)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        text = "Verify your bank account",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Black,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "We'll verify your bank details through a secure UPI transaction to ensure accurate fund transfers",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gray444,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                PennyDropCard()
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    StepCard(
                        number = "1",
                        text = "Open your UPI app when prompted",
                    )
                    StepCard(
                        number = "2",
                        text = "Complete the ₹1 verification payment",
                    )
                    StepCard(
                        number = "3",
                        text = "We'll automatically fetch your bank details",
                    )
                }
            }

            item {
                ImportantNoteCard()
            }
        }

        AppButton(
            text = "Verify your UPI",
            onClick = { handleEvent(VerifyBankAccountEvent.OnVerifyUpiClick) },
            loading = state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.dp24)
        )
    }
}

@Composable
private fun PennyDropCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(White)
            .padding(Spacing.dp20),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp48)
                    .clip(RoundedCornerShape(Spacing.dp12))
                    .background(lightBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.monument_icon),
                    contentDescription = "Bank Icon",
                    tint = Primary,
                    modifier = Modifier.size(Spacing.dp24)
                )
            }
            Text(
                text = "Penny Drop",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Black
            )
        }
        Text(
            text = "Quick & secure bank verification using UPI",
            style = MaterialTheme.typography.bodyLarge,
            color = Black
        )
    }
}

@Composable
private fun StepCard(number: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (number == "2") {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(Spacing.dp4)
                    .background(Secondary)
            )
        }

        Row(
            modifier = Modifier.padding(
                start = if (number == "2") Spacing.dp16 else Spacing.dp16 + Spacing.dp4,
                end = Spacing.dp16,
                top = Spacing.dp16,
                bottom = Spacing.dp16
            ),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp32)
                    .clip(CircleShape)
                    .background(lightBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.titleMedium,
                    color = Primary
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Gray444
            )
        }
    }
}

@Composable
private fun ImportantNoteCard() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp24))
            .border(
                width = Spacing.dp1,
                color = FilterChipUnselected,
                RoundedCornerShape(Spacing.dp24)
            )
            .background(tagColor)
            .padding(Spacing.dp20),
    ) {
        Icon(
            painter = painterResource(Res.drawable.info_filled_icon),
            contentDescription = "Info Icon",
            tint = Primary,
            modifier = Modifier.size(Spacing.dp20)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            Text(
                text = "Important Note",
                style = MaterialTheme.typography.titleMedium,
                color = Primary
            )
            Text(
                text = "The ₹1 verification amount will be instantly refunded to your account. This process is 100% secure and RBI compliant./ " + stringResource(Res.string.verification_amount_kyc),
                style = MaterialTheme.typography.bodyLarge,
                color = Black,
            )
        }
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

@Preview(showBackground = true)
@Composable
private fun VerifyBankAccountScreenPreview() {
    JantaNiveshTheme {
        VerifyBankAccountScreen(
            state = VerifyBankAccountUiState(),
            handleEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmBankAccountSheetPreview() {
    // ModalBottomSheet does not render inside previews, so the sheet surface is
    // mimicked here to preview the expanded state over the screen.
    JantaNiveshTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            VerifyBankAccountScreen(
                state = VerifyBankAccountUiState(),
                handleEvent = {}
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black.copy(alpha = 0.32f))
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(topStart = Spacing.dp24, topEnd = Spacing.dp24))
                    .background(White)
                    .padding(top = Spacing.dp12),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(width = Spacing.dp32, height = Spacing.dp4)
                        .clip(CircleShape)
                        .background(FilterChipUnselected)
                )
                ConfirmBankAccountSheetContent(
                    details = VerifyBankAccountUiState().bankAccountDetails,
                    onConfirm = {},
                    onChange = {},
                    modifier = Modifier.padding(top = Spacing.dp24)
                )
            }
        }
    }
}