package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SIPStatus
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CartType
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MandateStatus
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SipItemDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.UserCartDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.CheckSipPurchaseStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.ClearCartUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.DeleteCartItemUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetUserCartUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.InitiateSipPurchaseUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.PurchaseLumpsumFundUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.PurchaseSipFundUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.FundTypeSelector
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.SelectedFundType

data class CartUiModel(
    val cartData: UserCartDomain,
    val selectedCartType: CartType = CartType.LUMPSUM
)

sealed interface CartSideEffects{
    data class OpenForInitiation(val url: String, val mandateId:String): CartSideEffects
    data class OpenForPurchase(val url: String): CartSideEffects

    data class OpenForLumpSumPurchase(val url: String): CartSideEffects
}

class CartScreenViewModel(
    private val getUserCartUseCase: GetUserCartUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val initiateSipPurchaseUseCase: InitiateSipPurchaseUseCase,
    private val checkSipPurchaseStatusUseCase: CheckSipPurchaseStatusUseCase,
    private val purchaseSipUseCase: PurchaseSipFundUseCase,
    private val purchaseLumpSumUseCase: PurchaseLumpsumFundUseCase,
) : ViewModel() {

    private var currentCartType = when (FundTypeSelector.fundType.value) {
        SelectedFundType.SIP -> CartType.SIP
        SelectedFundType.LUMSUM -> CartType.LUMPSUM
    }

    private val _cartSideEffect = MutableSharedFlow<CartSideEffects>()
    val cartSideEffect = _cartSideEffect.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState<CartUiModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<CartUiModel>> = _uiState.asStateFlow()

    private val _loading= MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    val isPurchaseEnabled: StateFlow<Boolean> =
        uiState
            .map { state ->
                if (state !is UiState.Success) {
                    return@map false
                }
                val data = state.data
                when (data.selectedCartType) {
                    CartType.LUMPSUM -> {
                        data.cartData.lumpSumItems.isNotEmpty()
                    }
                    CartType.SIP -> {
                        data.cartData.sipItems.isNotEmpty() &&
                                data.cartData.sipItems.none { sip ->
                                    sip.stepUpRequired &&
                                            sip.stepUpAmount < sip.minStepUpAmount
                                }
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false
            )

    private val _confirmationPopupVisible= MutableStateFlow(false)
    val confirmationPopupVisible: StateFlow<Boolean> = _confirmationPopupVisible.asStateFlow()

    val totalAmount = uiState.map {
        if (it is UiState.Success){
            val sipTotal = it.data.cartData.sipItems.sumOf {item-> item.sipDetails.sipAmount + item.stepUpAmount }
            val lumpSumTotal = it.data.cartData.lumpSumItems.sumOf { item-> item.amount }
            when(it.data.selectedCartType){
                CartType.SIP -> sipTotal
                CartType.LUMPSUM -> lumpSumTotal
            }
        }
        else 0L
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0L
        )


    init {
        loadCart()
    }


    fun onCartTypeSelected(type: CartType) {
        currentCartType = type
        val current = _uiState.value
        if (current is UiState.Success) {
            _uiState.value = UiState.Success(
                current.data.copy(selectedCartType = type)
            )
        }
    }

    fun removeItem(itemId: String) {
        val currentState = uiState.value
        if (currentState is UiState.Success){
            val data = currentState.data
            viewModelScope.launch {
                _loading.value = true
                _uiState.value = UiState.Loading
                deleteCartItemUseCase(itemId)
                    .onSuccess {
                        _loading.value = false
                        loadCart()
                    }
                    .onError {
                        _loading.value = false
                        _uiState.value = UiState.Success(data)
                        SnackBarController.showError(it.message)
                    }
            }
        }
    }

    fun clearCart() {
        val currentState = uiState.value
        if (currentState is UiState.Success) {
            val data = currentState.data
            viewModelScope.launch {
                _loading.value = true
                _uiState.value = UiState.Loading
                clearCartUseCase()
                    .onSuccess {
                        _loading.value = false
                        loadCart()
                    }
                    .onError {
                        _loading.value = false
                        _uiState.value = UiState.Success(data)
                        SnackBarController.showError(it.message)
                    }
            }
        }
    }

    fun loadCart() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            getUserCartUseCase()
                .onSuccess {
                    _uiState.value=UiState.Success(
                        CartUiModel(
                            cartData = it,
                            selectedCartType = currentCartType
                        )
                    )
                }
                .onError {
                    SnackBarController.showError(it.message)
                    _uiState.value=UiState.Error(it.message)
                }
        }
    }

    fun reloadFund(){
        viewModelScope.launch {
            getUserCartUseCase()
                .onSuccess {
                    _uiState.value=UiState.Success(
                        CartUiModel(
                            cartData = it,
                            selectedCartType = currentCartType
                        )
                    )
                }
                .onError {
                    SnackBarController.showError(it.message)
                }
        }
    }

    fun purchase(){
        if (_uiState.value is UiState.Success){
            val data= (_uiState.value as UiState.Success).data
            hidePopup()
            if (data.selectedCartType == CartType.SIP){
                initiateSip()
            }else {
                purchaseLumpSum()
            }
        }
    }

    fun initiateSip() {
        val sipData = if (_uiState.value is UiState.Success){
            (_uiState.value as UiState.Success).data.cartData.sipItems
        }else return

        viewModelScope.launch {
            _loading.value = true
            initiateSipPurchaseUseCase(sipData)
                .onSuccess {
                    _loading.value = false

                    when(it.status){
                        MandateStatus.PENDING -> {
                            _cartSideEffect.emit(
                                CartSideEffects.OpenForInitiation(
                                    url = it.url,
                                    mandateId = it.mandateId
                                )
                            )
                        }
                        MandateStatus.APPROVED -> {
                            purchaseSip(it.mandateId)
                        }
                    }


                }
                .onError {
                    _loading.value = false
                    SnackBarController.showError(it.message)
                }
        }
    }

    fun checkPurchaseStatus(
        mandateId: String,
        retryCount: Int = 0
    ) {
        viewModelScope.launch {
            _loading.value = true

            checkSipPurchaseStatusUseCase(mandateId)
                .onSuccess { status ->
                    when (status) {
                        SIPStatus.SUCCESS -> {
                            _loading.value = false
                            purchaseSip(mandateId)
                        }

                        SIPStatus.PENDING, SIPStatus.REQUESTED -> {
                            if (retryCount < 2) {
                                delay(5000)
                                checkPurchaseStatus(
                                    mandateId = mandateId,
                                    retryCount = retryCount + 1
                                )
                            } else {
                                _loading.value = false
                                SnackBarController.showWarning(
                                    "Purchase status is still pending. Retry Again."
                                )
                            }
                        }
                    }
                }
                .onError {
                    _loading.value = false
                    SnackBarController.showError(it.message)
                }
        }
    }
    fun purchaseSip(mandateId: String) {
        val current = _uiState.value
        if (current is UiState.Success){
            val data = current.data
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                purchaseSipUseCase(mandateId=mandateId, sipItems = data.cartData.sipItems)
                    .onSuccess {
                        _uiState.value = UiState.Success(data)
                        _cartSideEffect.emit(
                            CartSideEffects.OpenForPurchase(
                                url = it
                            )
                        )
                    }
                    .onError {
                        _uiState.value = UiState.Success(data)
                        SnackBarController.showError(it.message)
                    }
            }
        }
    }


    fun purchaseLumpSum() {
        viewModelScope.launch {
            _loading.value = true
            purchaseLumpSumUseCase()
                .onSuccess {
                    _loading.value = false
                    _cartSideEffect.emit(
                        CartSideEffects.OpenForLumpSumPurchase(
                            url = it
                        )
                    )
                }
                .onError {
                    _loading.value = false
                    SnackBarController.showError(it.message)
                }
        }

    }

    fun enableStepUp(item: SipItemDomain) {
        val current = _uiState.value
        if (current !is UiState.Success) return

        val updatedSipItems = current.data.cartData.sipItems.map { sip ->
            if (sip.id == item.id) {
                sip.copy(
                    stepUpRequired = true,
                    stepUpAmount = sip.minStepUpAmount
                )
            } else {
                sip
            }
        }

        _uiState.value = UiState.Success(
            current.data.copy(
                cartData = current.data.cartData.copy(
                    sipItems = updatedSipItems
                )
            )
        )
    }

    fun disableStepUp(item: SipItemDomain) {
        val current = _uiState.value
        if (current !is UiState.Success) return

        val updatedSipItems = current.data.cartData.sipItems.map { sip ->
            if (sip.id == item.id) {
                sip.copy(
                    stepUpRequired = false,
                    stepUpAmount = 0
                )
            } else {
                sip
            }
        }

        _uiState.value = UiState.Success(
            current.data.copy(
                cartData = current.data.cartData.copy(
                    sipItems = updatedSipItems
                )
            )
        )
    }

    fun updateStepUpAmount(
        item: SipItemDomain,
        amount: String
    ) {
        val current = _uiState.value
        if (current !is UiState.Success) return

        val amountValue = amount.toLongOrNull() ?: 0L

        val updatedSipItems = current.data.cartData.sipItems.map { sip ->
            if (sip.id == item.id) {
                sip.copy(
                    stepUpAmount = amountValue
                )
            } else {
                sip
            }
        }

        _uiState.value = UiState.Success(
            current.data.copy(
                cartData = current.data.cartData.copy(
                    sipItems = updatedSipItems
                )
            )
        )
    }



    fun showPopup(){
        _confirmationPopupVisible.value=true
    }

    fun hidePopup(){
        _confirmationPopupVisible.value=false
    }

}