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
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.WebURLConstants
import org.velvetinvesting.jantanivesh.app.features.core.utils.AmountTypeLabel
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MandateOption
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchaseMode
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SchemePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SipThreshold
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.CreateMandateUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.CreateMfPurchaseUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.GetMandatesUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.GetMfPurchaseUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.GetSchemePlanUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.RequestMfPurchaseOtpUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.VerifyMfPurchaseOtpUseCase
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

/** How long the resend link stays disabled after an OTP goes out, in seconds. */
const val OTP_RESEND_SECONDS = 24

/** Drives the box count, the input cap and the completeness check — change it in one place. */
const val OTP_LENGTH = 6

/**
 * The server round trips behind the submit button, named so the button stays honest during the
 * waits — the review poll alone can run about twenty seconds, and a bare spinner reads as a hang.
 */
enum class SipSubmissionStage(val message: String) {
    /** A SIP starts by registering the autopay mandate its installments will be debited from. */
    CREATING_MANDATE("Setting up autopay\u2026"),
    CREATING("Setting up your investment\u2026"),
    AWAITING_REVIEW("Verifying with the exchange\u2026"),
    REQUESTING_OTP("Sending OTP\u2026"),

    /** Only the lumpsum flow reaches this: the user is back from the payment page. */
    AWAITING_PAYMENT("Confirming your payment\u2026")
}

