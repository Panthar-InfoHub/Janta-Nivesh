package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels

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
import org.velvetinvesting.jantanivesh.app.core.utils.toCapital
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.AccountType
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.BankAccount
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.PrefilledBankDetails
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.ReversePennyDropLinks
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.UpiApp
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.GetPennyDropStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.GetPrefilledBankDetailsUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.GetReversePennyDropStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.InitiateReversePennyDropUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.SubmitPennyDropUseCase
import kotlin.time.Duration.Companion.milliseconds

data class BankAccountDetails(
    val bankName: String = "",
    val accountHolder: String = "",
    val accountNumber: String = "",
    val ifscCode: String = ""
) {
    /** Shows only the last four digits, e.g. `••••••••4856`. */
    val maskedAccountNumber: String
        get() = if (accountNumber.length <= VISIBLE_ACCOUNT_DIGITS) {
            accountNumber
        } else {
            "•".repeat(accountNumber.length - VISIBLE_ACCOUNT_DIGITS) +
                    accountNumber.takeLast(VISIBLE_ACCOUNT_DIGITS)
        }

    private companion object {
        const val VISIBLE_ACCOUNT_DIGITS = 4
    }
}

/**
 * Which part of the bank step is on screen. The whole reverse penny drop is one onboarding stage
 * (`PENNY_DROP_VERIFICATION`): entering it always starts at [LOADING], nothing in between saves a
 * stage, and the only way on is submitting the [FORM] — exactly where the old form-only step
 * ended. The form is reached once the bank details are known — either the server already has
 * them, or the user has just made the ₹1 payment that lets the server read them.
 */
enum class BankVerificationStep {
    /** Asking the server whether it already holds the bank details. */
    LOADING,
    /** That ask never reached the server (no internet, timeout); only a retry makes sense. */
    LOAD_ERROR,
    /** Explains the ₹1 verification and starts it. */
    INTRO,
    /** Picks the UPI app the ₹1 is paid from. */
    SELECT_APP,
    /** Back from the payment page, waiting for the bank to confirm the ₹1. */
    VERIFYING,
    /** The bank details form, filled from the verification. */
    FORM
}

/** One row on the app picker: a specific UPI app, or the page showing a QR code to scan. */
sealed interface UpiPaymentOption {
    data class App(val app: UpiApp) : UpiPaymentOption
    data object ScanQr : UpiPaymentOption
}

data class VerifyBankAccountUiState(
    val step: BankVerificationStep = BankVerificationStep.LOADING,
    val loadError: String? = null,
    /** True while the ₹1 verification is being raised from the intro. */
    val isInitiating: Boolean = false,
    val paymentLinks: ReversePennyDropLinks? = null,
    val selectedPaymentOption: UpiPaymentOption? = null,
    /** Shown on the intro after a verification failed, asking the user to start again. */
    val introNotice: String? = null,
    /** Shown on the app picker when the payment has not been confirmed yet. */
    val paymentNotice: String? = null,
    /** Whether [paymentNotice] can be followed up by reading the status again. */
    val canCheckPaymentStatus: Boolean = false,
    /**
     * A UPI app link the payment page closed on, waiting for the app picker to open it. The picker
     * opens it once it is back in front, so it can watch for the user's return from the app.
     */
    val appLinkToOpen: String? = null,
    /** Set while the user is away in the UPI app the picker opened. */
    val awaitingAppReturn: Boolean = false,

    val bankName: String = "",
    val accountType: AccountType? = null,
    val accountHolder: String = "",
    val accountNumber: String = "",
    val ifscCode: String = "",
    val isLoading: Boolean = false,
    val showConfirmBankAccountSheet: Boolean = false,
    val bankAccountDetails: BankAccountDetails = BankAccountDetails()
) {
    /** The apps the server handed out a link for, in the order the picker lists them. */
    val availableApps: List<UpiApp>
        get() = UpiApp.entries.filter { paymentLinks?.appLinks?.containsKey(it) == true }

    /** The QR option opens the generic validation page, so it needs that link. */
    val canPayByQr: Boolean
        get() = paymentLinks?.validationLink != null

    val canSubmit: Boolean
        get() = bankName.isNotBlank() &&
                accountType != null &&
                accountHolder.isNotBlank() &&
                accountNumber.isNotBlank() &&
                ifscCode.isNotBlank()
}

