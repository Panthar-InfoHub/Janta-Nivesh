package org.velvetinvesting.jantanivesh.app.features.plans.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.arrow_front_icon
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.FundPurchaseEvent
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.FundPurchaseUiState
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.OTP_LENGTH

/**
 * Confirms the purchase with the OTP the gateway just sent.
 *
 * Entering the OTP *is* the user's authorisation for the transaction, so the sheet is bilingual
 * throughout — the consent has to be understood, not just tapped through.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseOtpSheet(
    state: FundPurchaseUiState,
    handleEvent: (FundPurchaseEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { handleEvent(FundPurchaseEvent.OnOtpSheetDismiss) },
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
        ) {
            Text(
                text = "Verify Your Mutual Fund Purchase\n" +
                        "(अपनी म्यूचुअल फ़ंड ख़रीद की पुष्टि करें)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Black
            )

            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                Text(
                    text = "A verification code has been sent to/ एक सत्यापन कोड भेजा गया है",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray444
                )
                if (state.otpDestination.isNotBlank()) {
                    Text(
                        text = state.otpDestination,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Black
                    )
                }
            }

            OtpBoxes(
                otp = state.otp,
                onOtpChange = { handleEvent(FundPurchaseEvent.OnOtpChange(it)) }
            )

            ResendRow(
                secondsLeft = state.resendSecondsLeft,
                onResendClick = { handleEvent(FundPurchaseEvent.OnResendOtpClick) }
            )

            AppButton(
                text = "Confirm",
                onClick = { handleEvent(FundPurchaseEvent.OnConfirmOtpClick) },
                loading = state.isVerifyingOtp,
                enabled = state.isOtpComplete,
                trailingIcon = Res.drawable.arrow_front_icon,
                style = AppButtonDefaults.style(height = Spacing.dp58),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * [OTP_LENGTH] circles fed by a single hidden text field — the same approach as the login OTP
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
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
    ) {
        repeat(OTP_LENGTH) { index ->
            val char = otp.getOrNull(index)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .border(
                        width = Spacing.dp1,
                        color = if (char != null) Secondary else BoxBorder,
                        shape = CircleShape
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
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
}

/** Counts down before the resend link becomes live, so the gateway is not hammered. */
@Composable
private fun ResendRow(
    secondsLeft: Int,
    onResendClick: () -> Unit
) {
    if (secondsLeft > 0) {
        Text(
            text = "You can resend the code in $secondsLeft seconds/ आप $secondsLeft सेकंड में " +
                    "कोड दोबारा भेज सकते हैं",
            style = MaterialTheme.typography.labelMedium,
            color = GreyText
        )
    } else {
        Text(
            text = "Resend code/ कोड दोबारा भेजें",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Secondary,
            modifier = Modifier.clickable(
                onClick = onResendClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PurchaseOtpSheetPreview() {
    JantaNiveshTheme {
        Column(
            modifier = Modifier.background(White).padding(Spacing.dp24),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20)
        ) {
            OtpBoxes(otp = "12", onOtpChange = {})
            ResendRow(secondsLeft = 24, onResendClick = {})
            ResendRow(secondsLeft = 0, onResendClick = {})
        }
    }
}
