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
import org.velvetinvesting.jantanivesh.app.features.goals.data.mapper.toBody
import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.GoalsRepository
import org.velvetinvesting.jantanivesh.app.features.goals.domain.usecases.GetPortfolioUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetAllBundledFundsUseCase

/**
 * The Map Schemes screen shows the same projection the details screen does, so it reads the one
 * [ProjectedImpactUiData] rather than keeping a second copy of those fields in step.
 */
typealias ProjectionImpactUiData = ProjectedImpactUiData

data class ProjectionImpactUiState(
    val goalDetailsState: UiState<ProjectionImpactUiData> = UiState.Loading,
    val bundleDataState: UiState<List<BundledMutualFundDomain>> = UiState.Loading,
    val portfolioDataState: UiState<List<SelectableSchemeUiModel>> = UiState.Loading
)

sealed interface ProjectionImpactEvent {
    object LoadData : ProjectionImpactEvent
    object RetryGoalDetails : ProjectionImpactEvent
    object RetryPortfolio : ProjectionImpactEvent
    object OpenBottomSheet : ProjectionImpactEvent
    object CloseBottomSheet : ProjectionImpactEvent
    object MapGoal : ProjectionImpactEvent
    data class UnMapGoal(val goalId: String) : ProjectionImpactEvent
    data class ToggleSelection(val schemeId: Int) : ProjectionImpactEvent
}

sealed interface ProjectionImpactEffect {
    object OpenBottomSheet : ProjectionImpactEffect
    object CloseBottomSheet : ProjectionImpactEffect
}

class ProjectionImpactViewModel(
    private val getAllBundledFundsUseCase: GetAllBundledFundsUseCase,
    private val goalsRepository: GoalsRepository,
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val goalId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectionImpactUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ProjectionImpactEffect>()
    val effect = _effect.asSharedFlow()

    init {
        handleEvent(ProjectionImpactEvent.LoadData)
    }

    fun handleEvent(event: ProjectionImpactEvent) {
        when (event) {
            ProjectionImpactEvent.LoadData -> loadData()
            ProjectionImpactEvent.RetryGoalDetails -> loadGoalDetails()
            ProjectionImpactEvent.RetryPortfolio -> loadPortfolio()
            ProjectionImpactEvent.OpenBottomSheet -> openBottomSheet()
            ProjectionImpactEvent.CloseBottomSheet -> closeBottomSheet()
            ProjectionImpactEvent.MapGoal -> mapGoal()
            is ProjectionImpactEvent.UnMapGoal -> unMapGoal(event.goalId)
            is ProjectionImpactEvent.ToggleSelection -> toggleSelection(event.schemeId)
        }
    }

    private fun loadData() {
        loadGoalDetails()
        loadBundles()
    }

    private fun loadGoalDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(goalDetailsState = UiState.Loading) }
            goalsRepository.getGoalById(goalId)
                .onSuccess { goal ->
                    _uiState.update {
                        it.copy(goalDetailsState = UiState.Success(goal.toUiData()))
                    }
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
                    _uiState.update { it.copy(goalDetailsState = UiState.Error(error.message)) }
                }
        }
    }

    private fun loadBundles() {
        viewModelScope.launch {
            _uiState.update { it.copy(bundleDataState = UiState.Loading) }
            getAllBundledFundsUseCase(page = 1, limit = 4)
                .onSuccess { data ->
                    _uiState.update { it.copy(bundleDataState = UiState.Success(data)) }
                }
                .onError { error ->
                    _uiState.update { it.copy(bundleDataState = UiState.Error(error.message)) }
                }
        }
    }

    private fun loadPortfolio() {
        viewModelScope.launch {
            _uiState.update { it.copy(portfolioDataState = UiState.Loading) }
            getPortfolioUseCase()
                .onSuccess { response ->
                    val schemes = response.mutualFunds.map {
                        SelectableSchemeUiModel(
                            schemeId = it.schemeId,
                            name = it.title,
                            units = it.balanceUnits.toString(),
                            value = it.amount,
                            folio = it.folio,
                            isSelected = false
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
            _effect.emit(ProjectionImpactEffect.OpenBottomSheet)
            loadPortfolio()
        }
    }

    private fun closeBottomSheet() {
        viewModelScope.launch {
            _effect.emit(ProjectionImpactEffect.CloseBottomSheet)
        }
    }

    private fun mapGoal() {
        val currentState = _uiState.value
        val goalState = currentState.goalDetailsState
        val portfolioState = currentState.portfolioDataState

        if (goalState !is UiState.Success || portfolioState !is UiState.Success) return

        viewModelScope.launch {
            closeBottomSheet()
            _uiState.update { it.copy(goalDetailsState = UiState.Loading) }

            val selectedSchemes = portfolioState.data.filter { it.isSelected }
            val body = selectedSchemes.toBody(goalState.data.goalId)

            goalsRepository.mapGoal(body)
                .onSuccess {
                    loadGoalDetails()
                    AppEventsController.sendGoalRefreshEvent()
                }
                .onError { error ->
                    _uiState.update { it.copy(goalDetailsState = UiState.Success(goalState.data)) }
                    SnackBarController.showError(error.message)
                }
        }
    }

    private fun unMapGoal(goalId: String) {
        val currentGoalDetails = _uiState.value.goalDetailsState
        viewModelScope.launch {
            _uiState.update { it.copy(goalDetailsState = UiState.Loading) }
            goalsRepository.unMapGoal(goalId)
                .onSuccess {
                    loadGoalDetails()
                    AppEventsController.sendGoalRefreshEvent()
                }
                .onError { error ->
                    _uiState.update { it.copy(goalDetailsState = currentGoalDetails) }
                    SnackBarController.showError(error.message)
                }
        }
    }

    private fun toggleSelection(id: Int) {
        val portfolioState = _uiState.value.portfolioDataState
        if (portfolioState !is UiState.Success) return

        val updatedList = portfolioState.data.map { scheme ->
            if (scheme.schemeId == id) {
                scheme.copy(isSelected = !scheme.isSelected)
            } else {
                scheme
            }
        }
        _uiState.update { it.copy(portfolioDataState = UiState.Success(updatedList)) }
    }

}
