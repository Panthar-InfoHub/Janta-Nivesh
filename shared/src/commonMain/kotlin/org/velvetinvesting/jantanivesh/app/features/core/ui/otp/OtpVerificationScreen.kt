package org.velvetinvesting.jantanivesh.app.features.core.ui.otp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.resend_code
import jantanivesh.shared.generated.resources.resend_code_timer
import jantanivesh.shared.generated.resources.verification_code_sent
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.JantaNiveshAndVelvetLogo
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.OtpInputField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap

/**
 * A one-time-password screen with no opinion about what the code is for. Every piece of copy and
 * every action is supplied by the caller, so login, transaction approval, profile-change
 * confirmation and so on can share this rather than forking it.
 *
 * The caller keeps ownership of [state] — see [OtpController] for a ready-made holder of the
 * input/cooldown behaviour that every OTP flow repeats.
 *
 * @param onSubmit invoked by the submit button; the caller decides what happens afterwards.
 * @param onBack null hides the back button, for flows the user must not escape.
 * @param subtitle slot under the title, e.g. [OtpSentToSubtitle] for "sent to +91 …".
 * @param footer slot pinned to the bottom of the screen.
 */
@Composable
fun OtpVerificationScreen(
    state: OtpUiState,
    title: String,
    submitText: String,
    onOtpChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onResend: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    resendText: String = stringResource(Res.string.resend_code),
    resendTimerText: String = stringResource(
        Res.string.resend_code_timer,
        state.resendTimerSeconds
    ),
    subtitle: @Composable ColumnScope.() -> Unit = {},
    footer: @Composable ColumnScope.() -> Unit = { JantaNiveshAndVelvetLogo() }
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(modifier = modifier.clearFocusOnTap()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.dp24, vertical = Spacing.dp16)
        ) {

            Column(modifier = Modifier.weight(1f)) {
                if (onBack != null) {
                    AppBackButton(onClick = onBack)
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = Spacing.dp12)
                )

                subtitle()

                OtpInputField(
                    otpValue = state.otpValue,
                    otpLength = state.otpLength,
                    onValueChange = onOtpChange,
                    focusRequester = focusRequester,
                    modifier = Modifier.padding(bottom = Spacing.dp24)
                )

                if (state.resendTimerSeconds == 0) {
                    Text(
                        text = resendText,
                        color = Primary,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.clickable(
                            onClick = onResend,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                    )
                } else {
                    Text(
                        text = resendTimerText,
                        color = GreyText,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(bottom = Spacing.dp24)
                    )
                }

                AppButton(
                    text = submitText,
                    onClick = onSubmit,
                    enabled = state.isSubmitEnabled,
                    loading = state.isLoading,
                    modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp28)
                )
            }

            footer()
        }
    }
}

/**
 * The usual "a verification code has been sent to …" block, as a subtitle slot.
 *
 * [destination] is shown verbatim, so callers pass a formatted phone number ("+91 9876543210") or
 * an email address as appropriate.
 */
@Composable
fun ColumnScope.OtpSentToSubtitle(
    destination: String,
    text: String = stringResource(Res.string.verification_code_sent)
) {
    Text(
        text = text,
        color = GreyText,
        style = MaterialTheme.typography.titleSmall
    )
    Text(
        text = destination,
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(bottom = Spacing.dp24, top = Spacing.dp4)
    )
}

@Preview(showBackground = true)
@Composable
fun OtpVerificationScreenPreview() {
    JantaNiveshTheme {
        OtpVerificationScreen(
            state = OtpUiState(otpValue = "123", resendTimerSeconds = 0),
            title = "Enter the Code",
            submitText = "Next",
            onOtpChange = {},
            onSubmit = {},
            onResend = {},
            onBack = {},
            subtitle = { OtpSentToSubtitle(destination = "+91 9876543210") }
        )
    }
}
