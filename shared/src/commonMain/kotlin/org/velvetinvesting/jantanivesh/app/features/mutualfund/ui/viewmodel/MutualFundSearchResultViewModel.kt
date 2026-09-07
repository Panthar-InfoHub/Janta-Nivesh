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
import org.velvetinvesting.jantanivesh.app.features.core.utils.AmountTypeLabel
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

    /**
     * The chips that read as on. Tag and amount type are separate query parameters, so both can
     * be lit at once — a single "selected filter" could not express that.
     */
    private val _selectedChipIds = MutableStateFlow(_filterState.value.toSelectedChipIds())
    val selectedChipIds: StateFlow<Set<String>> = _selectedChipIds

    /** Stands in for a tray selection no chip covers; null when the chips say it all. */
    private val _customFilter = MutableStateFlow(_filterState.value.toCustomChipLabel())
    val customFilter: StateFlow<LabelFilter?> = _customFilter

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
                    _totalFunds.value = data.items.size

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
     * A chip tap sets one group and leaves the rest of the tray alone — the chips are shortcuts
     * into a single group each, not a replacement for the whole selection. Tapping a lit chip
     * clears its group.
     */
    fun onFilterSelected(filter: LabelFilter) {
        // Tapping the standing custom chip is how the tray selection is cleared.
        if (filter is MutualFundLabel.CustomLabel) {
            clearFilter()
            return
        }

        val group = when (filter) {
            is AmountTypeLabel -> MfFilterIds.AMOUNT_TYPE
            is MutualFundLabel -> MfFilterIds.TAG
            else -> return
        }

        val isAlreadySelected = _filterState.value.selectedId(group) == filter.id

        _filterState.value = _filterState.value.withSelections(
            group to filter.id.takeUnless { isAlreadySelected }
        )
        syncChips()

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
        syncChips()

        reload()
    }

    fun clearFilter() {

        _filterState.value = createInitialInvestmentFilter()
        syncChips()

        reload()
    }

    private fun syncChips() {
        _selectedChipIds.value = _filterState.value.toSelectedChipIds()
        _customFilter.value = _filterState.value.toCustomChipLabel()
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

/** Which chips are lit: the selected tag and the selected amount type, each if there is one. */
fun InvestmentFilter.toSelectedChipIds(): Set<String> = setOfNotNull(
    selectedId(MfFilterIds.TAG),
    selectedId(MfFilterIds.AMOUNT_TYPE)
)

/**
 * Category is the one tray group with no chip of its own, so a category selection is shown as a
 * summary chip; tapping it clears the tray. Everything else the chips already say.
 */
fun InvestmentFilter.toCustomChipLabel(): LabelFilter? {
    if (toQuery().category == null) return null

    return MutualFundLabel.CustomLabel(getActiveFundFilterLabel(), "custom")
}

/**
 * The chip row. The two minimum-installment chips lead — they are the cheapest way in for a new
 * investor, and the home screen's micro-SIP cards land on this list already filtered by one.
 */
val defaultFilters: List<LabelFilter> = listOf(
    AmountTypeLabel.DailyTen,
    AmountTypeLabel.MonthlyHundred,
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
