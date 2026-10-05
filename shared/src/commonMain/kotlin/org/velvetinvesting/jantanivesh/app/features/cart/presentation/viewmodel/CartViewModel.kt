package org.velvetinvesting.jantanivesh.app.features.cart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.core.utils.WebURLConstants
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.CartType
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.CartCheckoutDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.UserCartDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.CheckoutCartLumpsumUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.CheckoutCartSipUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.ClearCartUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.ConfirmCartLumpsumCheckoutUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.DeleteCartItemUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.GetCartPaymentStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.GetUserCartUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.VerifyCartSipCheckoutOtpUseCase
import org.velvetinvesting.jantanivesh.app.features.core.domain.usecase.GetUserDataUseCase
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpController
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.FundTypeSelector
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.SelectedFundType
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.ConfirmMandateUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.CreateMandateUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.OTP_LENGTH
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.OTP_RESEND_SECONDS
import kotlin.time.Duration.Companion.milliseconds

data class CartUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val cart: UserCartDomain? = null,
    val selectedCartType: CartType = CartType.LUMPSUM,
    /** A purchase step is in flight: the mandate, a checkout, or polling the mandate or payment. */
    val isProcessing: Boolean = false,
    val showCutOffPopup: Boolean = false,
    /** The user tried to pay without a verified KYC; the popup sends them to finish it. */
    val showKycPopup: Boolean = false,
    /** The SIP or lumpsum checkout whose OTP the confirm screen is waiting on. */
    val checkout: CartCheckoutDomain? = null
) {
    val totalAmount: Long
        get() {
            val cart = cart ?: return 0L
            return when (selectedCartType) {
                // Step-up is not offered for now: `item.sipDetails.sipAmount + item.stepUpAmount`
                CartType.SIP -> cart.sipItems.sumOf { it.sipDetails.sipAmount }
                CartType.LUMPSUM -> cart.lumpSumItems.sumOf { it.amount }
            }
        }

    val isPurchaseEnabled: Boolean
        get() {
            val cart = cart ?: return false
            return when (selectedCartType) {
                CartType.LUMPSUM -> cart.lumpSumItems.isNotEmpty()
                CartType.SIP -> cart.sipItems.isNotEmpty()
                // Step-up is not offered for now.
                // && cart.sipItems.none { sip ->
                //     sip.stepUpRequired && sip.stepUpAmount < sip.minStepUpAmount
                // }
            }
        }
}

sealed interface CartEvent {
    data object Retry : CartEvent
    data object Refresh : CartEvent
    /** The screen came back into view, so a cart changed elsewhere is picked up quietly. */
    data object OnScreenResumed : CartEvent
    data object OnBackClicked : CartEvent

    data class OnCartTypeSelected(val type: CartType) : CartEvent
    data class OnRemoveItemClicked(val itemId: String) : CartEvent
    data object OnClearCartClicked : CartEvent

    data object OnPayClicked : CartEvent
    data object OnCutOffPopupDismissed : CartEvent
    data object OnPurchaseConfirmed : CartEvent

    data object OnKycPopupDismissed : CartEvent
    data object OnCompleteKycClicked : CartEvent

    /** The mandate or payment webview closed. */
    data object OnWebViewReturned : CartEvent

    /** Checkout OTP screen. */
    data class OnOtpChanged(val otp: String) : CartEvent
    data object OnOtpSubmitClicked : CartEvent
    data object OnOtpResendClicked : CartEvent
    data object OnOtpBackClicked : CartEvent

    // Step-up is not offered for now.
    // data class OnStepUpEnabled(val item: SipItemDomain) : CartEvent
    // data class OnStepUpDisabled(val item: SipItemDomain) : CartEvent
    // data class OnStepUpAmountChanged(val item: SipItemDomain, val amount: String) : CartEvent
}

sealed interface CartEffect {
    data object NavigateBack : CartEffect

    /** The bank's page for approving the SIP autopay mandate, which exits on the mandate URL. */
    data class OpenMandateAuthorization(val url: String, val exitUrl: String) : CartEffect

    /** A checkout is placed and its OTP is out. */
    data object NavigateToCheckoutOtp : CartEffect

    /** Onboarding is not complete, so the user is sent back into it at [stage] (`current_stage`). */
    data class NavigateToKyc(val stage: String) : CartEffect

    /** The lumpsum payment was confirmed; the orders it placed are shown over the cart. */
    data object NavigateToOrders : CartEffect
}

