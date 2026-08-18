package org.velvetinvesting.jantanivesh.app.features.plans.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.lock_outlined_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.GoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBoxDivider
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.InvertedAppButton
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.ChoosePlanEvent
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.ChoosePlanUiState
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.OTP_LENGTH

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmWithOtpSheet(
    state: ChoosePlanUiState,
    handleEvent: (ChoosePlanEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { handleEvent(ChoosePlanEvent.OnOtpSheetDismiss) },
        sheetState = sheetState,
        containerColor = White,
        shape = RoundedCornerShape(topStart = Spacing.dp24, topEnd = Spacing.dp24)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Spacing.dp24,
                    end = Spacing.dp24,
                    bottom = Spacing.dp32
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20)
        )
        {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.lock_outlined_icon),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(Spacing.dp24)
                )
                Text(
                    text = "Confirm with OTP",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Black
                )
            }

            TransactionSummary(state = state)

            ConsentCard()

            OtpBoxes(
                otp = state.otp,
                onOtpChange = { handleEvent(ChoosePlanEvent.OnOtpChange(it)) }
            )

            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                AppButton(
                    text = "Authorise",
                    onClick = { handleEvent(ChoosePlanEvent.OnAuthoriseClick) },
                    loading = state.isVerifyingOtp,
                    enabled = state.isOtpComplete,
                    style = AppButtonDefaults.style(height = Spacing.dp58),
                    modifier = Modifier.fillMaxWidth()
                )
                InvertedAppButton(
                    text = "Cancel",
                    onClick = { handleEvent(ChoosePlanEvent.OnOtpSheetDismiss) },
                    style = AppButtonDefaults.style(
                        containerColor = White,
                        contentColor = Secondary,
                        height = Spacing.dp58
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Reads from the plan the gateway created where possible, falling back to what the user picked
 * so the sheet still reads correctly if a field comes back empty.
 */
@Composable
private fun TransactionSummary(state: ChoosePlanUiState) {
    val plan = state.createdPlan

    val amount = plan?.amount?.takeIf { it.isNotBlank() } ?: state.monthlyAmount.toString()
    val debitDay = plan?.installmentDay ?: state.debitDay

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
        SummaryRow(label = "Transaction", value = "SIP registration")
        HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
        SummaryRow(label = "Scheme", value = state.selectedScheme?.schemeName.orEmpty())
        HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
        SummaryRow(label = "Amount", value = "₹$amount / month")
        HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
        SummaryRow(
            label = "Debit day",
            value = debitDay?.let { "$it of every month" }.orEmpty()
        )
        plan?.startDate?.takeIf { it.isNotBlank() }?.let { startDate ->
            HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
            SummaryRow(label = "Starts on", value = startDate.take(ISO_DATE_LENGTH))
        }
        HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
        SummaryRow(label = "Via", value = "UPI Autopay ••1193")
    }
}

/** `start_date` can arrive as a full timestamp; only the date part is shown. */
private const val ISO_DATE_LENGTH = 10

@Composable
private fun SummaryRow(label: String, value: String) {
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
            color = Gray444
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = Black,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ConsentCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(GoalIconBg)
            .padding(Spacing.dp20),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
    ) {
        Text(
            text = "Consent",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Primary
        )
        Text(
            text = "By entering the OTP sent to your registered mobile number and tapping " +
                    "Authorise, I confirm the details above and give my consent to place this " +
                    "SIP registration through Cybrilla on the ONDC network. This OTP entry is " +
                    "recorded as my authorisation for this transaction.",
            style = MaterialTheme.typography.labelMedium,
            color = Gray444
        )
    }
}

/**
 * [OTP_LENGTH] boxes fed by a single hidden text field — the same approach as the login OTP
 * screen, which keeps IME and autofill behaviour on one real input.
 */
@Composable
private fun OtpBoxes(
    otp: String,
    onOtpChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        value = otp,
        onValueChange = onOtpChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.focusRequester(focusRequester),
        decorationBox = {}
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
    ) {
        repeat(OTP_LENGTH) { index ->
            val char = otp.getOrNull(index)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .size(Spacing.dp53)
                    .clip(CircleShape)
                    .border(
                        width = Spacing.dp1,
                        color = if (char != null) Secondary else BoxBorder,
                        shape = CircleShape
                    )
                    .clickable {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (char != null) {
                    Text(
                        text = char.toString(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Black
                    )
                }
            }
        }
    }

    Text(
        text = "Sandbox: any $OTP_LENGTH digits.",
        style = MaterialTheme.typography.labelSmall,
        color = GreyText,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun ConfirmWithOtpSheetPreview() {
    JantaNiveshTheme {
        Column(modifier = Modifier.background(White).padding(Spacing.dp24), verticalArrangement = Arrangement.spacedBy(
            Spacing.dp20)) {
            TransactionSummary(state = ChoosePlanUiState(debitDay = 5))
            ConsentCard()
            OtpBoxes(otp = "123", onOtpChange = {})
        }
    }
}
