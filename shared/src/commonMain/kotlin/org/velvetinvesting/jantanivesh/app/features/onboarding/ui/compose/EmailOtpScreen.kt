package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.resend_code
import jantanivesh.shared.generated.resources.resend_code_timer
import jantanivesh.shared.generated.resources.verification_code_sent
import jantanivesh.shared.generated.resources.verify
import jantanivesh.shared.generated.resources.verify_your_email
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpSentToSubtitle
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpUiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpVerificationScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EmailOtpEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EmailOtpUiState

/**
 * Onboarding's configuration of the shared [OtpVerificationScreen]. Only the copy and the event
 * mapping differ from login's `EnterOtpScreen`.
 */
@Composable
fun EmailOtpScreen(
    state: EmailOtpUiState,
    handleEvent: (EmailOtpEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    OtpVerificationScreen(
        state = state.otp,
        title = "Verify your email ID/" + stringResource(Res.string.verify_your_email),
        submitText = "Verify/" + stringResource(Res.string.verify),
        onOtpChange = { handleEvent(EmailOtpEvent.OnOtpChanged(it)) },
        onSubmit = { handleEvent(EmailOtpEvent.OnVerifyClicked) },
        onResend = { handleEvent(EmailOtpEvent.OnResendClicked) },
        onBack = { handleEvent(EmailOtpEvent.OnBackClicked) },
        modifier = modifier,
        resendText = "Resend Code/" + stringResource(Res.string.resend_code),
        resendTimerText = "You can resend the code in ${state.otp.resendTimerSeconds} seconds/" +
            stringResource(Res.string.resend_code_timer, state.otp.resendTimerSeconds),
        subtitle = {
            OtpSentToSubtitle(
                destination = state.email,
                text = "A verification code has been sent to/" +
                    stringResource(Res.string.verification_code_sent)
            )
        }
    )
}

@Preview(showBackground = true, locale = "hi")
@Composable
fun EmailOtpScreenPreview() {
    JantaNiveshTheme {
        EmailOtpScreen(
            state = EmailOtpUiState(
                email = "khiwatari12@gmail.com",
                otp = OtpUiState(otpValue = "746", resendTimerSeconds = 0)
            ),
            handleEvent = {}
        )
    }
}
