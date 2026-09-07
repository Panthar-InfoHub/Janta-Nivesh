package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpController
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpUiState
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.RedemptionState
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.AwaitMfRedemptionUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.RequestMfRedemptionOtpUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.VerifyMfRedemptionOtpUseCase

data class RedeemOtpUiState(
    /** Where the code went, shown back to the user under the title. */
    val otp: OtpUiState = OtpUiState(
        otpLength = 6
    )
)

sealed interface RedeemOtpEvent {
    data class OnOtpChanged(val otp: String) : RedeemOtpEvent
    data object OnVerifyClicked : RedeemOtpEvent
    data object OnResendClicked : RedeemOtpEvent
}

sealed interface RedeemOtpEffect {
    /** Verified *and* accepted by the AMC — the portfolio is now stale. */
    data object RedemptionConfirmed : RedeemOtpEffect
}

/**
 * Confirms a redemption that has already been placed and accepted.
 *
 * The first code was requested by the redeem screen before it navigated here, so this starts on
 * a running cooldown rather than sending one of its own.
 *
 * Verifying the code is not the end: the gateway still has to forward the request, so this waits
 * for `SUBMITTED` before reporting success. Anything else — a failure or a wait that runs out —
 * is surfaced as an error and leaves the user here, because advancing would put them on a
 * portfolio that implies a redemption which was never confirmed.
 */
class RedeemOtpViewModel(
    private val redemptionId: String,
    private val requestOtp: RequestMfRedemptionOtpUseCase,
    private val verifyOtp: VerifyMfRedemptionOtpUseCase,
    private val awaitRedemption: AwaitMfRedemptionUseCase,
    authPrefs: AuthPrefs
) : ViewModel() {

    // The redemption gateway sends a six-digit code; the login and email flows are still four.
    val otp = OtpController(viewModelScope, otpLength = REDEMPTION_OTP_LENGTH)

    val uiState = otp.state
        .map { RedeemOtpUiState(otp = it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            RedeemOtpUiState()
        )

    private val _effect = Channel<RedeemOtpEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: RedeemOtpEvent) {
        when (event) {
            is RedeemOtpEvent.OnOtpChanged -> otp.onOtpChange(event.otp)
            RedeemOtpEvent.OnVerifyClicked -> verify()
            RedeemOtpEvent.OnResendClicked -> resend()
        }
    }

    private fun verify() {
        val code = otp.state.value.otpValue

        viewModelScope.launch {
            // The spinner stays up across both calls: to the user this is one step, and letting
            // the button go idle mid-wait invites a second tap on a redemption already in flight.
            otp.withLoading {
                val verified = verifyOtp(redemptionId, code)
                if (verified is NetworkResponse.Error) {
                    // The old code is dead either way, so the field is cleared for the retry.
                    otp.clearOtp()
                    SnackBarController.showError(verified.error.message)
                    return@withLoading
                }

                awaitRedemption(redemptionId, RedemptionState.SUBMITTED)
                    .onSuccess {
                        SnackBarController.showSuccess("Redemption placed successfully")
                        _effect.send(RedeemOtpEffect.RedemptionConfirmed)
                    }
                    .onError {
                        // Not confirmed, so the flow does not advance: the user is told what
                        // went wrong and stays here rather than landing on a portfolio that
                        // would imply the redemption went through.
                        SnackBarController.showError(it.message)
                    }
            }
        }
    }

    private fun resend() {
        if (!otp.state.value.canResend) return

        viewModelScope.launch {
            otp.withLoading { requestOtp(redemptionId) }
                .onSuccess {
                    otp.clearOtp()
                    otp.startResendTimer()
                    SnackBarController.showSuccess("A new code has been sent")
                }
                .onError { SnackBarController.showError(it.message) }
        }
    }

    private companion object {
        const val REDEMPTION_OTP_LENGTH = 6
    }
}
