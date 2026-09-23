package org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalHoldingDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.GoalsRepository
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.GetPortfolioUseCase

/**
 * What the Map SIP screen shows: the goal's mapped holdings, straight out of
 * `GET /user-goal/{id}`. Nothing here is a second copy of the projection — that screen reads it.
 */
data class MapSchemeUiData(
    val goalId: String,
    val goalName: String,
    val holdings: List<GoalHoldingDomain>,
    val totalCurrentValue: Double
)

/** One of the user's own holdings, as the mapping sheet offers it. */
data class SelectableSchemeUiModel(
    val holdingId: String,
    val name: String,
    val units: String,
    val value: Double,
    val folio: String,
    val isSelected: Boolean = false
)

data class MapSchemeUiState(
    val goalDetailsState: UiState<MapSchemeUiData> = UiState.Loading,
    val portfolioDataState: UiState<List<SelectableSchemeUiModel>> = UiState.Loading,
    /** Holdings whose removal is in flight; their delete button becomes a loader. */
    val removingHoldingIds: Set<String> = emptySet(),
    val mapping: Boolean = false
)

sealed interface MapSchemeEvent {
    data object LoadData : MapSchemeEvent
    data object RetryGoalDetails : MapSchemeEvent
    data object RetryPortfolio : MapSchemeEvent
    data object OpenBottomSheet : MapSchemeEvent
    data object CloseBottomSheet : MapSchemeEvent
    data object MapSelectedHoldings : MapSchemeEvent
    data class RemoveHolding(val holdingId: String) : MapSchemeEvent
    data class ToggleSelection(val holdingId: String) : MapSchemeEvent
}

sealed interface MapSchemeEffect {
    data object OpenBottomSheet : MapSchemeEffect
    data object CloseBottomSheet : MapSchemeEffect
}

class MapSchemeViewModel(
    private val goalId: String,
    private val goalsRepository: GoalsRepository,
    private val getPortfolioUseCase: GetPortfolioUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapSchemeUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<MapSchemeEffect>()
    val effect = _effect.asSharedFlow()

    init {
        handleEvent(MapSchemeEvent.LoadData)
    }

    fun handleEvent(event: MapSchemeEvent) {
        when (event) {
            MapSchemeEvent.LoadData -> loadGoalDetails()
            MapSchemeEvent.RetryGoalDetails -> loadGoalDetails()
            MapSchemeEvent.RetryPortfolio -> loadPortfolio()
            MapSchemeEvent.OpenBottomSheet -> openBottomSheet()
            MapSchemeEvent.CloseBottomSheet -> closeBottomSheet()
            MapSchemeEvent.MapSelectedHoldings -> mapSelectedHoldings()
            is MapSchemeEvent.RemoveHolding -> removeHolding(event.holdingId)
            is MapSchemeEvent.ToggleSelection -> toggleSelection(event.holdingId)
        }
    }

    private fun loadGoalDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(goalDetailsState = UiState.Loading) }
            goalsRepository.getGoalById(goalId)
                .onSuccess { goal ->
                    _uiState.update {
                        it.copy(goalDetailsState = UiState.Success(goal.toMapSchemeUiData()))
                    }
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
                    _uiState.update { it.copy(goalDetailsState = UiState.Error(error.message)) }
                }
        }
    }

    /**
     * The sheet offers what is not mapped yet: a holding already on this goal would only fail
     * server-side, and one mapped to another goal is the server's call to reject, not ours.
     */
    private fun loadPortfolio() {
        viewModelScope.launch {
            _uiState.update { it.copy(portfolioDataState = UiState.Loading) }
            getPortfolioUseCase()
                .onSuccess { response ->
                    val mapped = mappedHoldingIds()
                    val schemes = response.mutualFunds
                        .filterNot { it.id in mapped }
                        .map { fund ->
                            SelectableSchemeUiModel(
                                holdingId = fund.id,
                                name = fund.title,
                                units = fund.balanceUnits.toString(),
                                value = fund.currentValue,
                                folio = fund.folio
                            )
                        }
                    _uiState.update { it.copy(portfolioDataState = UiState.Success(schemes)) }
                }
                .onError { error ->
                    _uiState.update { it.copy(portfolioDataState = UiState.Error(error.message)) }
                }
        }
    }

    private fun openBottomSheet() {
        viewModelScope.launch {
            _effect.emit(MapSchemeEffect.OpenBottomSheet)
            loadPortfolio()
        }
    }

    private fun closeBottomSheet() {
        viewModelScope.launch {
            _effect.emit(MapSchemeEffect.CloseBottomSheet)
        }
    }

    private fun mapSelectedHoldings() {
        val portfolioState = _uiState.value.portfolioDataState
        if (portfolioState !is UiState.Success) return

        val selected = portfolioState.data.filter { it.isSelected }.map { it.holdingId }
        if (selected.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(mapping = true) }
            goalsRepository.mapHoldings(goalId, selected)
                .onSuccess {
                    _uiState.update { it.copy(mapping = false) }
                    closeBottomSheet()
                    // The mapped list comes from the goal, so it is re-read rather than patched.
                    loadGoalDetails()
                    AppEventsController.sendGoalRefreshEvent()
                }
                .onError { error ->
                    _uiState.update { it.copy(mapping = false) }
                    SnackBarController.showError(error.message)
                }
        }
    }

    /**
     * Removal drops the one row on success instead of re-reading the goal: the screen is already
     * showing everything the refetch would return, minus the row that has just gone.
     */
    private fun removeHolding(holdingId: String) {
        if (holdingId in _uiState.value.removingHoldingIds) return

        viewModelScope.launch {
            _uiState.update { it.copy(removingHoldingIds = it.removingHoldingIds + holdingId) }
            goalsRepository.removeHolding(goalId, holdingId)
                .onSuccess {
                    _uiState.update { state ->
                        val details = state.goalDetailsState
                        val updated = if (details is UiState.Success) {
                            val remaining = details.data.holdings
                                .filterNot { it.holdingId == holdingId }
                            UiState.Success(
                                details.data.copy(
                                    holdings = remaining,
                                    totalCurrentValue = remaining.sumOf { it.currentValue }
                                )
                            )
                        } else {
                            details
                        }
                        state.copy(
                            goalDetailsState = updated,
                            removingHoldingIds = state.removingHoldingIds - holdingId
                        )
                    }
                    AppEventsController.sendGoalRefreshEvent()
                }
                .onError { error ->
                    _uiState.update {
                        it.copy(removingHoldingIds = it.removingHoldingIds - holdingId)
                    }
                    SnackBarController.showError(error.message)
                }
        }
    }

    private fun toggleSelection(holdingId: String) {
        val portfolioState = _uiState.value.portfolioDataState
        if (portfolioState !is UiState.Success) return

        val updated = portfolioState.data.map { scheme ->
            if (scheme.holdingId == holdingId) {
                scheme.copy(isSelected = !scheme.isSelected)
            } else {
                scheme
            }
        }
        _uiState.update { it.copy(portfolioDataState = UiState.Success(updated)) }
    }

    private fun mappedHoldingIds(): Set<String> {
        val details = _uiState.value.goalDetailsState
        if (details !is UiState.Success) return emptySet()
        return details.data.holdings.map { it.holdingId }.toSet()
    }
}

fun GoalDomain.toMapSchemeUiData(): MapSchemeUiData = MapSchemeUiData(
    goalId = id,
    goalName = displayName,
    holdings = holdings,
    totalCurrentValue = totalHoldingsValue
)
