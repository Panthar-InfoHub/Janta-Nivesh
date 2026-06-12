package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.AddBundleToCartSipUseCase
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.LoadingState
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundlecart.AddBundleSipRequest
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.InvestmentFrequency
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SelectedReturnRatePeriod
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.AddBundleToCartLumpsumUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetBundleFundsUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.FundTypeSelector
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.SelectedFundType

data class BundleCartUiState(
    val amount: Long? = null,
    val loading: Boolean = false,

    val selectedFrequency: InvestmentFrequency? = null,
    val sipDay: Int? = null,

    val frequencyDropDownExpanded: Boolean = false,
    val sipDayDropDownExpanded: Boolean = false
)

class BundleResultViewModel(
    private val bundleKey: String,
    private val getBundleFundsUseCase: GetBundleFundsUseCase,
    private val addBundleToCartLumpsumUseCase: AddBundleToCartLumpsumUseCase,
    private val addBundleToCartSipUseCase: AddBundleToCartSipUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _bundleData = MutableStateFlow<BundledMutualFundDomain?>(null)
    val bundleData: StateFlow<BundledMutualFundDomain?> = _bundleData.asStateFlow()

    private val _selectedYear = MutableStateFlow(SelectedReturnRatePeriod.THREE_YEAR)
    val selectedYear: StateFlow<SelectedReturnRatePeriod> = _selectedYear.asStateFlow()

    private val _showCartSheet = MutableStateFlow(false)
    val showCartSheet = _showCartSheet.asStateFlow()

    private val _bundleCartState = MutableStateFlow(BundleCartUiState())
    val bundleCartState = _bundleCartState.asStateFlow()

    init {
        loadBundleFunds()
    }

    fun loadBundleFunds() {

        viewModelScope.launch {

            _loadingState.value = LoadingState.Loading

            getBundleFundsUseCase(bundleKey)
                .onSuccess { data ->
                    _bundleData.value = data
                    _loadingState.value = LoadingState.Success
                }
                .onError { error ->
                    _loadingState.value = LoadingState.Error(error.message)
                }
        }
    }

    fun showCartSheet() {
        _showCartSheet.value = true
    }

    fun hideCartSheet() {
        _showCartSheet.value = false
    }

    fun onBundleAmountChange(value: String) {
        _bundleCartState.value = _bundleCartState.value.copy(
            amount = value.toLongOrNull()
        )
    }

    fun onBundleFrequencyChange(frequency: InvestmentFrequency) {
        _bundleCartState.value = _bundleCartState.value.copy(
            selectedFrequency = frequency,
            frequencyDropDownExpanded = false
        )
    }

    fun onSipDaySelected(day: Int) {
        _bundleCartState.value = _bundleCartState.value.copy(
            sipDay = day,
            sipDayDropDownExpanded = false
        )
    }

    fun showFrequencyDropDown() {
        _bundleCartState.value = _bundleCartState.value.copy(
            frequencyDropDownExpanded = true
        )
    }

    fun dismissFrequencyDropDown() {
        _bundleCartState.value = _bundleCartState.value.copy(
            frequencyDropDownExpanded = false
        )
    }

    fun showSipDayDropDown() {
        _bundleCartState.value = _bundleCartState.value.copy(
            sipDayDropDownExpanded = true
        )
    }

    fun dismissSipDayDropDown() {
        _bundleCartState.value = _bundleCartState.value.copy(
            sipDayDropDownExpanded = false
        )
    }

    private fun getSipStartDateAfter30Days(): String {

        val today = kotlin.time.Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date

        return today
            .plus(DatePeriod(days = 31))
            .toString()
    }

    fun addBundleToCart() {

        val state = _bundleCartState.value

        when (FundTypeSelector.fundType.value) {

            SelectedFundType.LUMSUM -> {

                val amount = state.amount ?: return

                viewModelScope.launch {

                    startBundleCartLoading()

                    addBundleToCartLumpsumUseCase(
                        bundleId = bundleKey,
                        amount = amount
                    )
                        .onSuccess {

                            stopBundleCartLoading()
                            hideCartSheet()
                            resetBundleCartState()

                            SnackBarController.showSuccess(
                                "Bundle added to cart"
                            )
                        }
                        .onError {

                            stopBundleCartLoading()
                            hideCartSheet()

                            SnackBarController.showError(it.message)
                        }
                }
            }

            SelectedFundType.SIP -> {

                val amount = state.amount ?: return
                val frequency = state.selectedFrequency ?: return
                val sipDay = state.sipDay ?: return

                val startDate = getSipStartDateAfter30Days()
                val endDate = "2099-12-30"

                viewModelScope.launch {

                    startBundleCartLoading()

                    addBundleToCartSipUseCase(
                        AddBundleSipRequest(
                            bundle_id = bundleKey,
                            amount = amount,
                            sip_amt = amount,
                            sip_freq = frequency.code,
                            sip_st_date = startDate,
                            sip_en_date = endDate,
                            sip_day = sipDay
                        )
                    )
                        .onSuccess {

                            stopBundleCartLoading()
                            hideCartSheet()
                            resetBundleCartState()

                            SnackBarController.showSuccess(
                                "Bundle SIP added to cart"
                            )
                        }
                        .onError {

                            stopBundleCartLoading()
                            hideCartSheet()

                            SnackBarController.showError(it.message)
                        }
                }
            }
        }
    }

    private fun startBundleCartLoading() {
        _bundleCartState.value = _bundleCartState.value.copy(
            loading = true
        )
    }

    private fun stopBundleCartLoading() {
        _bundleCartState.value = _bundleCartState.value.copy(
            loading = false
        )
    }

    private fun resetBundleCartState() {
        _bundleCartState.value = BundleCartUiState()
    }

    fun cycleReturnRatePeriod() {
        when (_selectedYear.value) {
            SelectedReturnRatePeriod.THREE_MONTH -> {
                _selectedYear.value = SelectedReturnRatePeriod.SIX_MONTH
            }

            SelectedReturnRatePeriod.SIX_MONTH -> {
                _selectedYear.value = SelectedReturnRatePeriod.ONE_YEAR
            }

            SelectedReturnRatePeriod.ONE_YEAR -> {
                _selectedYear.value = SelectedReturnRatePeriod.THREE_YEAR
            }

            SelectedReturnRatePeriod.THREE_YEAR -> {
                _selectedYear.value = SelectedReturnRatePeriod.THREE_MONTH
            }
        }
    }
}