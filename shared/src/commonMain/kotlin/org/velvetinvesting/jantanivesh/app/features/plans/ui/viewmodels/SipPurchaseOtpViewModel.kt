package org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpController
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchaseMode
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.RequestPurchasePlanOtpUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.VerifyPurchasePlanOtpUseCase

sealed interface SipPurchaseOtpEffect {
    /** The SIP is registered; what the success screen shows comes from the gateway's echo. */
    data class PurchaseConfirmed(
        val mode: PurchaseMode,
        val schemeName: String,
        val amount: String,
        /** Zero when the mode has no debit day, which the success screen reads as "none". */
        val installmentDay: Int,
        val startDate: String
    ) : SipPurchaseOtpEffect

    data object Cancelled : SipPurchaseOtpEffect
}

/**
 * Confirms a SIP that is already registered and reviewed. Everything it needs travels on the
 * route, so a resend or a retry runs from here without going back through the purchase form.
 */
class SipPurchaseOtpViewModel(
    /** The plan's gateway id; both OTP calls are keyed on it. */
    private val planId: String,
    private val schemeName: String,
    private val amount: Int,
    private val mode: PurchaseMode,
    private val installmentDay: Int,
    private val requestPurchasePlanOtp: RequestPurchasePlanOtpUseCase,
    private val verifyPurchasePlanOtp: VerifyPurchasePlanOtpUseCase
) : ViewModel() {

    /** The OTP was sent by the screen before this one, so the cooldown starts already running. */
    val otp = OtpController(
        scope = viewModelScope,
        otpLength = OTP_LENGTH,
        resendCooldownSeconds = OTP_RESEND_SECONDS
    )

    private val _uiState = MutableStateFlow(SipPurchaseOtpUiState(schemeName = schemeName))
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SipPurchaseOtpEffect>()
    val effect = _effect.receiveAsFlow()

    fun onOtpChange(value: String) = otp.onOtpChange(value)

    fun onBackClick() {
        viewModelScope.launch { _effect.send(SipPurchaseOtpEffect.Cancelled) }
    }

    fun onVerifyClick() {
        val state = otp.state.value
        if (!state.isSubmitEnabled) return

        viewModelScope.launch {
            val result = otp.withLoading { verifyPurchasePlanOtp(planId, state.otpValue) }

            when (result) {
                // The screen stays put on a bad OTP so the user can correct it without starting
                // over — the plan is already registered and is still awaiting this code.
                is NetworkResponse.Error -> {
                    otp.clearOtp()
                    SnackBarController.showError(result.error.message)
                }

                is NetworkResponse.Success -> {
                    val plan = result.data

                    _effect.send(
                        SipPurchaseOtpEffect.PurchaseConfirmed(
                            mode = mode,
                            schemeName = schemeName,
                            // The gateway echoes the amount back, but not always; the typed
                            // amount stands in.
                            amount = plan.amount.takeIf { it.isNotBlank() } ?: amount.toString(),
                            installmentDay = if (mode.needsInstallmentDay) {
                                plan.installmentDay ?: installmentDay
                            } else {
                                0
                            },
                            startDate = plan.startDate.orEmpty()
                        )
                    )
                }
            }
        }
    }

    fun onResendClick() {
        if (!otp.state.value.canResend) return

        viewModelScope.launch {
            when (val result = otp.withLoading { requestPurchasePlanOtp(planId) }) {
                is NetworkResponse.Error -> SnackBarController.showError(result.error.message)

                is NetworkResponse.Success -> {
                    otp.clearOtp()
                    otp.startResendTimer()
                }
            }
        }
    }
}

data class SipPurchaseOtpUiState(
    val schemeName: String = ""
)