data class FundPurchaseUiState(
    /** Carried on the route so the header reads correctly before the scheme lookup lands. */
    val fundName: String = "",
    val fundSubtitle: String = "",
    val scheme: SchemePlan? = null,
    val isLoadingScheme: Boolean = true,
    val loadError: String? = null,

    val mode: PurchaseMode = PurchaseMode.MONTHLY,
    val amount: String = "",
    val installmentDay: Int? = null,
    val showDayPicker: Boolean = false,

    /** Approved mandates only — anything else cannot carry a debit, so it is never offered. */
    val mandates: List<MandateOption> = emptyList(),
    val selectedMandateId: String? = null,
    val isLoadingMandates: Boolean = false,
    val showMandateSheet: Boolean = false,

    /** Non-null only while the create → poll → OTP chain is in flight. */
    val submissionStage: SipSubmissionStage? = null,

    val showOtpSheet: Boolean = false,
    /** Gateway id of the plan or purchase awaiting confirmation; both OTP calls key on it. */
    val pendingPurchaseId: String? = null,
    val otp: String = "",
    val isVerifyingOtp: Boolean = false,
    val resendSecondsLeft: Int = 0,
    /** Masked mobile the OTP went to, as the sheet shows it. */
    val otpDestination: String = "",

    /**
     * Set once a lumpsum purchase is authorised. It is held rather than only emitted so the
     * screen can offer the link again if the user backs out of the payment page.
     */
    val paymentUrl: String? = null
) {
    val isSubmitting: Boolean
        get() = submissionStage != null

    /** Which modes the gateway actually offers for this scheme — a missing threshold hides a tab. */
    val availableModes: List<PurchaseMode>
        get() = scheme?.let { plan ->
            PurchaseMode.entries.filter { plan.thresholdFor(it) != null }
        }.orEmpty().ifEmpty { PurchaseMode.entries }

    val threshold: SipThreshold?
        get() = scheme?.thresholdFor(mode)

    val minimumAmount: Int
        get() = threshold?.amountMin ?: DEFAULT_MINIMUM_AMOUNT

    val suggestedAmounts: List<Int>
        get() = threshold?.suggestedAmounts
            ?: listOf(1, 2, 5, 10).map { it * DEFAULT_MINIMUM_AMOUNT }

    val debitDays: List<Int>
        get() = threshold?.dates ?: SipThreshold.ALL_DEBIT_DAYS

    val enteredAmount: Int
        get() = amount.toIntOrNull() ?: 0

    val isAmountValid: Boolean
        get() = threshold?.isAmountAllowed(enteredAmount) ?: (enteredAmount >= minimumAmount)

    /**
     * Why the typed amount is not accepted, or null when it is. Each mode carries its own floor,
     * ceiling and step, so naming the one that failed beats a single generic minimum message.
     */
    val amountError: String?
        get() {
            val limits = threshold ?: return null
            if (amount.isEmpty() || isAmountValid) return null

            return when {
                enteredAmount < limits.amountMin -> "Enter at least ₹${limits.amountMin}"
                enteredAmount > limits.amountMax -> "The most you can invest is ₹${limits.amountMax}"
                else -> "Enter an amount in multiples of ₹${limits.amountMultiples}"
            }
        }

    val selectedMandate: MandateOption?
        get() = mandates.firstOrNull { it.id == selectedMandateId }

    /**
     * The date the units are expected to be priced at. A monthly SIP prices on its debit day; a
     * daily SIP and a one-time buy price on the next working opportunity, which the app shows as
     * tomorrow — the gateway's own `scheduled_on` replaces this once the purchase is created.
     */
    val expectedNavDate: LocalDate
        get() {
            val today = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date

            val day = installmentDay.takeIf { mode.needsInstallmentDay }
                ?: return today.plus(DatePeriod(days = 1))

            return nextOccurrenceOf(day, today)
        }

    /** "SIP - SBI Gold Fund" for the two SIP modes, "Buy - …" for a one-time purchase. */
    val headerTitle: String
        get() {
            val name = fundName.takeIf { it.isNotBlank() }
                ?: scheme?.schemeName.orEmpty()
            val prefix = if (mode.isSip) "SIP" else "Buy"
            return if (name.isBlank()) prefix else "$prefix - $name"
        }

    /**
     * The `mf_product_id` a purchase is placed against. The scheme lookup is authoritative — the
     * route's id comes from a different listing and is only used until that lookup lands.
     */
    fun productId(routeProductId: String): String =
        scheme?.id?.takeIf { it.isNotBlank() } ?: routeProductId

    val canSubmit: Boolean
        get() = !isSubmitting &&
                // A purchase already awaiting its OTP must not be joined by a second one.
                !showOtpSheet &&
                !isLoadingScheme &&
                scheme != null &&
                enteredAmount > 0 &&
                isAmountValid &&
                (!mode.needsInstallmentDay || installmentDay != null)

    val isOtpComplete: Boolean
        get() = otp.length == OTP_LENGTH

    /** The primary button's label, which names the mode and the amount the user has typed. */
    val submitLabel: String
        get() = submissionStage?.message ?: when (mode) {
            PurchaseMode.DAILY -> "Start Daily SIP · ₹$enteredAmount/day"
            PurchaseMode.MONTHLY -> "Start Monthly SIP · ₹$enteredAmount/month"
            PurchaseMode.ONE_TIME -> "Invest ₹$enteredAmount"
        }

    private companion object {
        const val DEFAULT_MINIMUM_AMOUNT = 500
    }
}

private val PurchaseMode.orderFailedMessage: String
    get() = if (isSip) {
        "This SIP could not be set up. Please try again."
    } else {
        "This purchase could not be placed. Please try again."
    }

private val PurchaseMode.reviewTimedOutMessage: String
    get() = if (isSip) {
        "Your SIP is taking longer than usual to set up. Please try again in a moment."
    } else {
        "Your purchase is taking longer than usual. Please try again in a moment."
    }

/**
 * Reads as a calendar would: the same day this month if it is still ahead, otherwise next month.
 * Debit days never exceed 28, so the day always exists in the month it lands in.
 */
private fun nextOccurrenceOf(day: Int, today: LocalDate): LocalDate {
    val thisMonth = LocalDate(year = today.year, month = today.month, day = day)
    if (thisMonth > today) return thisMonth

    val nextMonth = today.plus(DatePeriod(months = 1))
    return LocalDate(year = nextMonth.year, month = nextMonth.month, day = day)
}

sealed interface FundPurchaseEvent {
    data class OnModeSelected(val mode: PurchaseMode) : FundPurchaseEvent

