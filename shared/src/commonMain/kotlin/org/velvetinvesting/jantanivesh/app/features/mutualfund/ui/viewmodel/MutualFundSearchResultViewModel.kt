package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SelectedReturnRatePeriod
import org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem.InvestmentFilter
import org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem.createInitialInvestmentFilter
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.LoadingState
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.core.utils.LabelFilter
import org.velvetinvesting.jantanivesh.app.features.core.utils.MutualFundLabel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetMutualFundSearchResultUseCase

class MutualFundSearchResultViewModel(
    private val search: String?,
    private val fundCategory: String?,
    private val getMutualFundSearchResultUseCase: GetMutualFundSearchResultUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _mutualFunds = MutableStateFlow<List<MutualFundDomain>>(emptyList())
    val mutualFunds = _mutualFunds.asStateFlow()

    private val _selectedYear =
        MutableStateFlow<SelectedReturnRatePeriod>(SelectedReturnRatePeriod.ONE_YEAR)
    val selectedYear = _selectedYear.asStateFlow()

    val sortedFunds: StateFlow<List<MutualFundDomain>> = _mutualFunds
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _selectedFilter = MutableStateFlow<LabelFilter?>(null)
    val selectedFilter: StateFlow<LabelFilter?> = _selectedFilter

    private val _showFilterScreen = MutableStateFlow(false)
    val showFilterScreen: StateFlow<Boolean> = _showFilterScreen

    private val _filterState =
        MutableStateFlow<InvestmentFilter>(createInitialInvestmentFilter())
    val filterState: StateFlow<InvestmentFilter> = _filterState

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private var currentPage = 1
    private var _hasNextPage = MutableStateFlow(true)
    val hasNextPage = _hasNextPage.asStateFlow()

    private val _isLoadingNext = MutableStateFlow(false)
    val isLoadingNext = _isLoadingNext.asStateFlow()

    init {
        loadFunds()
    }

    fun loadFunds() {
        viewModelScope.launch {

            _loadingState.value = LoadingState.Loading

            val (risk, category, fundCategoryFilter) = getSelectedFilters()

            getMutualFundSearchResultUseCase(
                search = search,
                fundCategory = fundCategoryFilter ?: fundCategory,
                risk = risk,
                category = category,
                page = 1,
                limit = 20
            )
                .onSuccess { data ->

                    currentPage = data.page
                    _hasNextPage.value = data.hasNextPage

                    _mutualFunds.value = data.items

                    _loadingState.value = LoadingState.Success
                }
                .onError { error ->
                    _loadingState.value = LoadingState.Error(error.message)
                }
        }
    }

    fun loadNext() {

        if (!_hasNextPage.value || _isLoadingNext.value) return

        viewModelScope.launch {

            _isLoadingNext.value = true

            val nextPage = currentPage + 1

            val (risk, category, fundCategoryFilter) = getSelectedFilters()

            getMutualFundSearchResultUseCase(
                search = search,
                fundCategory = fundCategoryFilter ?: fundCategory,
                risk = risk,
                category = category,
                page = nextPage,
                limit = 20
            )
                .onSuccess { data ->

                    currentPage = data.page
                    _hasNextPage.value = data.hasNextPage

                    _mutualFunds.value += data.items
                }
                .onError {
                    SnackBarController.showError(it.message)
                }

            _isLoadingNext.value = false
        }
    }

    fun onFilterSelected(filter: LabelFilter) {
        if (filter !is MutualFundLabel) return
        // Deselect Existing Filter
        if (_selectedFilter.value == filter) {
            _selectedFilter.value = null
            clearFilter()
            return
        }
        _selectedFilter.value = filter
        val freshFilter = createInitialInvestmentFilter()
        val updatedGroups = freshFilter.groups.map { group ->
            if (group.id != "fund_category") {
                group
            } else {
                group.copy(
                    options = group.options.map { option ->
                        option.copy(
                            isSelected = option.id == filter.id
                        )
                    }
                )
            }
        }
        _filterState.value = InvestmentFilter(updatedGroups)
        currentPage = 1
        _hasNextPage.value = true
        loadFunds()
    }
    fun cycleReturnRatePeriod() {

        when (_selectedYear.value) {
            SelectedReturnRatePeriod.THREE_MONTH ->
                _selectedYear.value = SelectedReturnRatePeriod.SIX_MONTH

            SelectedReturnRatePeriod.SIX_MONTH ->
                _selectedYear.value = SelectedReturnRatePeriod.ONE_YEAR

            SelectedReturnRatePeriod.ONE_YEAR ->
                _selectedYear.value = SelectedReturnRatePeriod.THREE_YEAR

            SelectedReturnRatePeriod.THREE_YEAR ->
                _selectedYear.value = SelectedReturnRatePeriod.THREE_MONTH
        }
    }

    fun applyFilter(newFilter: InvestmentFilter) {

        _filterState.value = newFilter

        currentPage = 1
        _hasNextPage.value = true

        _selectedFilter.value =
            MutualFundLabel.CustomLabel(
                newFilter.getActiveFundFilterLabel(), "Custom"
            )

        loadFunds()
    }

    fun clearFilter() {

        _filterState.value = createInitialInvestmentFilter()

        currentPage = 1
        _hasNextPage.value = true

        loadFunds()
    }

    fun toggleFilterScreen() {
        _showFilterScreen.value = !_showFilterScreen.value
    }

    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
    }

    private fun getSelectedFilters(): Triple<Int?, String?, String?> {

        val groups = _filterState.value.groups

        val risk = groups
            .find { it.id == "risk" }
            ?.options
            ?.firstOrNull { it.isSelected }
            ?.id
            ?.toIntOrNull()

        val category = groups
            .find { it.id == "category" }
            ?.options
            ?.firstOrNull { it.isSelected }
            ?.id

        val fundCategory = groups
            .find { it.id == "fund_category" }
            ?.options
            ?.firstOrNull { it.isSelected }
            ?.id

        return Triple(
            risk,
            category,
            fundCategory
        )
    }
}



val defaultFilters: List<LabelFilter> = listOf(
    MutualFundLabel.IndexOnly,
    MutualFundLabel.FlexiCap,
    MutualFundLabel.LargeCap,
    MutualFundLabel.MidCap,
    MutualFundLabel.SmallCap,
    MutualFundLabel.LargeMidCap,
    MutualFundLabel.GlobalOthers
)

fun InvestmentFilter.getActiveFundFilterLabel(): String {

    val groups = groups

    val risk = groups
        .find { it.id == "risk" }
        ?.options
        ?.firstOrNull { it.isSelected }

    val category = groups
        .find { it.id == "category" }
        ?.options
        ?.firstOrNull { it.isSelected }

    val fundCategory = groups
        .find { it.id == "fund_category" }
        ?.options
        ?.firstOrNull { it.isSelected }

    val parts = mutableListOf<String>()

    risk?.let {
        parts.add(it.title)
    }

    category?.let {
        parts.add(it.title)
    }

    fundCategory?.let {
        parts.add(it.title)
    }

    return if (parts.isEmpty()) {
        "All Funds"
    } else {
        parts.joinToString(" • ")
    }
}