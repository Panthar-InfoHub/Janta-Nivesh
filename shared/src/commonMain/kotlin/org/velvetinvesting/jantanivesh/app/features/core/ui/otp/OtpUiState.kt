package org.velvetinvesting.jantanivesh.app.features.core.ui.otp

/**
 * Everything [OtpVerificationScreen] needs to draw itself, and nothing that is specific to *why* an
 * OTP is being collected. A feature's own ui state embeds this rather than re-declaring the fields,
 * so the screen never has to know about the feature.
 */
data class OtpUiState(
    val otpValue: String = "",
    val otpLength: Int = DEFAULT_OTP_LENGTH,
    val resendTimerSeconds: Int = 0,
    val isLoading: Boolean = false
) {
    val isComplete: Boolean
        get() = otpValue.length == otpLength

    val isSubmitEnabled: Boolean
        get() = isComplete && !isLoading

    /** The resend link replaces the countdown text only once the cooldown has elapsed. */
    val canResend: Boolean
        get() = resendTimerSeconds == 0 && !isLoading

    companion object {
        const val DEFAULT_OTP_LENGTH = 4
    }
}
