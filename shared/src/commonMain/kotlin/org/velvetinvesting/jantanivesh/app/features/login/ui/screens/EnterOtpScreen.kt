package org.velvetinvesting.jantanivesh.app.features.login.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.enter_code
import jantanivesh.shared.generated.resources.next
import jantanivesh.shared.generated.resources.resend_code
import jantanivesh.shared.generated.resources.resend_code_timer
import jantanivesh.shared.generated.resources.verification_code_sent
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpSentToSubtitle
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpUiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpVerificationScreen
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpEvent
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpUiState

/**
 * Login's configuration of the shared [OtpVerificationScreen]: it supplies the copy and maps the
 * screen's callbacks onto [EnterOtpEvent]. Other flows that need an OTP should add a sibling of
 * this file rather than adding branches here.
 */
@Composable
fun EnterOtpScreen(
    state: EnterOtpUiState,
    onEvent: (EnterOtpEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    OtpVerificationScreen(
        state = state.otp,
        title = "Enter the Code/" + stringResource(Res.string.enter_code),
        submitText = stringResource(Res.string.next),
        onOtpChange = { onEvent(EnterOtpEvent.OnOtpChanged(it)) },
        onSubmit = { onEvent(EnterOtpEvent.OnNextClicked) },
        onResend = { onEvent(EnterOtpEvent.OnResendClicked) },
        onBack = { onEvent(EnterOtpEvent.OnBackClicked) },
        modifier = modifier,
        resendText = "Resend Code/" + stringResource(Res.string.resend_code),
        resendTimerText = "You can resend the code in ${state.otp.resendTimerSeconds} seconds/" +
            stringResource(Res.string.resend_code_timer, state.otp.resendTimerSeconds),
        subtitle = {
            OtpSentToSubtitle(
                destination = "+91 ${state.phoneNumber}",
                text = "A verification code has been sent to/" +
                    stringResource(Res.string.verification_code_sent)
            )
        }
    )
}

@Preview(showBackground = true, locale = "hi")
@Composable
fun EnterOtpScreenPreview() {
    JantaNiveshTheme {
        EnterOtpScreen(
            state = EnterOtpUiState(
                phoneNumber = "9876543210",
                otp = OtpUiState(otpValue = "123", resendTimerSeconds = 0)
            ),
            onEvent = {}
        )
    }
}