/**
 * Kept apart from [CartEffect]: the cart and OTP screens both collect while a transition runs, and
 * one channel would hand each effect to whichever collector reached it first.
 */
sealed interface CartCheckoutOtpEffect {
    /** Back to the cart, whether the purchase went through or the user backed out. */
    data object Close : CartCheckoutOtpEffect

    /**
     * A lumpsum checkout is authorised and waits on payment. The page replaces the OTP screen, so
     * closing it lands back on the cart, which then reads the payment back.
     */
    data class OpenPayment(val url: String, val exitUrl: String) : CartCheckoutOtpEffect

    /**
     * The SIP checkout is confirmed. The orders replace the OTP screen, so back from them lands on
     * the cart.
     */
    data object OpenOrders : CartCheckoutOtpEffect
}

class CartViewModel(
    private val getUserCartUseCase: GetUserCartUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val createMandateUseCase: CreateMandateUseCase,
    private val confirmMandateUseCase: ConfirmMandateUseCase,
    private val checkoutCartSipUseCase: CheckoutCartSipUseCase,
    private val verifyCartSipCheckoutOtpUseCase: VerifyCartSipCheckoutOtpUseCase,
    private val checkoutCartLumpsumUseCase: CheckoutCartLumpsumUseCase,
    private val confirmCartLumpsumCheckoutUseCase: ConfirmCartLumpsumCheckoutUseCase,
    private val getCartPaymentStatusUseCase: GetCartPaymentStatusUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CartUiState(
            selectedCartType = when (FundTypeSelector.fundType.value) {
                SelectedFundType.SIP -> CartType.SIP
                SelectedFundType.LUMSUM -> CartType.LUMPSUM
            }
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<CartEffect>()
    val effect = _effect.receiveAsFlow()

    private val _checkoutOtpEffect = Channel<CartCheckoutOtpEffect>()
    val checkoutOtpEffect = _checkoutOtpEffect.receiveAsFlow()

    /** The OTP is sent by the checkout call, so the cooldown starts when that lands. */
    val otp = OtpController(
        scope = viewModelScope,
        otpLength = OTP_LENGTH,
        resendCooldownSeconds = OTP_RESEND_SECONDS,
        startTimerImmediately = false
    )

    /** Follow-up queued when the payment webview is launched, run when it returns. */
    private var onWebViewReturn: (() -> Unit)? = null

    /**
     * The mandate record id the SIP checkout was placed on, kept so a resend can place it again
     * without a new mandate.
     */
    private var checkoutMandateId: String? = null

    init {
        loadCart()
    }

    fun handleEvent(event: CartEvent) {
        when (event) {
            CartEvent.Retry, CartEvent.Refresh -> loadCart()

            CartEvent.OnScreenResumed -> if (_uiState.value.cart != null) reloadCart()

            CartEvent.OnBackClicked -> sendEffect(CartEffect.NavigateBack)

            is CartEvent.OnCartTypeSelected -> _uiState.update { it.copy(selectedCartType = event.type) }

            is CartEvent.OnRemoveItemClicked -> removeItem(event.itemId)

            CartEvent.OnClearCartClicked -> clearCart()

            CartEvent.OnPayClicked -> {
                if (_uiState.value.isProcessing) return
                purchase()
            }

            CartEvent.OnCutOffPopupDismissed -> _uiState.update { it.copy(showCutOffPopup = false) }

            CartEvent.OnPurchaseConfirmed -> purchase()

            CartEvent.OnKycPopupDismissed -> _uiState.update { it.copy(showKycPopup = false) }

            CartEvent.OnCompleteKycClicked -> {
                _uiState.update { it.copy(showKycPopup = false) }
                sendEffect(CartEffect.NavigateToKyc(kycStage))
            }

            CartEvent.OnWebViewReturned -> {
                val action = onWebViewReturn
                onWebViewReturn = null
                action?.invoke()
            }

            is CartEvent.OnOtpChanged -> otp.onOtpChange(event.otp)

            CartEvent.OnOtpSubmitClicked -> when (_uiState.value.checkout?.type) {
                CartType.SIP -> verifySipOtp()
                CartType.LUMPSUM -> confirmLumpsumOtp()
                null -> Unit
            }

            CartEvent.OnOtpResendClicked -> resendOtp()

            CartEvent.OnOtpBackClicked -> {
                otp.clearOtp()
                viewModelScope.launch { _checkoutOtpEffect.send(CartCheckoutOtpEffect.Close) }
            }

            // Step-up is not offered for now.
            // is CartEvent.OnStepUpEnabled -> updateSipItem(event.item.id) {
            //     it.copy(stepUpRequired = true, stepUpAmount = it.minStepUpAmount)
            // }
            // is CartEvent.OnStepUpDisabled -> updateSipItem(event.item.id) {
            //     it.copy(stepUpRequired = false, stepUpAmount = 0)
            // }
            // is CartEvent.OnStepUpAmountChanged -> updateSipItem(event.item.id) {
            //     it.copy(stepUpAmount = event.amount.toLongOrNull() ?: 0L)
            // }
        }
    }

    /** Loads the cart behind a full-screen loader; a failure replaces it with the error screen. */
    private fun loadCart() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val response = getUserCartUseCase()) {
                is NetworkResponse.Success -> _uiState.update {
                    it.copy(isLoading = false, cart = response.data)
                }

                is NetworkResponse.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, cart = null, error = response.error.message)
                    }
                    SnackBarController.showError(response.error.message)
                }
            }
        }
    }

    /** Refreshes the cart in place, keeping what is on screen if it fails. */
    private fun reloadCart() {
        viewModelScope.launch {
            when (val response = getUserCartUseCase()) {
                is NetworkResponse.Success -> _uiState.update { it.copy(cart = response.data) }
                is NetworkResponse.Error -> SnackBarController.showError(response.error.message)
            }
        }
    }

    private fun reloadCartAndRefreshPortfolio() {
        reloadCart()
        viewModelScope.launch {
            AppEventsController.sendPortfolioRefreshEvent()
        }
    }

    private fun removeItem(itemId: String) {
        if (_uiState.value.cart == null) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val response = deleteCartItemUseCase(itemId)) {
                is NetworkResponse.Success -> loadCart()
                is NetworkResponse.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    SnackBarController.showError(response.error.message)
                }
            }
        }
    }

    private fun clearCart() {
        if (_uiState.value.cart == null) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val response = clearCartUseCase()) {
                is NetworkResponse.Success -> loadCart()
                is NetworkResponse.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    SnackBarController.showError(response.error.message)
                }
            }
        }
    }

    /**
     * KYC is read fresh on every attempt rather than cached, so a user who finishes it elsewhere is
     * not held back by a stale answer; an unverified user gets the popup instead of a purchase.
     */
    /** The onboarding `current_stage` from the last failed check, so the popup resumes there. */
    private var kycStage = ""

    private fun purchase() {
        val state = _uiState.value
        if (state.cart == null || state.isProcessing) return
        _uiState.update { it.copy(showCutOffPopup = false, isProcessing = true) }

        viewModelScope.launch {
            when (val result = getUserDataUseCase()) {
                is NetworkResponse.Error -> {
                    failPurchase(result.error.message)
                    return@launch
                }

                // Onboarding's is_completed is what clears a user to invest, not kyc_status alone.
                is NetworkResponse.Success -> if (!result.data.onboarding.isCompleted) {
                    kycStage = result.data.onboarding.currentStage
                    _uiState.update { it.copy(isProcessing = false, showKycPopup = true) }
                    return@launch
                }
            }

            _uiState.update { it.copy(isProcessing = false) }
            when (state.selectedCartType) {
                CartType.SIP -> startSipMandate()
                CartType.LUMPSUM -> checkoutLumpsum()
            }
        }
    }

    /**
     * A SIP cannot run without a mandate to debit, so the purchase starts by creating one for the
     * cart's SIP total. The rest of the purchase resumes in [awaitMandateAndCheckout] once the user
     * is back from the bank's approval page.
     */
    private fun startSipMandate() {
        val sipTotal = _uiState.value.cart?.sipItems?.sumOf { it.sipDetails.sipAmount } ?: return
        if (sipTotal <= 0) return

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }

            val result = createMandateUseCase(
                mandateLimit = sipTotal,
                validFrom = DateTimeUtils.today().toString(),
                paymentPostbackUrl = WebURLConstants.mandateExitUrl
            )

            when (result) {
                is NetworkResponse.Error -> failPurchase(result.error.message)

                is NetworkResponse.Success -> {
                    val mandate = result.data
                    val mandateId = mandate.id
                    val recordId = mandate.recordId
                    val tokenUrl = mandate.tokenUrl

                    if (mandateId == null || recordId.isNullOrBlank() || tokenUrl.isNullOrBlank()) {
                        failPurchase(MANDATE_FAILED_MESSAGE)
                        return@launch
                    }

                    _uiState.update { it.copy(isProcessing = false) }
                    onWebViewReturn = { awaitMandateAndCheckout(mandateId, recordId) }
                    _effect.send(
                        CartEffect.OpenMandateAuthorization(
                            url = tokenUrl,
                            exitUrl = WebURLConstants.mandateExitUrl
                        )
                    )
                }
            }
        }
    }

    /**
     * Coming back from the bank's page proves nothing, so the mandate is read back until it reports
     * approval, and only then is the cart checked out on it — which sends the OTP.
     */
    private fun awaitMandateAndCheckout(mandateId: Int, mandateRecordId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }

            if (!awaitMandateApproval(mandateId)) return@launch

            checkoutMandateId = mandateRecordId
            when (val result = checkoutCartSipUseCase(mandateRecordId)) {
                is NetworkResponse.Error -> failPurchase(result.error.message)

                is NetworkResponse.Success -> openCheckoutOtp(result.data)
            }
        }
    }

    /**
     * Polls up to [MANDATE_POLL_ATTEMPTS] times, [POLL_INTERVAL_MS] apart. False means the purchase
     * is over: the read failed, or the bank had not approved the mandate in time.
     */
    private suspend fun awaitMandateApproval(mandateId: Int): Boolean {
        repeat(MANDATE_POLL_ATTEMPTS) { attempt ->
            // The first read happens immediately; the bank has often answered by then.
            if (attempt > 0) delay(POLL_INTERVAL_MS.milliseconds)

            when (val result = confirmMandateUseCase(mandateId)) {
                is NetworkResponse.Error -> {
                    failPurchase(MANDATE_FAILED_MESSAGE)
                    return false
                }

                is NetworkResponse.Success -> if (result.data.isApproved) return true
            }
        }

        _uiState.update { it.copy(isProcessing = false) }
        SnackBarController.showWarning(MANDATE_PENDING_MESSAGE)
        return false
    }

    /** The OTP screen stays put on a bad code so the user can correct it without starting over. */
    private fun verifySipOtp() {
        val otpState = otp.state.value
        if (!otpState.isSubmitEnabled) return
        val batchId = _uiState.value.checkout?.batchId ?: return

        viewModelScope.launch {
            val result = otp.withLoading {
                verifyCartSipCheckoutOtpUseCase(batchId = batchId, otp = otpState.otpValue)
            }

            when (result) {
                is NetworkResponse.Error -> {
                    otp.clearOtp()
                    SnackBarController.showError(result.error.message)
                }

                is NetworkResponse.Success -> {
                    checkoutMandateId = null
                    otp.clearOtp()
                    _uiState.update { it.copy(checkout = null) }
                    _checkoutOtpEffect.send(CartCheckoutOtpEffect.OpenOrders)
                    SnackBarController.showSuccess("Your SIPs have been placed successfully")
                    reloadCartAndRefreshPortfolio()
                }
            }
        }
    }

    /**
     * The checkout call is what sends the OTP, so a resend places the checkout again — a SIP on
     * the same approved mandate — and confirms against the batch it returns.
     */
    private fun resendOtp() {
        if (!otp.state.value.canResend) return
        val checkoutAgain: suspend () -> NetworkResponse<CartCheckoutDomain, ErrorDomain> =
            when (_uiState.value.checkout?.type ?: return) {
                CartType.SIP -> {
                    val mandateId = checkoutMandateId ?: return
                    suspend { checkoutCartSipUseCase(mandateId) }
                }

                CartType.LUMPSUM -> suspend { checkoutCartLumpsumUseCase() }
            }

        viewModelScope.launch {
            val result = otp.withLoading { checkoutAgain() }

            when (result) {
                is NetworkResponse.Error -> SnackBarController.showError(result.error.message)

                is NetworkResponse.Success -> {
                    _uiState.update { it.copy(checkout = result.data) }
                    otp.clearOtp()
                    otp.startResendTimer()
                }
            }
        }
    }

    /** Batches the cart's lumpsum orders, which sends the OTP that authorises them. */
    private fun checkoutLumpsum() {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }

            when (val result = checkoutCartLumpsumUseCase()) {
                is NetworkResponse.Error -> failPurchase(result.error.message)
                is NetworkResponse.Success -> openCheckoutOtp(result.data)
            }
        }
    }

    private suspend fun openCheckoutOtp(checkout: CartCheckoutDomain) {
        _uiState.update { it.copy(isProcessing = false, checkout = checkout) }
        otp.clearOtp()
        otp.startResendTimer()
        _effect.send(CartEffect.NavigateToCheckoutOtp)
    }

    /**
     * The OTP only authorises the lumpsum; the money moves on the payment page. The OTP screen is
     * replaced by that page, and [awaitPayment] reads the payment back once the user returns.
     */
    private fun confirmLumpsumOtp() {
        val otpState = otp.state.value
        if (!otpState.isSubmitEnabled) return
        val batchId = _uiState.value.checkout?.batchId ?: return

        viewModelScope.launch {
            val result = otp.withLoading {
                confirmCartLumpsumCheckoutUseCase(
                    batchId = batchId,
                    otp = otpState.otpValue,
                    paymentPostbackUrl = WebURLConstants.paymentResultUrl
                )
            }

            when (result) {
                is NetworkResponse.Error -> {
                    otp.clearOtp()
                    SnackBarController.showError(result.error.message)
                }

                is NetworkResponse.Success -> {
                    val payment = result.data
                    otp.clearOtp()
                    _uiState.update { it.copy(checkout = null) }
                    onWebViewReturn = { awaitPayment(payment.paymentId) }
                    _checkoutOtpEffect.send(
                        CartCheckoutOtpEffect.OpenPayment(
                            url = payment.paymentUrl,
                            exitUrl = WebURLConstants.paymentResultUrl
                        )
                    )
                }
            }
        }
    }

    /**
     * Coming back from the payment page proves nothing — the user may have closed it — so the
     * payment is read back up to [PAYMENT_POLL_ATTEMPTS] times. `SUCCESS` and `FAILED` end the
     * poll at once; anything else is still in flight and spends the next attempt.
     */
    private fun awaitPayment(paymentId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }

            repeat(PAYMENT_POLL_ATTEMPTS) { attempt ->
                // The first read happens immediately; the payment has often settled by then.
                if (attempt > 0) delay(POLL_INTERVAL_MS.milliseconds)

                when (val result = getCartPaymentStatusUseCase(paymentId)) {
                    is NetworkResponse.Error -> {
                        failPurchase(result.error.message)
                        reloadCart()
                        return@launch
                    }

                    is NetworkResponse.Success -> when {
                        result.data.isSuccessful -> {
                            _uiState.update { it.copy(isProcessing = false) }
                            SnackBarController.showSuccess("Your purchase was successful")
                            reloadCartAndRefreshPortfolio()
                            _effect.send(CartEffect.NavigateToOrders)
                            return@launch
                        }

                        result.data.hasFailed -> {
                            failPurchase(result.data.failedReason ?: PAYMENT_FAILED_MESSAGE)
                            reloadCart()
                            return@launch
                        }

                        else -> Unit
                    }
                }
            }

            // Still unsettled after the poll window. The payment stays live on the server, so this
            // is reported as pending rather than as a failure.
            _uiState.update { it.copy(isProcessing = false) }
            SnackBarController.showWarning(PAYMENT_PENDING_MESSAGE)
            reloadCartAndRefreshPortfolio()
        }
    }

    private suspend fun failPurchase(message: String) {
        _uiState.update { it.copy(isProcessing = false) }
        SnackBarController.showError(message)
    }

    // Step-up is not offered for now.
    // private fun updateSipItem(itemId: String, transform: (SipItemDomain) -> SipItemDomain) {
    //     _uiState.update { state ->
    //         val cart = state.cart ?: return@update state
    //         state.copy(
    //             cart = cart.copy(
    //                 sipItems = cart.sipItems.map { if (it.id == itemId) transform(it) else it }
    //             )
    //         )
    //     }
    // }

    private fun sendEffect(effect: CartEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    private companion object {
        /** Five reads gives four 5-second gaps — about 20 seconds for the bank to answer. */
        const val MANDATE_POLL_ATTEMPTS = 5
        const val POLL_INTERVAL_MS = 5_000L

        /** The same window for the payment: about 20 seconds for it to settle. */
        const val PAYMENT_POLL_ATTEMPTS = 5

        const val MANDATE_FAILED_MESSAGE = "Autopay setup failed. Please try again."
        const val PAYMENT_FAILED_MESSAGE = "The payment did not go through. Please try again."
        const val PAYMENT_PENDING_MESSAGE =
            "We have not received your payment yet. It will show in your orders once it clears."
        const val MANDATE_PENDING_MESSAGE =
            "Your bank has not approved the autopay mandate yet. Please try again in a few minutes."
    }
}