sealed interface VerifyBankAccountEvent {
    data class OnBankNameChange(val bankName: String) : VerifyBankAccountEvent
    data class OnAccountTypeChange(val accountType: AccountType) : VerifyBankAccountEvent
    data class OnAccountHolderChange(val accountHolder: String) : VerifyBankAccountEvent
    data class OnAccountNumberChange(val accountNumber: String) : VerifyBankAccountEvent
    data class OnIfscCodeChange(val ifscCode: String) : VerifyBankAccountEvent
    data object OnProceedClick : VerifyBankAccountEvent
    data object OnConfirmBankAccountClick : VerifyBankAccountEvent
    data object OnChangeBankAccountClick : VerifyBankAccountEvent
    data object OnDismissConfirmBankAccountSheet : VerifyBankAccountEvent

    data object OnRetryLoadClick : VerifyBankAccountEvent
    data object OnStartVerificationClick : VerifyBankAccountEvent
    data class OnPaymentOptionSelected(val option: UpiPaymentOption) : VerifyBankAccountEvent
    data object OnPayClick : VerifyBankAccountEvent
    data object OnSelectAppBackClick : VerifyBankAccountEvent

    /** Back from the intro: leaves the bank step for whatever screen came before it. */
    data object OnIntroBackClick : VerifyBankAccountEvent

    /** Back from the bank form: returns to the intro rather than leaving the step. */
    data object OnFormBackClick : VerifyBankAccountEvent
    data object OnCheckPaymentStatusClick : VerifyBankAccountEvent

    /** The user is back from the payment page, whether or not they paid. */
    data object OnPaymentPageReturned : VerifyBankAccountEvent

    /** The payment page closed on this UPI app link instead of opening it. */
    data class OnUpiAppLinkCaught(val url: String) : VerifyBankAccountEvent

    /** The picker is opening [VerifyBankAccountUiState.appLinkToOpen] now. */
    data object OnAppLinkOpening : VerifyBankAccountEvent

    /** No app took the link — its app is not installed. */
    data object OnAppLinkNotOpened : VerifyBankAccountEvent

    /**
     * Back from the UPI app. [wasStopped] is whether this app was fully backgrounded meanwhile —
     * without it the user may only have dismissed the app chooser.
     */
    data class OnReturnedFromUpiApp(val wasStopped: Boolean) : VerifyBankAccountEvent
    data object SwitchToForm : VerifyBankAccountEvent
}

sealed interface VerifyBankAccountEffect {
    data object PennyDropCompleted : VerifyBankAccountEffect
    data object NavigateToChangeBankAccount : VerifyBankAccountEffect

    /** Opens the ₹1 payment page in the web view; its return is reported as a page return. */
    data class OpenPaymentPage(val url: String) : VerifyBankAccountEffect

    /** Leaves the bank step, back to the previous screen. */
    data object NavigateBack : VerifyBankAccountEffect
}