    /** One tap on the on-screen keypad: a digit, or [KEYPAD_BACKSPACE] to delete. */
    data class OnKeypadPress(val key: String) : FundPurchaseEvent
    data class OnSuggestedAmountClick(val amount: Int) : FundPurchaseEvent

    data object OnDayFieldClick : FundPurchaseEvent
    data object OnDayPickerDismiss : FundPurchaseEvent
    data class OnDaySelected(val day: Int) : FundPurchaseEvent

    data object OnMandateFieldClick : FundPurchaseEvent
    data object OnMandateSheetDismiss : FundPurchaseEvent
    data object OnAddMandateClick : FundPurchaseEvent
    data class OnMandateSelected(val mandateId: String) : FundPurchaseEvent
    data object OnMandateConfirm : FundPurchaseEvent

    data object OnSubmitClick : FundPurchaseEvent
    data object OnRetryLoad : FundPurchaseEvent

    data class OnOtpChange(val otp: String) : FundPurchaseEvent
    data object OnConfirmOtpClick : FundPurchaseEvent
    data object OnResendOtpClick : FundPurchaseEvent
    data object OnOtpSheetDismiss : FundPurchaseEvent
}

/**
 * What the SIP setup that follows the mandate needs, gathered on this screen and carried through
 * the authorization web view to the screen that finishes the purchase.
 */
data class SipMandateHandoff(
    /** The gateway's numeric mandate id, which the status lookup is polled on. */
    val mandateId: Int,
    /** The server's own mandate record id, which the create-SIP body is keyed on. */
    val mandateRecordId: String,
    val mfProductId: String,
    val schemeName: String,
    val amount: Int,
    val mode: PurchaseMode,
    /** Zero for a daily SIP, which has no debit day. */
    val installmentDay: Int
)

sealed interface FundPurchaseEffect {
    /** The user has no approved mandate, so autopay has to be set up before a SIP can run. */
    data object AddMandate : FundPurchaseEffect

    /**
     * A SIP's mandate has been created and needs the user's approval on the bank's page. The
     * caller opens this and calls [FundPurchaseViewModel.onMandateAuthorizationReturned] when
     * the user comes back.
     */
    data class OpenMandateAuthorization(val url: String) : FundPurchaseEffect

    /** The user is back from the mandate page; the rest of the SIP runs on its own screen. */
    data class StartSipSetup(val handoff: SipMandateHandoff) : FundPurchaseEffect

    /**
     * A lumpsum purchase is authorised but not paid. The caller opens this in the web view and
     * calls [FundPurchaseViewModel.onPaymentReturned] when the user comes back.
     */
    data class OpenPayment(val url: String) : FundPurchaseEffect

    data class PurchaseConfirmed(
        /** What was bought, which is what decides the success screen's wording. */
        val mode: PurchaseMode,
        val schemeName: String,
        val amount: String,
        /** Zero when the mode has no debit day, which the success screen reads as "none". */
        val installmentDay: Int,
        val startDate: String
    ) : FundPurchaseEffect
}

const val KEYPAD_BACKSPACE = "<"

/**
 * Drives the whole purchase screen: the mode tabs, the keypad amount, the debit day, the mandate
 * choice and the OTP confirmation.
 *
 * The three purchase modes share one submission shape — create, poll while the gateway reviews,
 * then ask for the OTP — but a SIP and a one-time buy run it on different endpoints, so the
 * chain forks once at each step rather than being duplicated per mode.
 */
