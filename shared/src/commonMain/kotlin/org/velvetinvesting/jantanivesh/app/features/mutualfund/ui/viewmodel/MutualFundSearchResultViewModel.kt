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
import org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem.MfFilterIds
import org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem.createInitialInvestmentFilter
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.LoadingState
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.core.utils.LabelFilter
import org.velvetinvesting.jantanivesh.app.features.core.utils.MutualFundLabel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetMutualFundSearchResultUseCase

/**
 * Backs the fund list.
 *
 * The filter tray is the single source of truth for what is queried: the route's own [tag],
 * [category] and [amountType] are folded into the initial tray state rather than kept beside it,
 * so a screen opened pre-filtered — the daily/monthly SIP cards on home, a category tile — shows
 * that filter as selected and clearing it behaves like clearing any other.
 */
class MutualFundSearchResultViewModel(
    private val search: String?,
    tag: String?,
    category: String?,
    amountType: String?,
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

    private val _filterState = MutableStateFlow(
        createInitialInvestmentFilter().withSelections(
            MfFilterIds.TAG to tag,
            MfFilterIds.CATEGORY to category,
            MfFilterIds.AMOUNT_TYPE to amountType
        )
    )
    val filterState: StateFlow<InvestmentFilter> = _filterState

    private val _selectedFilter = MutableStateFlow(_filterState.value.toChipLabel())
    val selectedFilter: StateFlow<LabelFilter?> = _selectedFilter

    private val _showFilterScreen = MutableStateFlow(false)
    val showFilterScreen: StateFlow<Boolean> = _showFilterScreen

    /** Total the header reports, which is the server's count and not the page in hand. */
    private val _totalFunds = MutableStateFlow(0)
    val totalFunds = _totalFunds.asStateFlow()

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

            val filters = _filterState.value.toQuery()

            getMutualFundSearchResultUseCase(
                search = search,
                tag = filters.tag,
                category = filters.category,
                amountType = filters.amountType,
                page = 1,
                limit = PAGE_SIZE
            )
                .onSuccess { data ->

                    currentPage = data.page
                    _hasNextPage.value = data.hasNextPage
                    _totalFunds.value = data.totalItems

                    _mutualFunds.value = data.items

                    _loadingState.value = LoadingState.Success
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
                    _loadingState.value = LoadingState.Error(error.message)
                }
        }
    }

    fun loadNext() {

        if (!_hasNextPage.value || _isLoadingNext.value) return

        viewModelScope.launch {

            _isLoadingNext.value = true

            val nextPage = currentPage + 1

            val filters = _filterState.value.toQuery()

            getMutualFundSearchResultUseCase(
                search = search,
                tag = filters.tag,
                category = filters.category,
                amountType = filters.amountType,
                page = nextPage,
                limit = PAGE_SIZE
            )
                .onSuccess { data ->

                    currentPage = data.page
                    _hasNextPage.value = data.hasNextPage
                    _totalFunds.value = data.totalItems

                    _mutualFunds.value += data.items
                }
                .onError {
                    SnackBarController.showError(it.message)
                }

            _isLoadingNext.value = false
        }
    }

    /**
     * A chip tap sets the `tag` filter and leaves the rest of the tray alone — the chips are a
     * shortcut into one group, not a replacement for the whole selection.
     */
    fun onFilterSelected(filter: LabelFilter) {
        if (filter !is MutualFundLabel) return

        // Tapping the standing custom chip is how the tray selection is cleared.
        if (filter is MutualFundLabel.CustomLabel) {
            clearFilter()
            return
        }

        val isAlreadySelected = _filterState.value.selectedId(MfFilterIds.TAG) == filter.id

        _filterState.value = _filterState.value.withSelections(
            MfFilterIds.TAG to filter.id.takeUnless { isAlreadySelected }
        )
        _selectedFilter.value = _filterState.value.toChipLabel()

        reload()
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
        _selectedFilter.value = newFilter.toChipLabel()

        reload()
    }

    fun clearFilter() {

        _filterState.value = createInitialInvestmentFilter()
        _selectedFilter.value = null

        reload()
    }

    fun toggleFilterScreen() {
        _showFilterScreen.value = !_showFilterScreen.value
    }

    private fun reload() {
        currentPage = 1
        _hasNextPage.value = true
        loadFunds()
    }

    private companion object {
        /** The endpoint caps `limit` at 50. */
        const val PAGE_SIZE = 20
    }
}

/** The three query values `GET /mf/funds` filters on, null where nothing is selected. */
data class FundQueryFilters(
    val tag: String?,
    val category: String?,
    val amountType: String?
)

fun InvestmentFilter.selectedId(groupId: String): String? =
    groups.find { it.id == groupId }
        ?.options
        ?.firstOrNull { it.isSelected }
        ?.id

fun InvestmentFilter.toQuery(): FundQueryFilters = FundQueryFilters(
    tag = selectedId(MfFilterIds.TAG),
    // "all" is the server's own default and means "no category filter", so it is not sent.
    category = selectedId(MfFilterIds.CATEGORY)?.takeIf { it != MfFilterIds.CATEGORY_ALL },
    amountType = selectedId(MfFilterIds.AMOUNT_TYPE)
)

/**
 * Applies `groupId to optionId` selections, replacing whatever that group had. A null option id
 * clears the group, and a group named here that the tray does not define is ignored.
 */
fun InvestmentFilter.withSelections(vararg selections: Pair<String, String?>): InvestmentFilter {
    val bySelection = selections.toMap()

    return copy(
        groups = groups.map { group ->
            if (!bySelection.containsKey(group.id)) return@map group

            val selectedId = bySelection[group.id]
            group.copy(
                options = group.options.map { it.copy(isSelected = it.id == selectedId) }
            )
        }
    )
}

/**
 * The chip that stands for the current tray state: the tag chip itself when the tag is the only
 * thing selected — so the row highlights it rather than showing a redundant custom chip — and a
 * summary chip otherwise.
 */
fun InvestmentFilter.toChipLabel(): LabelFilter? {
    val tag = selectedId(MfFilterIds.TAG)
    val query = toQuery()

    if (query.category == null && query.amountType == null) {
        return defaultFilters.firstOrNull { it.id == tag }
    }

    return MutualFundLabel.CustomLabel(getActiveFundFilterLabel(), "custom")
}

/** The chip row, in the order the sections are presented server-side. */
val defaultFilters: List<LabelFilter> = listOf(
    MutualFundLabel.Popular,
    MutualFundLabel.LargeCap,
    MutualFundLabel.MidCap,
    MutualFundLabel.SmallCap,
    MutualFundLabel.FlexiCap,
    MutualFundLabel.MultiCap,
    MutualFundLabel.Debt,
    MutualFundLabel.Others
)

fun InvestmentFilter.getActiveFundFilterLabel(): String {

    val parts = groups.mapNotNull { group ->
        group.options.firstOrNull { it.isSelected }?.title
    }

    return if (parts.isEmpty()) "All Funds" else parts.joinToString(" • ")
}
