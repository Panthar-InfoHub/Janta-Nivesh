package org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.ConfirmMandateUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.CreateSipPlanUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.GetPurchasePlanUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.RequestPurchasePlanOtpUseCase
import kotlin.time.Duration.Companion.milliseconds

/**
 * The unattended half of a SIP: everything between the user approving the mandate and the OTP
 * they have to type. Each step is named so the screen can say what is happening — the two polls
 * alone can run about half a minute, and a bare spinner reads as a hang.
 */
enum class SipSetupStage(val message: String) {
    CONFIRMING_MANDATE("Confirming your autopay mandate…"),
    CREATING_PLAN("Setting up your SIP…"),
    AWAITING_REVIEW("Verifying with the exchange…"),
    REQUESTING_OTP("Sending OTP…")
}

data class SipSetupUiState(
    val stage: SipSetupStage = SipSetupStage.CONFIRMING_MANDATE,
    /** Set when the chain stops; the screen then offers a retry instead of a spinner. */
    val error: String? = null
) {
    val isWorking: Boolean
        get() = error == null
}

sealed interface SipSetupEffect {
    /** The plan is created and the OTP is out; the user confirms it on the OTP screen. */
    data class OtpRequested(val planId: String) : SipSetupEffect

    data object Cancelled : SipSetupEffect
}

/**
 * Runs mandate → SIP → review → OTP without the user watching anything but a progress message.
 *
 * It is a screen of its own rather than part of the purchase form because the mandate takes the
 * user out to the bank's page: coming back re-creates the form, and this chain must survive that.
 */
class SipSetupViewModel(
    /** What the purchase screen gathered, carried here through the authorization web view. */
    private val handoff: SipMandateHandoff,
    private val confirmMandate: ConfirmMandateUseCase,
    private val createSipPlan: CreateSipPlanUseCase,
    private val getPurchasePlan: GetPurchasePlanUseCase,
    private val requestPurchasePlanOtp: RequestPurchasePlanOtpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SipSetupUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SipSetupEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        start()
    }

    fun onRetryClick() {
        if (_uiState.value.isWorking) return
        start()
    }

    fun onCancelClick() {
        viewModelScope.launch { _effect.send(SipSetupEffect.Cancelled) }
    }

    private fun start() {
        _uiState.update { it.copy(stage = SipSetupStage.CONFIRMING_MANDATE, error = null) }

        viewModelScope.launch {
            if (!awaitMandateApproval()) return@launch

            val planId = createPlan() ?: return@launch

            if (!awaitReviewCompleted(planId)) return@launch

            setStage(SipSetupStage.REQUESTING_OTP)

            when (val result = requestPurchasePlanOtp(planId)) {
                is NetworkResponse.Error -> fail(result.error.message)
                is NetworkResponse.Success -> _effect.send(SipSetupEffect.OtpRequested(planId))
            }
        }
    }

    /**
     * The bank approves a mandate asynchronously, so coming back from its page proves nothing —
     * the mandate is read back until it reports approval. A mandate that never settles ends the
     * setup here rather than registering a SIP with nothing to debit.
     */
    private suspend fun awaitMandateApproval(): Boolean {
        setStage(SipSetupStage.CONFIRMING_MANDATE)

        repeat(MANDATE_POLL_ATTEMPTS) { attempt ->
            // The first read happens immediately; the bank has often answered by then.
            if (attempt > 0) delay(POLL_INTERVAL_MS.milliseconds)

            when (val result = confirmMandate(handoff.mandateId)) {
                is NetworkResponse.Error -> {
                    fail(MANDATE_FAILED_MESSAGE)
                    return false
                }

                is NetworkResponse.Success -> if (result.data.isApproved) return true
            }
        }

        fail(MANDATE_PENDING_MESSAGE)
        return false
    }

    private suspend fun createPlan(): String? {
        setStage(SipSetupStage.CREATING_PLAN)

        val result = createSipPlan(
            mfProductId = handoff.mfProductId,
            amount = handoff.amount,
            frequency = handoff.mode.frequency,
            // Null is meaningful here: it is what selects the daily request body.
            installmentDay = handoff.installmentDay
                .takeIf { handoff.mode.needsInstallmentDay && it > 0 },
            mandateId = handoff.mandateRecordId
        )

        return when (result) {
            is NetworkResponse.Error -> {
                fail(result.error.message)
                null
            }

            is NetworkResponse.Success -> result.data.id
        }
    }

    /**
     * `review_completed` is the only state the OTP endpoint accepts, so anything else means keep
     * waiting, and running out of attempts ends the setup rather than firing a request that would
     * be rejected.
     */
    private suspend fun awaitReviewCompleted(planId: String): Boolean {
        setStage(SipSetupStage.AWAITING_REVIEW)

        repeat(POLL_ATTEMPTS) { attempt ->
            if (attempt > 0) delay(POLL_INTERVAL_MS.milliseconds)

            when (val result = getPurchasePlan(planId)) {
                is NetworkResponse.Error -> {
                    fail(result.error.message)
                    return false
                }

                is NetworkResponse.Success -> when {
                    result.data.isReviewCompleted -> return true

                    result.data.hasFailed -> {
                        fail(SIP_FAILED_MESSAGE)
                        return false
                    }

                    else -> Unit
                }
            }
        }

        fail(SIP_TIMED_OUT_MESSAGE)
        return false
    }

    private fun setStage(stage: SipSetupStage) {
        _uiState.update { it.copy(stage = stage, error = null) }
    }

    /** The message stays on screen with a retry: the mandate is already approved and reusable. */
    private fun fail(message: String) {
        _uiState.update { it.copy(error = message) }
    }

    private companion object {
        /** Five reads gives four 5-second gaps — about 20 seconds of grace per wait. */
        const val POLL_ATTEMPTS = 5
        const val MANDATE_POLL_ATTEMPTS = 5
        const val POLL_INTERVAL_MS = 5_000L

        const val MANDATE_FAILED_MESSAGE = "We could not confirm your autopay mandate."

        const val MANDATE_PENDING_MESSAGE =
            "Your autopay mandate has not been approved yet. Please try again in a moment."

        const val SIP_FAILED_MESSAGE = "This SIP could not be set up. Please try again."

        const val SIP_TIMED_OUT_MESSAGE =
            "Your SIP is taking longer than usual to set up. Please try again in a moment."
    }
}