class FundPurchaseViewModel(
    /**
     * Product id carried on the route, from the fund listing. It is only a fallback: the scheme
     * lookup returns the id the purchase endpoints actually key on, and that one wins.
     */
    private val mfProductId: String,
    private val isin: String,
    private val fundName: String,
    private val fundSubtitle: String,
    private val fundAmountType: String?,
    private val getSchemePlan: GetSchemePlanUseCase,
    private val getMandates: GetMandatesUseCase,
    private val createMandate: CreateMandateUseCase,
    private val createMfPurchase: CreateMfPurchaseUseCase,
    private val getMfPurchase: GetMfPurchaseUseCase,
    private val requestMfPurchaseOtp: RequestMfPurchaseOtpUseCase,
    private val verifyMfPurchaseOtp: VerifyMfPurchaseOtpUseCase
) : ViewModel() {

    private val initialAmountType = AmountTypeLabel.getType(fundAmountType)

    private val _uiState = MutableStateFlow(
        FundPurchaseUiState(
            fundName = fundName,
            fundSubtitle = fundSubtitle,
            mode = when (initialAmountType) {
                AmountTypeLabel.DailyTen -> PurchaseMode.DAILY
                AmountTypeLabel.MonthlyHundred -> PurchaseMode.MONTHLY
                null -> PurchaseMode.MONTHLY

            },
            amount = when (initialAmountType) {
                AmountTypeLabel.DailyTen -> "10"
                AmountTypeLabel.MonthlyHundred -> "100"
                null -> ""
            }
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<FundPurchaseEffect>()
    val effect = _effect.receiveAsFlow()

    /**
     * Held from the mandate creation so the return from the authorization page can hand the SIP
     * on without re-reading anything the user typed.
     */
    private var pendingSipHandoff: SipMandateHandoff? = null

    init {
        loadScheme()
        loadMandates()
    }

    fun handleEvent(event: FundPurchaseEvent) {
        when (event) {
            is FundPurchaseEvent.OnModeSelected -> onModeSelected(event.mode)

            is FundPurchaseEvent.OnKeypadPress -> onKeypadPress(event.key)

            is FundPurchaseEvent.OnSuggestedAmountClick -> _uiState.update {
                it.copy(amount = event.amount.toString())
            }

            FundPurchaseEvent.OnDayFieldClick ->
                _uiState.update { it.copy(showDayPicker = true) }

            FundPurchaseEvent.OnDayPickerDismiss ->
                _uiState.update { it.copy(showDayPicker = false) }

            is FundPurchaseEvent.OnDaySelected -> _uiState.update {
                // A day the gateway does not offer is ignored rather than sent on to be rejected.
                if (event.day in it.debitDays) {
                    it.copy(installmentDay = event.day, showDayPicker = false)
                } else {
                    it
                }
            }

            FundPurchaseEvent.OnMandateFieldClick -> {
                _uiState.update { it.copy(showMandateSheet = true) }
                if (_uiState.value.mandates.isEmpty()) loadMandates()
            }

            FundPurchaseEvent.OnMandateSheetDismiss,
            FundPurchaseEvent.OnMandateConfirm ->
                _uiState.update { it.copy(showMandateSheet = false) }

            FundPurchaseEvent.OnAddMandateClick -> {
                // The sheet closes first so the user does not come back from autopay setup to a
                // stale list sitting open on top of the screen.
                _uiState.update { it.copy(showMandateSheet = false) }
                viewModelScope.launch { _effect.send(FundPurchaseEffect.AddMandate) }
            }

            is FundPurchaseEvent.OnMandateSelected -> _uiState.update {
                it.copy(selectedMandateId = event.mandateId)
            }

            FundPurchaseEvent.OnSubmitClick -> onSubmitClick()

            FundPurchaseEvent.OnRetryLoad -> {
                loadScheme()
                loadMandates()
            }

            is FundPurchaseEvent.OnOtpChange -> _uiState.update {
                it.copy(otp = event.otp.filter { char -> char.isDigit() }.take(OTP_LENGTH))
            }

            FundPurchaseEvent.OnConfirmOtpClick -> onConfirmOtpClick()

            FundPurchaseEvent.OnResendOtpClick -> onResendOtpClick()

            FundPurchaseEvent.OnOtpSheetDismiss ->
                _uiState.update { it.copy(showOtpSheet = false, otp = "") }
        }
    }

    private fun loadScheme() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingScheme = true, loadError = null) }

            when (val result = getSchemePlan(isin)) {
                is NetworkResponse.Error -> _uiState.update {
                    it.copy(isLoadingScheme = false, loadError = result.error.message)
                }

                is NetworkResponse.Success -> _uiState.update { state ->
                    val scheme = result.data
                    // Land on a mode the scheme actually offers, then seed the debit day from
                    // that mode's own allowed days.
                    val mode = when {
                        scheme.thresholdFor(state.mode) != null -> state.mode

                        else -> listOf(
                            PurchaseMode.MONTHLY,
                            PurchaseMode.DAILY,
                            PurchaseMode.ONE_TIME
                        ).firstOrNull { scheme.thresholdFor(it) != null }
                            ?: state.mode
                    }

                    state.copy(
                        scheme = scheme,
                        mode = mode,
                        isLoadingScheme = false,
                        loadError = null,
                        fundName = state.fundName.ifBlank { scheme.schemeName },
                        fundSubtitle = state.fundSubtitle.ifBlank { scheme.fundName },
                        installmentDay = scheme.thresholdFor(mode)?.dates?.firstOrNull()
                    )
                }
            }
        }
    }

    /**
     * Mandates are only needed to label the payment row, so a failure here is left silent — it
     * must not block a one-time buy, which does not use a mandate at all.
     */
    /**
     * Re-reads the mandates. Called when the user comes back from autopay setup, so a mandate
     * they just approved shows up without leaving the purchase screen.
     */
    fun refreshMandates() = loadMandates()

    private fun loadMandates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMandates = true) }

            val result = getMandates()
            _uiState.update { state ->
                val mandates = (result as? NetworkResponse.Success)?.data.orEmpty()
                    .filter { it.isApproved }

                state.copy(
                    mandates = mandates,
                    isLoadingMandates = false,
                    // Keep the user's choice if it survived the refresh; otherwise fall back to
                    // the first approved mandate so the row is never left blank.
                    selectedMandateId = state.selectedMandateId
                        ?.takeIf { id -> mandates.any { it.id == id } }
                        ?: mandates.firstOrNull()?.id
                )
            }
        }
    }

    /**
     * Modes have different minimums and different debit days, so switching re-seeds both rather
     * than carrying over an amount or a day the new mode would reject.
     */
    private fun onModeSelected(mode: PurchaseMode) {
        _uiState.update { state ->
            if (mode == state.mode) return@update state

            val threshold = state.scheme?.thresholdFor(mode)
            val allowedDays = threshold?.dates ?: SipThreshold.ALL_DEBIT_DAYS

            state.copy(
                mode = mode,
                amount = state.amount.takeIf { value ->
                    value.toIntOrNull()?.let { threshold?.isAmountAllowed(it) ?: true } == true
                }.orEmpty(),
                installmentDay = state.installmentDay?.takeIf { it in allowedDays }
                    ?: allowedDays.firstOrNull()
            )
        }
    }

    private fun onKeypadPress(key: String) {
        _uiState.update { state ->
            val next = when {
                key == KEYPAD_BACKSPACE -> state.amount.dropLast(1)
                // The keypad has a decimal key, but every threshold is in whole rupees and the
                // request bodies take integers, so a decimal point is simply not accepted.
                !key.all { it.isDigit() } -> state.amount
                // A leading zero would read as "0500" in the display.
                state.amount.isEmpty() && key == "0" -> state.amount
                state.amount.length >= MAX_AMOUNT_DIGITS -> state.amount
                else -> state.amount + key
            }
            state.copy(amount = next)
        }
    }

    /**
     * A SIP cannot be registered without a mandate to debit, so the submit button starts one:
     * the mandate is created for exactly the amount the user typed, and the rest of the SIP
     * happens after they approve it. A one-time buy needs no mandate and runs here as before.
     */
    private fun onSubmitClick() {
        val state = _uiState.value
        if (!state.canSubmit) return

        if (state.mode.isSip) {
            startSipMandate(state)
            return
        }

        viewModelScope.launch {
            setStage(SipSubmissionStage.CREATING)

            val purchaseId = createLumpsum(state) ?: return@launch

            val ready = awaitPurchaseConfirmable(purchaseId)
            if (!ready) return@launch

            setStage(SipSubmissionStage.REQUESTING_OTP)

            when (val otpResult = requestMfPurchaseOtp(purchaseId)) {
                is NetworkResponse.Error -> failSubmission(otpResult.error.message)

                is NetworkResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            submissionStage = null,
                            pendingPurchaseId = purchaseId,
                            showOtpSheet = true,
                            otp = ""
                        )
                    }
                    startResendCountdown()
                }
            }
        }
    }

    /**
     * The mandate's ceiling is the installment itself: a mandate approved for more than the SIP
     * debits would be asking the user for headroom this purchase never uses.
     */
    private fun startSipMandate(state: FundPurchaseUiState) {
        viewModelScope.launch {
            setStage(SipSubmissionStage.CREATING_MANDATE)

            val result = createMandate(
                mandateLimit = state.enteredAmount.toLong(),
                validFrom = DateTimeUtils.today().toString(),
                paymentPostbackUrl = PAYMENT_POSTBACK_URL
            )

            when (result) {
                is NetworkResponse.Error -> failSubmission(result.error.message)

                is NetworkResponse.Success -> {
                    val mandate = result.data
                    val tokenUrl = mandate.tokenUrl
                    val mandateId = mandate.id
                    val recordId = mandate.recordId

                    if (mandateId == null || recordId.isNullOrBlank() || tokenUrl.isNullOrBlank()) {
                        failSubmission(MANDATE_FAILED_MESSAGE)
                        return@launch
                    }

                    pendingSipHandoff = SipMandateHandoff(
                        mandateId = mandateId,
                        mandateRecordId = recordId,
                        mfProductId = state.productId(mfProductId),
                        schemeName = state.scheme?.schemeName?.takeIf { it.isNotBlank() }
                            ?: state.fundName,
                        amount = state.enteredAmount,
                        mode = state.mode,
                        installmentDay = state.installmentDay
                            ?.takeIf { state.mode.needsInstallmentDay }
                            ?: 0
                    )

                    _uiState.update { it.copy(submissionStage = null) }
                    _effect.send(FundPurchaseEffect.OpenMandateAuthorization(tokenUrl))
                }
            }
        }
    }

    /**
     * Called when the user comes back from the mandate authorization page. Whether they actually
     * approved it is only knowable from the server, so the SIP screen takes over and polls.
     */
    fun onMandateAuthorizationReturned() {
        val handoff = pendingSipHandoff ?: return

        viewModelScope.launch {
            _effect.send(FundPurchaseEffect.StartSipSetup(handoff))
        }
    }

    private suspend fun createLumpsum(state: FundPurchaseUiState): String? {
        val result = createMfPurchase(
            mfProductId = state.productId(mfProductId),
            amount = state.enteredAmount
        )

        return when (result) {
            is NetworkResponse.Error -> {
                failSubmission(result.error.message)
                null
            }

            is NetworkResponse.Success -> result.data.id
        }
    }

    /**
     * Waits for the purchase to become confirmable, polling up to [POLL_ATTEMPTS] times at
     * [POLL_INTERVAL_MS] apart. The gateway only accepts an OTP once its own review has finished,
     * so anything short of that means keep waiting, and running out of attempts ends the
     * submission rather than firing a request that would be rejected.
     *
     * False means the submission is over: a read failed, the purchase failed, or it never became
     * confirmable in time.
     */
    private suspend fun awaitPurchaseConfirmable(purchaseId: String): Boolean {
        setStage(SipSubmissionStage.AWAITING_REVIEW)

        repeat(POLL_ATTEMPTS) { attempt ->
            // The first read happens immediately; the order is often reviewed by then.
            if (attempt > 0) delay(POLL_INTERVAL_MS.milliseconds)

            when (val result = getMfPurchase(purchaseId)) {
                is NetworkResponse.Error -> {
                    failSubmission(result.error.message)
                    return false
                }

                is NetworkResponse.Success -> when {
                    result.data.isReadyForOtp -> return true

                    result.data.hasFailed -> {
                        failSubmission(PurchaseMode.ONE_TIME.orderFailedMessage)
                        return false
                    }

                    // Still being reviewed — fall through to the next attempt.
                    else -> Unit
                }
            }
        }

        failSubmission(PurchaseMode.ONE_TIME.reviewTimedOutMessage)
        return false
    }

    /** Only a one-time purchase is confirmed here; a SIP's OTP has its own screen. */
    private fun onConfirmOtpClick() {
        val state = _uiState.value
        if (state.isVerifyingOtp || !state.isOtpComplete) return

        val purchaseId = state.pendingPurchaseId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isVerifyingOtp = true) }
            confirmLumpsum(purchaseId, state)
        }
    }

    /**
     * A lumpsum is only authorised by the OTP; the money moves on the gateway's payment page.
     * The sheet closes and the user is sent there, and the flow resumes in [onPaymentReturned].
     */
    private suspend fun confirmLumpsum(purchaseId: String, state: FundPurchaseUiState) {
        when (val result = verifyMfPurchaseOtp(purchaseId, state.otp)) {
            is NetworkResponse.Error -> failVerification(result.error.message)

            is NetworkResponse.Success -> {
                val confirmation = result.data
                val paymentUrl = confirmation.paymentUrl

                _uiState.update {
                    it.copy(
                        isVerifyingOtp = false,
                        showOtpSheet = false,
                        otp = "",
                        paymentUrl = paymentUrl
                    )
                }

                if (paymentUrl.isNullOrBlank()) {
                    // Authorised with nowhere to pay: report it rather than claiming success.
                    SnackBarController.showError(
                        "Your purchase was confirmed but the payment page is unavailable. " +
                                "Please check your orders in a moment."
                    )
                    return
                }

                _effect.send(FundPurchaseEffect.OpenPayment(paymentUrl))
            }
        }
    }

    /**
     * Called when the user comes back from the payment page. Coming back proves nothing about
     * whether they paid — they may have closed it — so the purchase is read back until it reports
     * `SUBMITTED` *and* its payment reports `SUCCESS`.
     *
     * The two move separately: the order is submitted the moment it reaches the exchange, while
     * the debit behind it can still be pending or can fail outright. Only a successful payment
     * finishes the flow; a pending one spends the remaining attempts, and a failed one stops here.
     */
    fun onPaymentReturned() {
        val state = _uiState.value
        val purchaseId = state.pendingPurchaseId ?: return

        viewModelScope.launch {
            setStage(SipSubmissionStage.AWAITING_PAYMENT)

            repeat(POLL_ATTEMPTS) { attempt ->
                if (attempt > 0) delay(POLL_INTERVAL_MS.milliseconds)

                when (val result = getMfPurchase(purchaseId)) {
                    is NetworkResponse.Error -> {
                        failSubmission(result.error.message)
                        return@launch
                    }

                    is NetworkResponse.Success -> {
                        val purchase = result.data

                        if (purchase.isSubmitted) {
                            when {
                                purchase.hasPaymentFailed -> {
                                    failSubmission(
                                        purchase.paymentFailureReason ?: PAYMENT_FAILED_MESSAGE
                                    )
                                    return@launch
                                }

                                purchase.isPaymentSuccessful -> {
                                    _uiState.update { it.copy(submissionStage = null) }
                                    emitConfirmed(
                                        ConfirmedPurchase(
                                            amount = purchase.amount,
                                            installmentDay = null,
                                            startDate = purchase.scheduledOn
                                        ),
                                        state
                                    )
                                    return@launch
                                }

                                // Submitted with the debit still in flight. Nothing has gone
                                // wrong yet, so the remaining attempts are spent waiting on it.
                                else -> Unit
                            }
                        }

                        if (purchase.hasFailed) {
                            failSubmission(PAYMENT_FAILED_MESSAGE)
                            return@launch
                        }
                    }
                }
            }

            // Still unpaid after the poll window. The purchase stays live on the server, so this
            // is reported as pending rather than as a failure.
            failSubmission(
                "We have not received your payment yet. It will show in your orders once it " +
                        "clears."
            )
        }
    }

    private suspend fun emitConfirmed(
        confirmed: ConfirmedPurchase,
        state: FundPurchaseUiState
    ) {
        val schemeName = state.scheme?.schemeName?.takeIf { it.isNotBlank() } ?: state.fundName

        resetForNextPurchase()

        _effect.send(
            FundPurchaseEffect.PurchaseConfirmed(
                mode = state.mode,
                schemeName = schemeName,
                // The gateway echoes the amount back, but not always; the typed amount stands in.
                amount = confirmed.amount.takeIf { it.isNotBlank() }
                    ?: state.enteredAmount.toString(),
                // A daily SIP and a lumpsum both carry a seeded debit day in state that means
                // nothing to them, so it is dropped rather than shown on the success screen.
                installmentDay = if (state.mode.needsInstallmentDay) {
                    confirmed.installmentDay ?: state.installmentDay ?: 0
                } else {
                    0
                },
                startDate = confirmed.startDate.orEmpty()
            )
        )
    }

    private fun onResendOtpClick() {
        val state = _uiState.value
        if (state.resendSecondsLeft > 0) return

        val purchaseId = state.pendingPurchaseId ?: return

        viewModelScope.launch {
            when (val result = requestMfPurchaseOtp(purchaseId)) {
                is NetworkResponse.Error -> SnackBarController.showError(result.error.message)
                is NetworkResponse.Success -> startResendCountdown()
            }
        }
    }

    private fun startResendCountdown() {
        viewModelScope.launch {
            _uiState.update { it.copy(resendSecondsLeft = OTP_RESEND_SECONDS) }

            while (_uiState.value.resendSecondsLeft > 0) {
                delay(SECOND_MS.milliseconds)
                _uiState.update { it.copy(resendSecondsLeft = it.resendSecondsLeft - 1) }
            }
        }
    }

    private fun setStage(stage: SipSubmissionStage) {
        _uiState.update { it.copy(submissionStage = stage) }
    }

    private suspend fun failSubmission(message: String) {
        _uiState.update { it.copy(submissionStage = null) }
        SnackBarController.showError(message)
    }

    /** The sheet stays open on a bad OTP so the user can correct it without starting over. */
    private suspend fun failVerification(message: String) {
        _uiState.update { it.copy(isVerifyingOtp = false, otp = "") }
        SnackBarController.showError(message)
    }

    /**
     * Clears everything tied to the purchase just confirmed, so coming back to this screen starts
     * clean instead of re-showing a stale sheet or a spent purchase id.
     */
    private fun resetForNextPurchase() {
        _uiState.update { state ->
            state.copy(
                amount = "",
                showOtpSheet = false,
                pendingPurchaseId = null,
                otp = "",
                resendSecondsLeft = 0,
                paymentUrl = null,
                installmentDay = state.threshold?.dates?.firstOrNull()
            )
        }
    }

    /** The two verify calls return different types; this is the slice the success screen needs. */
    private data class ConfirmedPurchase(
        val amount: String,
        val installmentDay: Int?,
        val startDate: String?
    )

    private companion object {
        const val MAX_AMOUNT_DIGITS = 9

        /** No server-side postback is wired up yet, so the mandate is sent the placeholder URL. */
        const val PAYMENT_POSTBACK_URL = WebURLConstants.mandateExitUrl

        const val MANDATE_FAILED_MESSAGE = "Autopay setup failed. Please try again."
        const val PAYMENT_FAILED_MESSAGE = "The payment did not go through. Please try again."
        const val SECOND_MS = 1_000L

        /** Five reads gives four 5-second gaps — about 20 seconds of grace per wait. */
        const val POLL_ATTEMPTS = 5
        const val POLL_INTERVAL_MS = 5_000L
    }
}
