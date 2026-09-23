package org.velvetinvesting.jantanivesh.app.features.plans.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpSentToSubtitle
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpUiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpVerificationScreen

/**
 * The purchase flow's configuration of the shared [OtpVerificationScreen]. Entering the OTP *is*
 * the user's authorisation for the debit, so the copy names the fund and the amount rather than
 * asking for a code in the abstract.
 */
@Composable
fun SipPurchaseOtpScreen(
    otpState: OtpUiState,
    schemeName: String,
    amountLabel: String,
    onOtpChange: (String) -> Unit,
    onVerifyClick: () -> Unit,
    onResendClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OtpVerificationScreen(
        state = otpState,
        title = "Confirm your SIP",
        submitText = "Confirm SIP",
        onOtpChange = onOtpChange,
        onSubmit = onVerifyClick,
        onResend = onResendClick,
        onBack = onBackClick,
        modifier = modifier,
        resendText = "Resend Code",
        resendTimerText = "You can resend the code in ${otpState.resendTimerSeconds} seconds",
        subtitle = {
            OtpSentToSubtitle(
                destination = schemeName.takeIf { it.isNotBlank() },
                text = "Enter the OTP sent to your registered mobile number to start your " +
                        "$amountLabel SIP in"
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SipPurchaseOtpScreenPreview() {
    JantaNiveshTheme {
        SipPurchaseOtpScreen(
            otpState = OtpUiState(otpValue = "1234", otpLength = 6, resendTimerSeconds = 0),
            schemeName = "SBI Gold Fund",
            amountLabel = "₹5,000/month",
            onOtpChange = {},
            onVerifyClick = {},
            onResendClick = {},
            onBackClick = {}
        )
    }
}
