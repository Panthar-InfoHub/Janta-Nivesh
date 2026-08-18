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
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchasePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SchemePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SipThreshold
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.CreatePurchasePlanUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.GetPurchasePlanUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.GetSchemePlansUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.RequestPurchasePlanOtpUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.VerifyPurchasePlanOtpUseCase

/** Drives the box count, the input cap and the completeness check — change it in one place. */
const val OTP_LENGTH = 4

/**
 * The three server round trips behind the Start SIP button. Naming the current one keeps the
 * button honest during the review wait, which can run to about twenty seconds.
 */
enum class SipSubmissionStage(val message: String) {
    CREATING("Setting up your SIP\u2026"),
    AWAITING_REVIEW("Verifying with the exchange\u2026"),
    REQUESTING_OTP("Sending OTP\u2026")
}

data class ChoosePlanUiState(
    val schemes: List<SchemePlan> = emptyList(),
    val selectedScheme: SchemePlan? = null,
    val amount: String = "",
    val debitDay: Int? = null,
    val showDebitDayPicker: Boolean = false,
    val isLoadingSchemes: Boolean = false,
    /** Non-null only while the Start SIP chain is in flight. */
    val submissionStage: SipSubmissionStage? = null,

    // OTP sheet
    val showOtpSheet: Boolean = false,
    /** The plan created by the first call — its id keys both OTP calls. */
    val createdPlan: PurchasePlan? = null,
    val otp: String = "",
    val isVerifyingOtp: Boolean = false
) {
    val isSubmitting: Boolean
        get() = submissionStage != null

    private val threshold: SipThreshold?
        get() = selectedScheme?.monthlySip

    val minimumAmount: Int
        get() = threshold?.amountMin ?: DEFAULT_MINIMUM_AMOUNT

    val debitDays: List<Int>
        get() = threshold?.dates ?: SipThreshold.ALL_DEBIT_DAYS

    val monthlyAmount: Int
        get() = amount.toIntOrNull() ?: 0

    val isAmountValid: Boolean
        get() = threshold?.isAmountAllowed(monthlyAmount) ?: (monthlyAmount >= minimumAmount)

    val canSubmit: Boolean
        get() = !isSubmitting &&
                !isLoadingSchemes &&
                selectedScheme != null &&
                debitDay != null &&
                monthlyAmount > 0 &&
                isAmountValid

    val isOtpComplete: Boolean
        get() = otp.length == OTP_LENGTH

    private companion object {
        const val DEFAULT_MINIMUM_AMOUNT = 100
    }
}

sealed interface ChoosePlanEvent {
    data class OnSchemeSelected(val scheme: SchemePlan) : ChoosePlanEvent
    data class OnAmountChange(val value: String) : ChoosePlanEvent
    data object OnDebitDayClick : ChoosePlanEvent
    data object OnDebitDayPickerDismiss : ChoosePlanEvent
    data class OnDebitDaySelected(val day: Int) : ChoosePlanEvent
    data object OnStartSipClick : ChoosePlanEvent

    data class OnOtpChange(val otp: String) : ChoosePlanEvent
    data object OnAuthoriseClick : ChoosePlanEvent
    data object OnOtpSheetDismiss : ChoosePlanEvent
}

sealed interface ChoosePlanEffect {
    /** Carries the confirmed plan so the success screen can show what was registered. */
    data class PurchasePlanConfirmed(
        val plan: PurchasePlan,
        val schemeName: String
    ) : ChoosePlanEffect
}