class VerifyBankAccountViewModel(
    private val submitPennyDrop: SubmitPennyDropUseCase,
    private val getPennyDropStatus: GetPennyDropStatusUseCase,
    private val getPrefilledBankDetails: GetPrefilledBankDetailsUseCase,
    private val initiateReversePennyDrop: InitiateReversePennyDropUseCase,
    private val getReversePennyDropStatus: GetReversePennyDropStatusUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(VerifyBankAccountUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<VerifyBankAccountEffect>()
    val effect = _effect.receiveAsFlow()

    /** Set while the payment page is open, so only a return from it starts the status poll. */
    private var awaitingPaymentReturn = false

    /** Whether the last prefill read followed a confirmed payment; a retry repeats it as such. */
    private var prefillAfterPayment = false

    init {
        loadPrefill(afterPayment = false)
    }

    fun handleEvent(event: VerifyBankAccountEvent) {
        when (event) {
            is VerifyBankAccountEvent.OnBankNameChange -> onBankNameChange(event.bankName)
            is VerifyBankAccountEvent.OnAccountTypeChange -> onAccountTypeChange(event.accountType)
            is VerifyBankAccountEvent.OnAccountHolderChange -> onAccountHolderChange(event.accountHolder)
            is VerifyBankAccountEvent.OnAccountNumberChange -> onAccountNumberChange(event.accountNumber)
            is VerifyBankAccountEvent.OnIfscCodeChange -> onIfscCodeChange(event.ifscCode)
            VerifyBankAccountEvent.OnProceedClick -> onProceedClick()
            VerifyBankAccountEvent.OnConfirmBankAccountClick -> onConfirmBankAccountClick()
            VerifyBankAccountEvent.OnChangeBankAccountClick -> onChangeBankAccountClick()
            VerifyBankAccountEvent.OnDismissConfirmBankAccountSheet -> setSheetVisible(false)
            VerifyBankAccountEvent.OnRetryLoadClick -> loadPrefill(prefillAfterPayment)
            VerifyBankAccountEvent.OnStartVerificationClick -> onStartVerificationClick()
            is VerifyBankAccountEvent.OnPaymentOptionSelected -> onPaymentOptionSelected(event.option)
            VerifyBankAccountEvent.OnPayClick -> onPayClick()
            VerifyBankAccountEvent.OnSelectAppBackClick -> onSelectAppBackClick()
            VerifyBankAccountEvent.OnIntroBackClick -> sendEffect(VerifyBankAccountEffect.NavigateBack)
            VerifyBankAccountEvent.OnFormBackClick -> _uiState.update {
                it.copy(step = BankVerificationStep.INTRO, showConfirmBankAccountSheet = false)
            }
            VerifyBankAccountEvent.OnCheckPaymentStatusClick -> awaitPaymentConfirmation()
            VerifyBankAccountEvent.OnPaymentPageReturned -> onPaymentPageReturned()
            is VerifyBankAccountEvent.OnUpiAppLinkCaught -> onUpiAppLinkCaught(event.url)
            VerifyBankAccountEvent.OnAppLinkOpening -> _uiState.update {
                it.copy(appLinkToOpen = null, awaitingAppReturn = true)
            }
            VerifyBankAccountEvent.OnAppLinkNotOpened -> _uiState.update {
                it.copy(awaitingAppReturn = false)
            }
            is VerifyBankAccountEvent.OnReturnedFromUpiApp -> onReturnedFromUpiApp(event.wasStopped)
            VerifyBankAccountEvent.SwitchToForm -> switchToForm()
        }
    }

    /**
     * Reads the bank details the server may already hold. Details on file go straight into the
     * form. None on file — or a server that answered with a failure — means the ₹1 verification
     * has to run first. Only a request that never reached the server stops here with a retry,
     * since the answer is simply not known yet.
     *
     * [afterPayment] marks the read that follows a confirmed payment: if the details are still
     * missing then, running the payment again would not help, so the form opens for manual entry.
     */
    private fun loadPrefill(afterPayment: Boolean) {
        prefillAfterPayment = afterPayment
        _uiState.update { it.copy(step = BankVerificationStep.LOADING, loadError = null) }

        viewModelScope.launch {
           setStep(BankVerificationStep.INTRO)
            when (val result = getPrefilledBankDetails()) {
                is NetworkResponse.Success -> {
                    val details = result.data
                    when {
                        details != null -> showPrefilledForm(details)
                        afterPayment -> showEmptyForm()
                        else -> setStep(BankVerificationStep.INTRO)
                    }
                }

                is NetworkResponse.Error -> when {
                    result.error.code == DEVICE_ERROR_CODE -> _uiState.update {
                        it.copy(
                            step = BankVerificationStep.LOAD_ERROR,
                            loadError = result.error.message
                        )
                    }

                    afterPayment -> showEmptyForm()
                    else -> setStep(BankVerificationStep.INTRO)
                }
            }
        }
    }

    private fun showPrefilledForm(details: PrefilledBankDetails) {
        _uiState.update {
            it.copy(
                step = BankVerificationStep.FORM,
                bankName = details.bankName.toCapital(),
                accountType = AccountType.entries.firstOrNull { type ->
                    type.id.equals(details.accountType, ignoreCase = true)
                },
                accountHolder = details.accountHolderName,
                accountNumber = details.accountNumber.filter { c -> c.isDigit() },
                ifscCode = details.ifscCode.toCapital()
            )
        }
    }

    private fun showEmptyForm(
    ) {
        setStep(BankVerificationStep.FORM)
        viewModelScope.launch { SnackBarController.showInfo(ENTER_MANUALLY_MESSAGE) }
    }

    private fun switchToForm(
    ) {
        setStep(BankVerificationStep.FORM)
    }

    /**
     * Raises the ₹1 verification. Links from an earlier, unfinished attempt are reused — going
     * back from the app picker should not cost the user a fresh payment link.
     */
    private fun onStartVerificationClick() {
        val state = _uiState.value
        if (state.isInitiating) return

        if (state.paymentLinks != null) {
            setStep(BankVerificationStep.SELECT_APP)
            return
        }

        _uiState.update { it.copy(isInitiating = true) }
        viewModelScope.launch {
            try {
                when (val result = initiateReversePennyDrop()) {
                    is NetworkResponse.Error -> SnackBarController.showError(result.error.message)
                    is NetworkResponse.Success -> {
                        val links = result.data
                        val firstApp = UpiApp.entries.firstOrNull { it in links.appLinks }
                        _uiState.update {
                            it.copy(
                                step = BankVerificationStep.SELECT_APP,
                                paymentLinks = links,
                                selectedPaymentOption = firstApp?.let(UpiPaymentOption::App)
                                    ?: UpiPaymentOption.ScanQr.takeIf { links.validationLink != null },
                                introNotice = null,
                                paymentNotice = null,
                                canCheckPaymentStatus = false
                            )
                        }
                    }
                }
            } finally {
                _uiState.update { it.copy(isInitiating = false) }
            }
        }
    }

    private fun onPaymentOptionSelected(option: UpiPaymentOption) {
        _uiState.update { it.copy(selectedPaymentOption = option) }
    }

    private fun onPayClick() {
        val state = _uiState.value
        // Already on its way to, or away in, a UPI app.
        if (state.appLinkToOpen != null || state.awaitingAppReturn) return
        val links = state.paymentLinks ?: return
        val url = when (val option = state.selectedPaymentOption) {
            is UpiPaymentOption.App -> links.appLinks[option.app]
            UpiPaymentOption.ScanQr -> links.validationLink
            null -> null
        }

        if (url == null) {
            viewModelScope.launch { SnackBarController.showError(PAYMENT_LINK_MISSING_MESSAGE) }
            return
        }

        awaitingPaymentReturn = true
        sendEffect(VerifyBankAccountEffect.OpenPaymentPage(url))
    }

    private fun onSelectAppBackClick() {
        _uiState.update {
            it.copy(
                step = BankVerificationStep.INTRO,
                paymentNotice = null,
                canCheckPaymentStatus = false
            )
        }
    }

    private fun onPaymentPageReturned() {
        if (!awaitingPaymentReturn) return
        awaitingPaymentReturn = false
        awaitPaymentConfirmation()
    }

    /**
     * The payment page only led to a UPI app; nothing is paid yet, so there is nothing to check.
     * The picker opens the app from here.
     */
    private fun onUpiAppLinkCaught(url: String) {
        awaitingPaymentReturn = false
        _uiState.update {
            it.copy(
                step = BankVerificationStep.SELECT_APP,
                appLinkToOpen = url,
                paymentNotice = null,
                canCheckPaymentStatus = false
            )
        }
    }

    /**
     * How the user left decides how hard to look for the payment. A full-screen app took over
     * ([wasStopped]) — they most likely paid, and the bank can take a few seconds, so the status
     * is polled. Otherwise only something small came up over this app: the app chooser they may
     * simply have dismissed, or an app that pays in a sheet. One read settles a payment made that
     * way; anything else leaves them on the picker straight away instead of waiting out a poll.
     */
    private fun onReturnedFromUpiApp(wasStopped: Boolean) {
        if (!_uiState.value.awaitingAppReturn) return
        _uiState.update { it.copy(awaitingAppReturn = false) }

        if (wasStopped) {
            awaitPaymentConfirmation()
        } else {
            awaitPaymentConfirmation(attempts = 1, pendingMessage = PAYMENT_NOT_FINISHED_MESSAGE)
        }
    }

    /**
     * The bank reports the ₹1 asynchronously, so coming back from the page proves nothing: the
     * status is read until it settles or the attempts run out.
     *
     * - Success → the server can now read the bank details, so the prefill runs again.
     * - Failed → this payment link is spent; the user starts the verification over.
     * - Still pending → back to the picker, where the user can check again or pay again, told
     *   [pendingMessage].
     */
    private fun awaitPaymentConfirmation(
        attempts: Int = PAYMENT_POLL_ATTEMPTS,
        pendingMessage: String = PAYMENT_PENDING_MESSAGE
    ) {
        if (_uiState.value.step == BankVerificationStep.VERIFYING) return
        setStep(BankVerificationStep.VERIFYING)

        viewModelScope.launch {
            repeat(attempts) { attempt ->
                // The first read happens immediately; the bank has often answered by then.
                if (attempt > 0) delay(PAYMENT_POLL_INTERVAL_MS.milliseconds)

                when (val result = getReversePennyDropStatus()) {
                    is NetworkResponse.Error -> {
                        showPaymentNotice(
                            message = "$STATUS_CHECK_FAILED_MESSAGE (${result.error.message})",
                            canCheckStatus = true
                        )
                        return@launch
                    }

                    is NetworkResponse.Success -> {
                        val status = result.data
                        when {
                            status.isSuccess -> {
                                loadPrefill(afterPayment = true)
                                return@launch
                            }

                            status.isFailed -> {
                                _uiState.update {
                                    it.copy(
                                        step = BankVerificationStep.INTRO,
                                        // A fresh link is needed for the next attempt.
                                        paymentLinks = null,
                                        selectedPaymentOption = null,
                                        introNotice = status.description
                                            ?.takeIf { d -> d.isNotBlank() }
                                            ?.let { d -> "$d. $PAYMENT_FAILED_MESSAGE" }
                                            ?: PAYMENT_FAILED_MESSAGE,
                                        paymentNotice = null,
                                        canCheckPaymentStatus = false
                                    )
                                }
                                return@launch
                            }
                        }
                    }
                }
            }

            showPaymentNotice(pendingMessage, canCheckStatus = true)
        }
    }

    private fun showPaymentNotice(message: String, canCheckStatus: Boolean) {
        _uiState.update {
            it.copy(
                step = BankVerificationStep.SELECT_APP,
                paymentNotice = message,
                canCheckPaymentStatus = canCheckStatus
            )
        }
    }

    private fun setStep(step: BankVerificationStep) {
        _uiState.update { it.copy(step = step) }
    }

    private fun onBankNameChange(bankName: String) {
        _uiState.update { it.copy(bankName = bankName.toCapital()) }
    }

    private fun onAccountTypeChange(accountType: AccountType) {
        _uiState.update { it.copy(accountType = accountType) }
    }

    private fun onAccountHolderChange(accountHolder: String) {
        _uiState.update { it.copy(accountHolder = accountHolder) }
    }

    private fun onAccountNumberChange(accountNumber: String) {
        if (!accountNumber.all { it.isDigit() }) return
        _uiState.update { it.copy(accountNumber = accountNumber) }
    }

    private fun onIfscCodeChange(ifscCode: String) {
        _uiState.update { it.copy(ifscCode = ifscCode.toCapital()) }
    }

    private fun onProceedClick() {
        val state = _uiState.value
        if (!state.canSubmit || state.isLoading) return

        _uiState.update {
            it.copy(
                bankAccountDetails = BankAccountDetails(
                    bankName = state.bankName,
                    accountHolder = state.accountHolder,
                    accountNumber = state.accountNumber,
                    ifscCode = state.ifscCode
                )
            )
        }
        setSheetVisible(true)
    }

    private fun onConfirmBankAccountClick() {
        if (_uiState.value.isLoading) return

        setSheetVisible(false)
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val state = _uiState.value
            val bankAccount = BankAccount(
                accountNumber = state.accountNumber,
                ifscCode = state.ifscCode,
                bankName = state.bankName,
                accountHolderName = state.accountHolder,
                accountType = state.accountType?.id ?: AccountType.SAVINGS.id
            )
            try {
                when (val result = submitPennyDrop(bankAccount)) {
                    is NetworkResponse.Error -> SnackBarController.showError(result.error.message)
                    is NetworkResponse.Success -> awaitPennyDropVerification(state.accountNumber)
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * The bank verifies the deposit out of band, so the submission succeeding only means the
     * request was accepted. This reads the status back until the account turns verified, and only
     * then lets the user move on — the screen stays in its loading state throughout.
     */
    private suspend fun awaitPennyDropVerification(accountNumber: String) {
        repeat(STATUS_POLL_ATTEMPTS) { attempt ->
            // The first read happens immediately; the bank has often answered by then.
            if (attempt > 0) delay(STATUS_POLL_INTERVAL_MS)

            when (val result = getPennyDropStatus(accountNumber)) {
                is NetworkResponse.Error -> {
                    SnackBarController.showError(result.error.message)
                    return
                }

                is NetworkResponse.Success -> {
                    val status = result.data
                    when {
                        status.isVerified -> {
                            sendEffect(VerifyBankAccountEffect.PennyDropCompleted)
                            return
                        }

                        status.isFailed -> {
                            SnackBarController.showError(
                                status.reason ?: VERIFICATION_FAILED_MESSAGE
                            )
                            return
                        }
                    }
                }
            }
        }

        SnackBarController.showError(VERIFICATION_PENDING_MESSAGE)
    }

    private fun onChangeBankAccountClick() {
        setSheetVisible(false)
    }

    private fun setSheetVisible(visible: Boolean) {
        _uiState.update { it.copy(showConfirmBankAccountSheet = visible) }
    }

    private fun sendEffect(effect: VerifyBankAccountEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    private companion object {
        /** `safeRequest` reports a request that never got a response with this code. */
        const val DEVICE_ERROR_CODE = -1

        /** Five reads with 4-second gaps — about 16 seconds for the ₹1 to be confirmed. */
        const val PAYMENT_POLL_ATTEMPTS = 5
        const val PAYMENT_POLL_INTERVAL_MS = 4_000L

        const val PAYMENT_NOT_FINISHED_MESSAGE =
            "Didn't finish paying? Choose an app to pay the ₹1. Already paid? Tap Check Status."

        const val PAYMENT_PENDING_MESSAGE =
            "We have not received your ₹1 payment yet. If you have paid, tap Check Status. " +
                "Otherwise, choose an app and pay again."

        const val STATUS_CHECK_FAILED_MESSAGE = "We could not check your payment status"

        const val PAYMENT_FAILED_MESSAGE =
            "Your bank verification did not go through. Please start the verification again."

        const val PAYMENT_LINK_MISSING_MESSAGE =
            "This payment option is not available right now. Please choose another."

        const val ENTER_MANUALLY_MESSAGE =
            "We could not fetch your bank details. Please enter them below."

        /** Five reads with 5-second gaps — about 20 seconds for the bank to answer. */
        const val STATUS_POLL_ATTEMPTS = 5
        const val STATUS_POLL_INTERVAL_MS = 5_000L

        const val VERIFICATION_FAILED_MESSAGE =
            "We could not verify this bank account. Please check the details and try again."

        const val VERIFICATION_PENDING_MESSAGE =
            "Your bank account is still being verified. Please try again in a moment."
    }
}
