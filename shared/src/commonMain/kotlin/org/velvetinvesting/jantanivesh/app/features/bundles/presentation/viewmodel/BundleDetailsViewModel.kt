package org.velvetinvesting.jantanivesh.app.features.bundles.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleCategoryDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleTransactionRules
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.FundDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.PortfolioSlotDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.deriveTransactionRules
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.investmentName
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.minAmountFor
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.supports
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.usecases.GetBundleDetailsUseCase
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.toBundleFund
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.GetSchemePlanUseCase
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.bundles.presentation.viewmodel.BundleDetailsEffect.*
import org.velvetinvesting.jantanivesh.app.features.bundles.presentation.viewmodel.SelectFundEffect.*
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PurchaseMode
import org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.bundlecart.AddBundleLumpsumRequest
import org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.bundlecart.AddBundleSipRequest
import org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.bundlecart.BundleFundSelectionRequest
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.AddBundleToCartLumpsumUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.AddBundleToCartSipUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.GetUserCartUseCase
import org.velvetinvesting.jantanivesh.app.features.cart.domain.CartCountController

/**
 * The fund picker for one category, held while the select-fund screen is open.
 *
 * Picks stay in [pendingSelections] until saved, so backing out of the picker leaves the bundle
 * exactly as it was.
 */
data class FundSelectionState(
    val categoryId: String,
    val activeSlotId: String? = null,
    val pendingSelections: Map<String, FundDomain> = emptyMap()
)

data class BundleDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val bundle: BundleDetailsDomain? = null,
    val transactionRules: BundleTransactionRules? = null,
    val purchaseMode: PurchaseMode = PurchaseMode.MONTHLY,
    val investmentAmount: Long = 0L,
    val selectedSipDay: Int? = null,
    val showSipDayPicker: Boolean = false,
    val fundSelection: FundSelectionState? = null,
    /** The fund picked on the explore-funds screen, not yet saved. */
    val exploreSelectedFund: MutualFundDomain? = null,
    /**
     * Explored funds whose details are in, keyed by their fund-list id. Fetched when the fund is
     * tapped, so a fund that can't be bought as [purchaseMode] can be shown as unselectable.
     */
    val exploredFunds: Map<String, FundDomain> = emptyMap(),
    /** Fund-list ids whose details can't be looked up (no ISIN), so they can't be picked. */
    val exploreUnavailableIds: Set<String> = emptySet(),
    /** The explored fund whose details are loading. */
    val exploreLoadingFundId: String? = null,
    val isAddingToCart: Boolean = false,
    /** Funds in the user's cart, SIP and one-time together, for the header badge. */
    val cartFundCount: Int = 0,
    /**
     * Whether any slot now holds a different fund from the one it loaded with. Until then the
     * minimum is the server's start amount; from then on it is worked out from the funds selected.
     */
    val hasChangedFunds: Boolean = false
) {
    /** Selected funds that can't be bought as [purchaseMode]; they block investing until changed. */
    val unsupportedSlots: List<PortfolioSlotDomain>
        get() = bundle?.slots?.filter { slot ->
            slot.selectedFund?.supports(purchaseMode) == false
        }.orEmpty()

    /** Whether every selected fund can be bought as [purchaseMode]. */
    val isPurchaseModeSupported: Boolean
        get() = unsupportedSlots.isEmpty()

    /**
     * The bundle minimum for [purchaseMode]. It reads 0 while a selected fund can't be bought that
     * way, rather than a figure worked out from only some of the funds.
     *
     * With the funds the bundle loaded with, it is the server's start amount for the mode. Once a
     * fund is changed — or where the server sent none — it is worked out from the selected funds.
     */
    val minAmount: Long
        get() {
            if (!isPurchaseModeSupported) return 0L
            if (!hasChangedFunds) bundle?.metaData?.startAmountFor(purchaseMode)?.let { return it }
            return transactionRules?.minAmountFor(purchaseMode)?.toLong() ?: 0L
        }

    val isFetchingExploredFund: Boolean
        get() = exploreLoadingFundId != null

    val allSlotsFilled: Boolean
        get() = bundle?.slots?.all { it.selectedFund != null } == true

    val canInvest: Boolean
        get() = bundle != null &&
                allSlotsFilled &&
                investmentAmount > 0 &&
                investmentAmount >= minAmount &&
                isPurchaseModeSupported &&
                (!purchaseMode.needsInstallmentDay || selectedSipDay != null)

}

