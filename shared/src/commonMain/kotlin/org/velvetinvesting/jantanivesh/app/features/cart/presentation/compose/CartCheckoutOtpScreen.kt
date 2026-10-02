package org.velvetinvesting.jantanivesh.app.features.cart.presentation.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.utils.formatWithCommas
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.CartCheckoutDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.CartType
import org.velvetinvesting.jantanivesh.app.features.cart.presentation.viewmodel.CartEvent
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpSentToSubtitle
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpUiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpVerificationScreen

/**
 * The cart's configuration of the shared [OtpVerificationScreen], confirming a SIP or lumpsum
 * checkout. The OTP is the user's authorisation for everything in the batch, so the copy names
 * how many and the total.
 */
@Composable
fun CartCheckoutOtpScreen(
    otpState: OtpUiState,
    checkout: CartCheckoutDomain?,
    onEvent: (CartEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSip = checkout?.type != CartType.LUMPSUM
    OtpVerificationScreen(
        state = otpState,
        title = if (isSip) "Confirm your SIPs" else "Confirm your purchase",
        submitText = if (isSip) "Confirm SIPs" else "Confirm & Pay",
        onOtpChange = { onEvent(CartEvent.OnOtpChanged(it)) },
        onSubmit = { onEvent(CartEvent.OnOtpSubmitClicked) },
        onResend = { onEvent(CartEvent.OnOtpResendClicked) },
        onBack = { onEvent(CartEvent.OnOtpBackClicked) },
        modifier = modifier,
        resendText = "Resend Code",
        resendTimerText = "You can resend the code in ${otpState.resendTimerSeconds} seconds",
        subtitle = {
            OtpSentToSubtitle(
                destination = checkout?.let {
                    val noun = if (isSip) "SIP" else "fund"
                    val items = if (it.itemsCount == 1) "1 $noun" else "${it.itemsCount} ${noun}s"
                    "$items · ₹${formatWithCommas(it.totalAmount)}"
                },
                text = if (isSip) {
                    "Enter the OTP sent to your registered mobile number to start"
                } else {
                    "Enter the OTP sent to your registered mobile number to invest in"
                }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CartCheckoutOtpScreenPreview() {
    JantaNiveshTheme {
        CartCheckoutOtpScreen(
            otpState = OtpUiState(otpValue = "1234", otpLength = 6, resendTimerSeconds = 0),
            checkout = CartCheckoutDomain(
                batchId = "batch",
                type = CartType.LUMPSUM,
                itemsCount = 2,
                totalAmount = 5000
            ),
            onEvent = {}
        )
    }
}
