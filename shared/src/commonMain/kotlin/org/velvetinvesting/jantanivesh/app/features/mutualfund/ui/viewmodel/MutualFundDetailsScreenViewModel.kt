package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorType
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.trimTo
import org.velvetinvesting.jantanivesh.app.features.core.utils.pruneForGraph
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CalculatorInputState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CartBottomSheetState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.DetailsState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.Duration
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.GraphDurationSelection
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.GraphState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.InvestmentFrequency
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MFPurchaseTypes
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.Metrics
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundGraphPointsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundScreenState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.StableMetricUi
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.toSipRequest
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.AddToCartLumpsumUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.AddToCartSipUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetMutualFundDetailsUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetMutualFundGraphUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetUserCartUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.FundTypeSelector
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.SelectedFundType

class MutualFundDetailsScreenViewModel(
    private val id: String,
    private val getDetailsUseCase: GetMutualFundDetailsUseCase,
    private val getGraphUseCase: GetMutualFundGraphUseCase,
    private val loadCartUseCase: GetUserCartUseCase,
    private val addToCartLumpsumUseCase: AddToCartLumpsumUseCase,
    private val addToCartSIPUseCase: AddToCartSipUseCase,
): ViewModel() {

    private val _detailsState=MutableStateFlow<DetailsState>(DetailsState.Loading)
    val detailsState = _detailsState.asStateFlow()

    private val _calculatorInput = MutableStateFlow(CalculatorInputState())
    val calculatorInput = _calculatorInput.asStateFlow()

    private val _graphState = MutableStateFlow<GraphState>(GraphState.Loading)
    val graphState: StateFlow<GraphState> = _graphState.asStateFlow()

    private val _selectedYear = MutableStateFlow(GraphDurationSelection.ThreeYears)
    val selectedYear= _selectedYear.asStateFlow()

    val navChangePercent = combine(graphState,detailsState,selectedYear){ graphState,detailsState, selectedYear->
        if (selectedYear == GraphDurationSelection.All){
            when(detailsState){
                is DetailsState.Error -> "n/a"
                DetailsState.Loading -> "--"
                is DetailsState.Success -> detailsState.data.metrics.nav_change_pct?.trimTo(2)?:"n/a"
            }
        }
        else{
            when(graphState){
                is GraphState.Error -> "n/a"
                GraphState.Loading -> "--"
                is GraphState.Success -> {
                    val first = graphState.data.graphPoints.firstOrNull()?.navValue?:0.0
                    val last = graphState.data.graphPoints.lastOrNull()?.navValue?:0.0
                    ((last - first) / first * 100).trimTo(2)
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = "--"
    )

    private val _bottomSheetVisibility= MutableStateFlow<MFBottomSheetType?>(null)
    val bottomSheetVisibility= _bottomSheetVisibility.asStateFlow()

    private val _cartSheetState= MutableStateFlow<CartBottomSheetState>(CartBottomSheetState())
    val cartSheetState= _cartSheetState.asStateFlow()

    fun onCartAmountChange(amount: String) {
        if (amount.isEmpty()) {
            _cartSheetState.value = _cartSheetState.value.copy(amount = null)
            return
        }

        val safeAmount = amount.toLongOrNull()
        if (safeAmount != null) {
            _cartSheetState.value = _cartSheetState.value.copy(amount = safeAmount)
        }

    }

    fun onCartTypeChange(
        type: MFPurchaseTypes
    ){
        _cartSheetState.value = _cartSheetState.value.copy(selectedType = type)
    }

    fun onCartFrequencyChange(
        frequency: InvestmentFrequency
    ){
        _cartSheetState.value = _cartSheetState.value.copy(selectedFrequency = frequency)
    }

    fun onCartDateChange(
        date: String
    ){
        _cartSheetState.value = _cartSheetState.value.copy(selectedSIPDate = date)
    }

    fun onCartDurationChange(
        duration: Duration
    ){
        _cartSheetState.value = _cartSheetState.value.copy(selectedDuration = duration)
    }

    fun showCartFrequenciesDropDown(){
        _cartSheetState.value = _cartSheetState.value.copy(frequencyDropDownExpanded = true)
    }

    fun showCartDateDropDown(){
        _cartSheetState.value = _cartSheetState.value.copy(dayDropDownExpanded = true)
    }

    fun showCartDurationDropDown(){
        _cartSheetState.value = _cartSheetState.value.copy(durationDropDownExpanded = true)
    }

    fun onCartDropDownDismiss(){
        _cartSheetState.value = _cartSheetState.value.copy(
            frequencyDropDownExpanded = false,
            dayDropDownExpanded = false,
            durationDropDownExpanded = false)
    }


    val chartPoints: StateFlow<List<MutualFundGraphPointsDomain>> =
        graphState.map { state ->
            when (state) {
                is GraphState.Success -> {
                    state.data.graphPoints
                        .pruneForGraph()
                }
                else -> emptyList()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    val uiState= combine(graphState,detailsState,chartPoints){graphState,detailsState, chartData->

        MutualFundScreenState(
            detailsState = detailsState,
            graphState = graphState,
            chartPoints=chartData
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MutualFundScreenState()
    )

    val stableMetric: StateFlow<StableMetricUi?> = combine(_detailsState) { detailsStateArray ->
        val detail=detailsStateArray[0]
        when (detail) {
            is DetailsState.Success -> {
                detail.data.metrics.getBestMetric()
            }
            else -> null
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    init {
        loadInitial()
    }

    fun loadInitial() {
        loadDetails()
        loadGraph()
        loadCart()
    }

    fun loadDetails() {
        viewModelScope.launch {
            _detailsState.value = DetailsState.Loading
            getDetailsUseCase(id)
                .onSuccess {
                    _detailsState.value = DetailsState.Success(it)
                    _cartSheetState.value = _cartSheetState.value.copy(minLumpSumAmount = it.minLumpSumAmount, minSipAmount = it.minSipAmount)
                }
                .onError {
                    _detailsState.value = DetailsState.Error(it.message)
                }
        }
    }


    fun loadGraph() {
        viewModelScope.launch {
            _graphState.value = GraphState.Loading
            getGraphUseCase(id, _selectedYear.value.id)
                .onSuccess {
                    _graphState.value = GraphState.Success(it)
                }
                .onError {
                    _graphState.value = GraphState.Error(it.message)
                }
        }
    }

    fun onSelectedYearChange(newDuration: GraphDurationSelection)  {
        _selectedYear.value = newDuration
        loadGraph()
    }

    fun showBottomSheet(){
        showCartBottomSheet()
    }

    fun hideBottomSheet(){
        _bottomSheetVisibility.value=null
    }

    fun addToCart(folioId: String?) {
        when(FundTypeSelector.fundType.value){
            SelectedFundType.LUMSUM -> {
                val amount= _cartSheetState.value.amount
                if (amount== null || amount < _cartSheetState.value.minLumpSumAmount)
                    return
                viewModelScope.launch {
                    startCartSheetLoading()
                    addToCartLumpsumUseCase(
                        id = id,
                        amount = amount,
                        folioId= folioId
                    ).onSuccess {
                        stopCartSheetLoading()
                        hideBottomSheet()
                        resetCartSheet()
                        SnackBarController.showSuccess("Fund Added to the cart")
                        loadCart()
                    }.onError {
                        stopCartSheetLoading()
                        if (it.type == ErrorType.MF_KYC_REQUIRED){
                            showKYCBottomSheet()
                            return@onError
                        }

                        if (it.type == ErrorType.MF_TRADING_ACCOUNT_REQUIRED){
                            showTradingAccountBottomSheet()
                            return@onError
                        }
                        hideBottomSheet()
                        SnackBarController.showError(it.message)
                    }
                }
            }
            SelectedFundType.SIP -> {
                val state = _cartSheetState.value
                val request = state.toSipRequest(id, folioId=folioId) ?: return
                viewModelScope.launch {
                    startCartSheetLoading()
                    addToCartSIPUseCase(
                        request = request
                    ).onSuccess {
                        stopCartSheetLoading()
                        hideBottomSheet()
                        resetCartSheet()
                        SnackBarController.showSuccess("Fund Added to the cart")
                        loadCart()
                    }.onError {
                        stopCartSheetLoading()
                        if (it.type == ErrorType.MF_KYC_REQUIRED){
                            showKYCBottomSheet()
                            return@onError
                        }

                        if (it.type == ErrorType.MF_TRADING_ACCOUNT_REQUIRED){
                            showTradingAccountBottomSheet()
                            return@onError
                        }
                        hideBottomSheet()
                        SnackBarController.showError(it.message)
                    }
                }
            }
        }
    }

    private fun startCartSheetLoading(){
        _cartSheetState.value = _cartSheetState.value.copy(loading = true)
    }

    private fun stopCartSheetLoading(){
        _cartSheetState.value = _cartSheetState.value.copy(loading = false)
    }

    private fun resetCartSheet(){
        _cartSheetState.value = _cartSheetState.value.copy(
            amount = null,
            selectedFrequency = null,
            selectedSIPDate = null,
            selectedDuration = null,
            frequencyDropDownExpanded = false,
            dayDropDownExpanded = false,
            durationDropDownExpanded = false,
            loading = false,
        )
    }


    private fun showKYCBottomSheet(){
        hideBottomSheet()
        _bottomSheetVisibility.value=MFBottomSheetType.KYC
    }

    private fun showTradingAccountBottomSheet(){
        hideBottomSheet()
        _bottomSheetVisibility.value=MFBottomSheetType.TRADING_ACCOUNT
    }

    private fun showCartBottomSheet(){
        _bottomSheetVisibility.value=MFBottomSheetType.CART
    }

    fun onSipToggle(isSip: Boolean) {
        _calculatorInput.value = _calculatorInput.value.copy(isSip = isSip)
    }

    fun onInvestmentChange(value: Long) {
        _calculatorInput.value = _calculatorInput.value.copy(monthlyInvestment = value)
    }

    fun onTimeChange(value: Int) {
        _calculatorInput.value = _calculatorInput.value.copy(timeInYears = value)
    }
    fun loadCart(){
        viewModelScope.launch {
            loadCartUseCase()
        }
    }
}

fun Metrics.getBestMetric(): StableMetricUi? {
    return when {
        return_3y != null -> StableMetricUi("3Y", return_3y)
        return_1y != null -> StableMetricUi("1Y", return_1y)
        return_6m != null -> StableMetricUi("6M", return_6m)
        return_90d != null -> StableMetricUi("3M", return_90d)
        return_30d != null -> StableMetricUi("1M", return_30d)
        else -> null
    }
}


fun Metrics.getPreferredReturn(): Float {
    return when {
        return_1y != null -> return_1y.toFloat()
        return_6m != null -> return_6m.toFloat()
        return_90d != null -> return_90d.toFloat()
        return_30d != null -> return_30d.toFloat()
        return_3y != null -> return_3y.toFloat()
        return_5y != null -> return_5y.toFloat()
        else -> 12f
    }
}

enum class MFBottomSheetType{
    KYC,TRADING_ACCOUNT,CART
}