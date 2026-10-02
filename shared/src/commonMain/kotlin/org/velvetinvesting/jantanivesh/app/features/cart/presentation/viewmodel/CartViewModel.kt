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
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.CartType
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.MandateStatus
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.SIPStatus
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.UserCartDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.CheckSipPurchaseStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.ClearCartUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.DeleteCartItemUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.GetUserCartUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.InitiateSipPurchaseUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.PurchaseLumpsumFundUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.PurchaseSipFundUseCase
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.FundTypeSelector
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.SelectedFundType

data class CartUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val cart: UserCartDomain? = null,
    val selectedCartType: CartType = CartType.LUMPSUM,
    /** A purchase step is in flight: initiating, polling the mandate or fetching a payment link. */
    val isProcessing: Boolean = false,
    val showCutOffPopup: Boolean = false
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

    /** The payment webview closed. */
    data object OnWebViewReturned : CartEvent

    // Step-up is not offered for now.
    // data class OnStepUpEnabled(val item: SipItemDomain) : CartEvent
    // data class OnStepUpDisabled(val item: SipItemDomain) : CartEvent
    // data class OnStepUpAmountChanged(val item: SipItemDomain, val amount: String) : CartEvent
}

sealed interface CartEffect {
    data object NavigateBack : CartEffect
    /** A mandate approval or payment page, opened in the in-app webview. */
    data class OpenWebView(val url: String) : CartEffect
}

class CartViewModel(
    private val getUserCartUseCase: GetUserCartUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val initiateSipPurchaseUseCase: InitiateSipPurchaseUseCase,
    private val checkSipPurchaseStatusUseCase: CheckSipPurchaseStatusUseCase,
    private val purchaseSipUseCase: PurchaseSipFundUseCase,
    private val purchaseLumpSumUseCase: PurchaseLumpsumFundUseCase,
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

    /** Follow-up queued when the payment webview is launched, run when it returns. */
    private var onWebViewReturn: (() -> Unit)? = null

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

            CartEvent.OnPayClicked -> _uiState.update { it.copy(showCutOffPopup = true) }

            CartEvent.OnCutOffPopupDismissed -> _uiState.update { it.copy(showCutOffPopup = false) }

            CartEvent.OnPurchaseConfirmed -> purchase()

            CartEvent.OnWebViewReturned -> {
                val action = onWebViewReturn
                onWebViewReturn = null
                action?.invoke()
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

    private fun purchase() {
        val state = _uiState.value
        if (state.cart == null) return
        _uiState.update { it.copy(showCutOffPopup = false) }
        when (state.selectedCartType) {
            CartType.SIP -> initiateSip()
            CartType.LUMPSUM -> purchaseLumpSum()
        }
    }

    private fun initiateSip() {
        val sipItems = _uiState.value.cart?.sipItems ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            when (val response = initiateSipPurchaseUseCase(sipItems)) {
                is NetworkResponse.Success -> {
                    val mandate = response.data
                    when (mandate.status) {
                        MandateStatus.PENDING -> {
                            _uiState.update { it.copy(isProcessing = false) }
                            onWebViewReturn = { checkPurchaseStatus(mandate.mandateId) }
                            _effect.send(CartEffect.OpenWebView(mandate.url))
                        }

                        MandateStatus.APPROVED -> purchaseSip(mandate.mandateId)
                    }
                }

                is NetworkResponse.Error -> {
                    _uiState.update { it.copy(isProcessing = false) }
                    SnackBarController.showError(response.error.message)
                }
            }
        }
    }

    private fun checkPurchaseStatus(mandateId: String, retryCount: Int = 0) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }

            when (val response = checkSipPurchaseStatusUseCase(mandateId)) {
                is NetworkResponse.Success -> when (response.data) {
                    SIPStatus.SUCCESS -> purchaseSip(mandateId)

                    SIPStatus.PENDING, SIPStatus.REQUESTED -> {
                        if (retryCount < MAX_MANDATE_STATUS_RETRIES) {
                            delay(MANDATE_STATUS_RETRY_DELAY_MS)
                            checkPurchaseStatus(mandateId = mandateId, retryCount = retryCount + 1)
                        } else {
                            _uiState.update { it.copy(isProcessing = false) }
                            SnackBarController.showWarning("Purchase status is still pending. Retry Again.")
                        }
                    }
                }

                is NetworkResponse.Error -> {
                    _uiState.update { it.copy(isProcessing = false) }
                    SnackBarController.showError(response.error.message)
                }
            }
        }
    }

    private fun purchaseSip(mandateId: String) {
        val sipItems = _uiState.value.cart?.sipItems ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            val response = purchaseSipUseCase(mandateId = mandateId, sipItems = sipItems)
            _uiState.update { it.copy(isProcessing = false) }
            when (response) {
                is NetworkResponse.Success -> {
                    onWebViewReturn = { reloadCartAndRefreshPortfolio() }
                    _effect.send(CartEffect.OpenWebView(response.data))
                }

                is NetworkResponse.Error -> SnackBarController.showError(response.error.message)
            }
        }
    }

    private fun purchaseLumpSum() {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            val response = purchaseLumpSumUseCase()
            _uiState.update { it.copy(isProcessing = false) }
            when (response) {
                is NetworkResponse.Success -> {
                    onWebViewReturn = { reloadCartAndRefreshPortfolio() }
                    _effect.send(CartEffect.OpenWebView(response.data))
                }

                is NetworkResponse.Error -> SnackBarController.showError(response.error.message)
            }
        }
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
        const val MAX_MANDATE_STATUS_RETRIES = 2
        const val MANDATE_STATUS_RETRY_DELAY_MS = 5000L
    }
}