sealed interface BundleDetailsEvent {
    data object Retry : BundleDetailsEvent
    data object OnBackClicked : BundleDetailsEvent
    data object OnCartClicked : BundleDetailsEvent

    data class OnAmountChanged(val amount: Long) : BundleDetailsEvent
    data class OnPurchaseModeSelected(val mode: PurchaseMode) : BundleDetailsEvent
    data object OnSipDayClicked : BundleDetailsEvent
    data class OnSipDaySelected(val day: Int) : BundleDetailsEvent
    data object OnSipDayPickerDismissed : BundleDetailsEvent

    data class OnChangeFundClicked(val categoryId: String) : BundleDetailsEvent
    data object OnProceedToInvestClicked : BundleDetailsEvent
    data object OnAddToCartClicked : BundleDetailsEvent

    /** Select-fund screen. */
    data class OnFundSelectionOpened(val categoryId: String) : BundleDetailsEvent
    data class OnSlotSelected(val slotId: String) : BundleDetailsEvent
    data class OnFundPicked(val fund: FundDomain) : BundleDetailsEvent
    data object OnSaveFundsClicked : BundleDetailsEvent
    data object OnFundSelectionBackClicked : BundleDetailsEvent
    data object OnExploreMoreClicked : BundleDetailsEvent

    /** Explore-funds screen. */
    data class OnExploredFundSelected(val fund: MutualFundDomain) : BundleDetailsEvent
    data object OnSaveExploredFundClicked : BundleDetailsEvent
    data object OnExploreBackClicked : BundleDetailsEvent
}

sealed interface BundleDetailsEffect {
    data object NavigateBack : BundleDetailsEffect
    data class NavigateToSelectFund(val categoryId: String) : BundleDetailsEffect
    data object NavigateToCart : BundleDetailsEffect
}

/**
 * Kept apart from [BundleDetailsEffect]: both screens collect while a transition runs, and one
 * channel would hand each effect to whichever collector reached it first.
 */
sealed interface SelectFundEffect {
    data object Close : SelectFundEffect
    data class NavigateToExplore(val categoryId: String) : SelectFundEffect
}

/** The explore-funds screen's own stream, for the same reason as [SelectFundEffect]. */
sealed interface ExploreFundsEffect {
    data object Close : ExploreFundsEffect
}

/**
 * Backs both the bundle details screen and its select-fund screen, which share this instance so
 * a fund swapped in the picker is what the details screen — and the bundle minimums — reflect.
 */
