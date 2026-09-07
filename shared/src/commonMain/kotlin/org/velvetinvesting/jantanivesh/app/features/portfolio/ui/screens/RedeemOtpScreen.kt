package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.redeem_otp_title
import jantanivesh.shared.generated.resources.resend_code
import jantanivesh.shared.generated.resources.resend_code_timer
import jantanivesh.shared.generated.resources.verification_code_sent
import jantanivesh.shared.generated.resources.verify
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpSentToSubtitle
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpUiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpVerificationScreen
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.RedeemOtpEvent
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.RedeemOtpUiState

/**
 * The redemption's configuration of the shared [OtpVerificationScreen]. Only the copy and the
 * event mapping differ from the other OTP screens in the app.
 */
@Composable
fun RedeemOtpScreen(
    state: RedeemOtpUiState,
    handleEvent: (RedeemOtpEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    OtpVerificationScreen(
        state = state.otp,
        title = "Confirm your redemption/" + stringResource(Res.string.redeem_otp_title),
        submitText = stringResource(Res.string.verify),
        onOtpChange = { handleEvent(RedeemOtpEvent.OnOtpChanged(it)) },
        onSubmit = { handleEvent(RedeemOtpEvent.OnVerifyClicked) },
        onResend = { handleEvent(RedeemOtpEvent.OnResendClicked) },
        onBack = onBack,
        modifier = modifier,
        resendText = "Resend Code/" + stringResource(Res.string.resend_code),
        resendTimerText = "You can resend the code in ${state.otp.resendTimerSeconds} seconds/" +
            stringResource(Res.string.resend_code_timer, state.otp.resendTimerSeconds),
        subtitle = {
            OtpSentToSubtitle(
                destination = null,
                text = "A verification code has been sent to/" +
                    stringResource(Res.string.verification_code_sent)
            )
        }
    )
}

@Preview(showBackground = true, locale = "hi")
@Composable
private fun RedeemOtpScreenPreview() {
    JantaNiveshTheme {
        RedeemOtpScreen(
            state = RedeemOtpUiState(
                otp = OtpUiState(otpValue = "12", resendTimerSeconds = 18, otpLength = 6)
            ),
            handleEvent = {},
            onBack = {}
        )
    }
}