class ChoosePlanViewModel(
    private val getSchemePlans: GetSchemePlansUseCase,
    private val createPurchasePlan: CreatePurchasePlanUseCase,
    private val getPurchasePlan: GetPurchasePlanUseCase,
    private val requestPurchasePlanOtp: RequestPurchasePlanOtpUseCase,
    private val verifyPurchasePlanOtp: VerifyPurchasePlanOtpUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChoosePlanUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<ChoosePlanEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadSchemes()
    }

    fun handleEvent(event: ChoosePlanEvent) {
        when (event) {
            is ChoosePlanEvent.OnSchemeSelected -> onSchemeSelected(event.scheme)

            is ChoosePlanEvent.OnAmountChange -> _uiState.update {
                it.copy(amount = event.value.filter { char -> char.isDigit() }.take(MAX_AMOUNT_DIGITS))
            }

            ChoosePlanEvent.OnDebitDayClick ->
                _uiState.update { it.copy(showDebitDayPicker = true) }

            ChoosePlanEvent.OnDebitDayPickerDismiss ->
                _uiState.update { it.copy(showDebitDayPicker = false) }

            is ChoosePlanEvent.OnDebitDaySelected ->
                _uiState.update { it.copy(debitDay = event.day, showDebitDayPicker = false) }

            ChoosePlanEvent.OnStartSipClick -> onStartSipClick()

            is ChoosePlanEvent.OnOtpChange -> _uiState.update {
                it.copy(otp = event.otp.filter { char -> char.isDigit() }.take(OTP_LENGTH))
            }

            ChoosePlanEvent.OnAuthoriseClick -> onAuthoriseClick()

            ChoosePlanEvent.OnOtpSheetDismiss ->
                _uiState.update { it.copy(showOtpSheet = false, otp = "") }
        }
    }

    private fun loadSchemes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSchemes = true) }

            when (val result = getSchemePlans()) {
                is NetworkResponse.Error -> {
                    _uiState.update { it.copy(isLoadingSchemes = false) }
                    SnackBarController.showError(result.error.message)
                }

                // Nothing is pre-selected: the amount and debit day only appear once the user
                // picks a fund, and those inputs depend on that fund's thresholds.
                is NetworkResponse.Success -> _uiState.update {
                    it.copy(
                        schemes = result.data.filter { scheme -> scheme.isSipAvailable },
                        isLoadingSchemes = false
                    )
                }
            }
        }
    }

    /**
     * Schemes differ in which debit days they accept, so a day the previous scheme allowed is
     * dropped rather than silently carried into a request the gateway would reject.
     */
    private fun onSchemeSelected(scheme: SchemePlan) {
        _uiState.update { state ->
            val allowedDays = scheme.monthlySip?.dates ?: SipThreshold.ALL_DEBIT_DAYS
            state.copy(
                selectedScheme = scheme,
                debitDay = state.debitDay?.takeIf { it in allowedDays } ?: allowedDays.firstOrNull()
            )
        }
    }

    /**
     * Registers the plan, reads it back, then asks for the OTP. The sheet only opens once all
     * three land, so it is never shown against a plan the gateway has not accepted — and the
     * read-back is what fills in the fields the create response leaves out.
     */
    private fun onStartSipClick() {
        val state = _uiState.value
        if (!state.canSubmit) return

        val scheme = state.selectedScheme ?: return
        val debitDay = state.debitDay ?: return

        viewModelScope.launch {
            setStage(SipSubmissionStage.CREATING)

            val createResult = createPurchasePlan(
                scheme = scheme.isin,
                amount = state.monthlyAmount,
                installmentDay = debitDay
            )

            val createdPlan = when (createResult) {
                is NetworkResponse.Error -> {
                    failSubmission(createResult.error.message)
                    return@launch
                }

                is NetworkResponse.Success -> createResult.data
            }

            // Every gateway call from here on keys on the plan id from the create response; the
            // read-back only enriches what is displayed.
            val planId = createdPlan.id

            val fetchedPlan = awaitPlanReview(planId) ?: return@launch

            // The plan may have come back failed; the OTP request still runs, and the server
            // decides whether it is allowed.
            setStage(SipSubmissionStage.REQUESTING_OTP)

            when (val otpResult = requestPurchasePlanOtp(planId)) {
                is NetworkResponse.Error -> failSubmission(otpResult.error.message)

                is NetworkResponse.Success -> _uiState.update {
                    it.copy(
                        submissionStage = null,
                        createdPlan = fetchedPlan,
                        showOtpSheet = true,
                        otp = ""
                    )
                }
            }
        }
    }

    /**
     * Polls the plan while the gateway reviews it, up to [PLAN_POLL_ATTEMPTS] reads spaced
     * [PLAN_POLL_INTERVAL_MS] apart. Returns as soon as the plan leaves the `created` state, or
     * hands back the last read once the attempts run out — the OTP request goes ahead either way.
     *
     * Returns null only when a read fails outright, which ends the submission.
     */
    private suspend fun awaitPlanReview(planId: String): PurchasePlan? {
        var lastPlan: PurchasePlan? = null
        setStage(SipSubmissionStage.AWAITING_REVIEW)

        repeat(PLAN_POLL_ATTEMPTS) { attempt ->
            // The first read happens immediately; the plan is often reviewed by then.
            if (attempt > 0) delay(PLAN_POLL_INTERVAL_MS)

            when (val result = getPurchasePlan(planId)) {
                is NetworkResponse.Error -> {
                    failSubmission(result.error.message)
                    return null
                }

                is NetworkResponse.Success -> {
                    lastPlan = result.data
                    if (!result.data.isUnderReview) return result.data
                }
            }
        }

        return lastPlan
    }

    private fun setStage(stage: SipSubmissionStage) {
        _uiState.update { it.copy(submissionStage = stage) }
    }

    private suspend fun failSubmission(message: String) {
        _uiState.update { it.copy(submissionStage = null) }
        SnackBarController.showError(message)
    }

    private fun onAuthoriseClick() {
        val state = _uiState.value
        if (state.isVerifyingOtp || !state.isOtpComplete) return

        val plan = state.createdPlan ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isVerifyingOtp = true) }

            val result = verifyPurchasePlanOtp(plan.id, state.otp)
            _uiState.update { it.copy(isVerifyingOtp = false) }

            when (result) {
                is NetworkResponse.Error -> SnackBarController.showError(result.error.message)

                is NetworkResponse.Success -> {
                    val schemeName = state.selectedScheme?.schemeName ?: result.data.scheme
                    resetForNextPurchase()
                    _effect.send(
                        ChoosePlanEffect.PurchasePlanConfirmed(
                            plan = result.data,
                            schemeName = schemeName
                        )
                    )
                }
            }
        }
    }

    /**
     * Clears everything tied to the plan just confirmed, so coming back to this screen starts
     * clean instead of re-showing a stale sheet or a spent plan id.
     */
    private fun resetForNextPurchase() {
        _uiState.update { state ->
            state.copy(
                amount = "",
                showOtpSheet = false,
                createdPlan = null,
                otp = "",
                debitDay = state.selectedScheme?.monthlySip?.dates?.firstOrNull()
            )
        }
    }


    private companion object {
        const val MAX_AMOUNT_DIGITS = 9

        /** Five reads gives four 5-second gaps — about 20 seconds of grace for the review. */
        const val PLAN_POLL_ATTEMPTS = 5
        const val PLAN_POLL_INTERVAL_MS = 5_000L
    }
}