class BundleDetailsViewModel(
    private val bundleId: String,
    purchaseMode: PurchaseMode,
    private val getBundleDetailsUseCase: GetBundleDetailsUseCase,
    private val getSchemePlanUseCase: GetSchemePlanUseCase,
    private val addBundleToCartLumpsumUseCase: AddBundleToCartLumpsumUseCase,
    private val addBundleToCartSipUseCase: AddBundleToCartSipUseCase,
    private val getUserCartUseCase: GetUserCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BundleDetailsUiState(purchaseMode = purchaseMode))
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<BundleDetailsEffect>()
    val effect = _effect.receiveAsFlow()

    private val _selectFundEffect = Channel<SelectFundEffect>()
    val selectFundEffect = _selectFundEffect.receiveAsFlow()

    private val _exploreFundsEffect = Channel<ExploreFundsEffect>()
    val exploreFundsEffect = _exploreFundsEffect.receiveAsFlow()

    /**
     * Once the user types an amount, a change in the bundle minimum no longer overwrites it —
     * until they switch the way of investing, which starts the amount over.
     */
    private var hasUserSetAmount = false

    private var loadCartJob: Job? = null

    init {
        loadBundleDetails()
        observeCartCount()
        loadCart()
    }

    fun handleEvent(event: BundleDetailsEvent) {
        when (event) {
            BundleDetailsEvent.Retry -> loadBundleDetails()

            BundleDetailsEvent.OnBackClicked -> sendEffect(BundleDetailsEffect.NavigateBack)

            is BundleDetailsEvent.OnAmountChanged -> {
                hasUserSetAmount = true
                _uiState.update { it.copy(investmentAmount = event.amount.coerceAtLeast(0L)) }
            }

            is BundleDetailsEvent.OnPurchaseModeSelected -> selectPurchaseMode(event.mode)

            BundleDetailsEvent.OnSipDayClicked -> _uiState.update { it.copy(showSipDayPicker = true) }

            is BundleDetailsEvent.OnSipDaySelected -> _uiState.update {
                it.copy(selectedSipDay = event.day, showSipDayPicker = false)
            }

            BundleDetailsEvent.OnSipDayPickerDismissed -> _uiState.update { it.copy(showSipDayPicker = false) }

            is BundleDetailsEvent.OnChangeFundClicked -> {
                // A fresh picker every time, so picks abandoned by a system back don't resurface.
                _uiState.update { it.copy(fundSelection = FundSelectionState(categoryId = event.categoryId)) }
                sendEffect(NavigateToSelectFund(event.categoryId))
            }

            BundleDetailsEvent.OnProceedToInvestClicked -> {
                if (!_uiState.value.canInvest) return
                // TODO: hook up the bundle purchase API once it is available.
            }

            BundleDetailsEvent.OnAddToCartClicked -> addToCart()

            is BundleDetailsEvent.OnFundSelectionOpened -> _uiState.update {
                // Only fills in a picker that is missing, as after the process was restored; an
                // open picker for this category keeps its unsaved picks.
                if (it.fundSelection?.categoryId == event.categoryId) it
                else it.copy(fundSelection = FundSelectionState(categoryId = event.categoryId))
            }

            is BundleDetailsEvent.OnSlotSelected -> _uiState.update { state ->
                val selection = state.fundSelection ?: return@update state
                state.copy(fundSelection = selection.copy(activeSlotId = event.slotId))
            }

            is BundleDetailsEvent.OnFundPicked -> pickFund(event.fund)

            BundleDetailsEvent.OnSaveFundsClicked -> saveFundSelection()

            BundleDetailsEvent.OnFundSelectionBackClicked -> {
                _uiState.update { it.copy(fundSelection = null) }
                closeFundSelection()
            }

            BundleDetailsEvent.OnExploreMoreClicked -> {
                val categoryId = _uiState.value.fundSelection?.categoryId ?: return
                _uiState.update { it.copy(exploreSelectedFund = null) }
                viewModelScope.launch {
                    _selectFundEffect.send(NavigateToExplore(categoryId))
                }
            }

            is BundleDetailsEvent.OnExploredFundSelected -> selectExploredFund(event.fund)

            BundleDetailsEvent.OnSaveExploredFundClicked -> saveExploredFund()

            BundleDetailsEvent.OnExploreBackClicked -> {
                _uiState.update { it.copy(exploreSelectedFund = null) }
                closeExplore()
            }
            BundleDetailsEvent.OnCartClicked -> sendEffect(BundleDetailsEffect.NavigateToCart)
        }
    }

    /**
     * The minimums are already worked out for every way of investing, so switching only changes
     * which one applies. The amount starts over at the new mode's minimum: a monthly figure means
     * nothing as a daily or one-time one.
     */
    private fun selectPurchaseMode(mode: PurchaseMode) {
        if (mode == _uiState.value.purchaseMode) return
        hasUserSetAmount = false
        _uiState.update { it.copy(purchaseMode = mode, showSipDayPicker = false).withSeededAmount() }
    }

    private fun loadBundleDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val response = getBundleDetailsUseCase(bundleId)) {
                is NetworkResponse.Success -> _uiState.update {
                    it.copy(isLoading = false).withBundle(response.data)
                }

                is NetworkResponse.Error -> _uiState.update {
                    it.copy(isLoading = false, error = response.error.message)
                }
            }
        }
    }

    private fun pickFund(fund: FundDomain) {
        _uiState.update { state ->
            val selection = state.fundSelection ?: return@update state
            val category = state.categoryFor(selection) ?: return@update state
            val activeSlot = category.activeSlot(selection) ?: return@update state

            // One fund can fill only one slot of a category, and only if it can be bought this way.
            if (fund.id in state.fundIdsInOtherSlots()) return@update state
            if (!fund.supports(state.purchaseMode)) return@update state

            state.copy(
                fundSelection = selection.copy(
                    pendingSelections = selection.pendingSelections + (activeSlot.id to fund)
                )
            )
        }
    }

    /**
     * Picks a fund on the explore screen. Its details are fetched on the first tap — the fund list
     * carries no minimums — and kept, so a fund that can't be bought as the current mode turns
     * unselectable and saving needs no further call.
     */
    private fun selectExploredFund(listedFund: MutualFundDomain) {
        val state = _uiState.value
        if (state.isFetchingExploredFund) return
        if (listedFund.id in state.exploreUnselectableFundIds()) return

        if (listedFund.id in state.exploredFunds) {
            _uiState.update { it.copy(exploreSelectedFund = listedFund) }
            return
        }

        // The scheme plan is looked up by ISIN, which a few listed funds don't carry.
        val isin = listedFund.isin
        if (isin.isNullOrBlank()) {
            _uiState.update { it.copy(exploreUnavailableIds = it.exploreUnavailableIds + listedFund.id) }
            viewModelScope.launch {
                SnackBarController.showError("This fund's details are unavailable. Please pick another.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(exploreLoadingFundId = listedFund.id) }

            when (val response = getSchemePlanUseCase(isin)) {
                is NetworkResponse.Success -> {
                    val fund = listedFund.toBundleFund(response.data)
                    val mode = _uiState.value.purchaseMode
                    val isSupported = fund.supports(mode)
                    _uiState.update {
                        it.copy(
                            exploreLoadingFundId = null,
                            exploredFunds = it.exploredFunds + (listedFund.id to fund),
                            exploreSelectedFund = if (isSupported) listedFund else it.exploreSelectedFund
                        )
                    }
                    if (!isSupported) {
                        SnackBarController.showError("This fund isn't available for ${mode.investmentName}.")
                    }
                }

                is NetworkResponse.Error -> {
                    _uiState.update { it.copy(exploreLoadingFundId = null) }
                    SnackBarController.showError(response.error.message)
                }
            }
        }
    }

    /**
     * Saves the fund picked while exploring straight into the active slot, so the select-fund
     * screen it returns to doesn't need saving again. Its details were fetched when it was picked;
     * it is put at the top of the category's options.
     */
    private fun saveExploredFund() {
        val state = _uiState.value
        val listedFund = state.exploreSelectedFund ?: return
        val fund = state.exploredFunds[listedFund.id] ?: return
        if (state.isFetchingExploredFund || state.fundSelection == null) return
        if (!fund.supports(state.purchaseMode)) return

        val takenIds = state.fundIdsInOtherSlots()
        if (listedFund.id in takenIds || fund.id in takenIds) {
            viewModelScope.launch {
                SnackBarController.showError("This fund is already selected in another slot.")
            }
            return
        }

        _uiState.update { it.copy(exploreSelectedFund = null).withFundSavedToActiveSlot(fund) }
        closeExplore()
    }

    /**
     * Puts [fund] in the active slot of the open picker's category and saves it into the bundle.
     * The category's other unsaved picks stay pending, as the picker is still open.
     */
    private fun BundleDetailsUiState.withFundSavedToActiveSlot(fund: FundDomain): BundleDetailsUiState {
        val selection = fundSelection ?: return this
        val bundle = bundle ?: return this
        val activeSlotId = categoryFor(selection)?.activeSlot(selection)?.id ?: return this

        val withOption = bundle.withFundOption(selection.categoryId, fund)
        val updatedBundle = withOption.copy(
            categories = withOption.categories.map { category ->
                if (category.id != selection.categoryId) return@map category
                category.copy(
                    slots = category.slots.map { slot ->
                        if (slot.id == activeSlotId) slot.copy(selectedFund = fund) else slot
                    }
                )
            }
        )

        return copy(
            fundSelection = selection.copy(pendingSelections = selection.pendingSelections - activeSlotId)
        ).withBundle(updatedBundle)
    }

    /**
     * Adds the bundle to the cart as a one-time or SIP bundle, by the mode picked. The button shows
     * the loader until the call returns; on success the cart is reloaded so the badge counts the
     * bundle's funds.
     */
    private fun addToCart() {
        val state = _uiState.value
        if (!state.canInvest || state.isAddingToCart) return
        val bundle = state.bundle ?: return

        val selections = bundle.slots.mapNotNull { slot ->
            val fund = slot.selectedFund ?: return@mapNotNull null
            BundleFundSelectionRequest(
                mf_product_id = fund.id,
                allocation_percentage = slot.allocationPercentage
            )
        }

        _uiState.update { it.copy(isAddingToCart = true) }
        viewModelScope.launch {
            val response = when (state.purchaseMode) {
                PurchaseMode.ONE_TIME -> addBundleToCartLumpsumUseCase(
                    AddBundleLumpsumRequest(
                        bundle_id = bundleId,
                        amount = state.investmentAmount,
                        selections = selections
                    )
                )

                PurchaseMode.DAILY, PurchaseMode.MONTHLY -> addBundleToCartSipUseCase(
                    AddBundleSipRequest(
                        bundle_id = bundleId,
                        amount = state.investmentAmount,
                        frequency = state.purchaseMode.name,
                        installment_day = state.selectedSipDay
                            .takeIf { state.purchaseMode.needsInstallmentDay },
                        selections = selections
                    )
                )
            }

            _uiState.update { it.copy(isAddingToCart = false) }
            when (response) {
                is NetworkResponse.Success -> {
                    SnackBarController.showSuccess("Bundle added to the cart")
                    loadCart()
                }

                is NetworkResponse.Error -> SnackBarController.showError(response.error.message)
            }
        }
    }

    /**
     * The badge follows [CartCountController], so a change made on the cart screen shows here on
     * return without this screen reloading the cart.
     */
    private fun observeCartCount() {
        viewModelScope.launch {
            CartCountController.cartFundCount.collect { count ->
                _uiState.update { it.copy(cartFundCount = count) }
            }
        }
    }

    /**
     * Fetches the cart, which updates [CartCountController] and through it the badge. A reload
     * replaces one still running, so the newest count wins.
     */
    private fun loadCart() {
        loadCartJob?.cancel()
        loadCartJob = viewModelScope.launch {
            val response = getUserCartUseCase()
            if (response is NetworkResponse.Error) SnackBarController.showError(response.error.message)
        }
    }

    /** Moves [fund] to the front of a category's options, adding it if it was not listed. */
    private fun BundleDetailsDomain.withFundOption(categoryId: String?, fund: FundDomain) = copy(
        categories = categories.map { category ->
            if (category.id != categoryId) category
            else category.copy(funds = listOf(fund) + category.funds.filter { it.id != fund.id })
        }
    )

    private fun saveFundSelection() {
        val state = _uiState.value
        val selection = state.fundSelection ?: return
        val bundle = state.bundle ?: return

        val updatedBundle = bundle.copy(
            categories = bundle.categories.map { category ->
                if (category.id != selection.categoryId) return@map category
                category.copy(
                    slots = category.slots.map { slot ->
                        slot.copy(selectedFund = selection.effectiveFund(slot))
                    }
                )
            }
        )

        _uiState.update { it.copy(fundSelection = null).withBundle(updatedBundle) }
        closeFundSelection()
    }

    /**
     * Applies a new bundle and everything that follows from its selected funds. Every change of
     * selected fund comes through here — loading, saving the picker, saving an explored fund — so
     * the bundle minimums for all three ways of investing are recalculated from the funds now
     * selected each time, and [BundleDetailsUiState.minAmount] reads the one for the current mode.
     */
    private fun BundleDetailsUiState.withBundle(bundle: BundleDetailsDomain): BundleDetailsUiState {
        val rules = bundle.deriveTransactionRules()
        // The first load has no bundle to compare with; later ones count only a real fund swap,
        // so saving the picker unchanged keeps the server's minimum.
        val previous = this.bundle
        val fundsChanged = previous != null && previous.selectedFundIds() != bundle.selectedFundIds()
        return copy(
            bundle = bundle,
            transactionRules = rules,
            hasChangedFunds = hasChangedFunds || fundsChanged,
            // A swapped fund can narrow the dates every fund accepts.
            selectedSipDay = selectedSipDay?.takeIf { it in rules.sipAllowedDates }
        ).withSeededAmount()
    }

    private fun BundleDetailsDomain.selectedFundIds(): List<String?> = slots.map { it.selectedFund?.id }

    /** Starts the amount at the bundle minimum until the user enters one of their own. */
    private fun BundleDetailsUiState.withSeededAmount(): BundleDetailsUiState =
        if (hasUserSetAmount || transactionRules == null) this
        else copy(investmentAmount = minAmount)

    private fun sendEffect(effect: BundleDetailsEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    private fun closeExplore() {
        viewModelScope.launch { _exploreFundsEffect.send(ExploreFundsEffect.Close) }
    }

    private fun closeFundSelection() {
        viewModelScope.launch { _selectFundEffect.send(SelectFundEffect.Close) }
    }
}

fun BundleDetailsUiState.categoryFor(selection: FundSelectionState): BundleCategoryDomain? =
    bundle?.categories?.find { it.id == selection.categoryId }

/** The slot being edited: the one tapped, or the first slot until one is. */
fun BundleCategoryDomain.activeSlot(selection: FundSelectionState): PortfolioSlotDomain? =
    slots.find { it.id == selection.activeSlotId } ?: slots.firstOrNull()

/** What a slot will hold if the picker is saved now. */
fun FundSelectionState.effectiveFund(slot: PortfolioSlotDomain): FundDomain? =
    pendingSelections[slot.id] ?: slot.selectedFund

/** Funds the open picker has placed in the category's other slots, which the active slot can't take. */
fun BundleDetailsUiState.fundIdsInOtherSlots(): Set<String> {
    val selection = fundSelection ?: return emptySet()
    val category = categoryFor(selection) ?: return emptySet()
    val activeSlotId = category.activeSlot(selection)?.id
    return category.slots
        .filter { it.id != activeSlotId }
        .mapNotNull { selection.effectiveFund(it)?.id }
        .toSet()
}

/**
 * Explore-list funds that can't be picked: ones whose details can't be looked up, and ones whose
 * details show they can't be bought as the current mode.
 */
fun BundleDetailsUiState.exploreUnselectableFundIds(): Set<String> =
    exploreUnavailableIds + exploredFunds.filterValues { !it.supports(purchaseMode) }.keys

/** Names of the funds in [category] that can't be bought as the current mode. */
fun BundleDetailsUiState.unsupportedFundNames(category: BundleCategoryDomain): List<String> =
    category.slots.mapNotNull { slot ->
        slot.selectedFund?.takeUnless { it.supports(purchaseMode) }?.name
    }
