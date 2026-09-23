package org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalHoldingDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.GoalsRepository

/**
 * One goal's projection, as `GET /user-goal/{id}` reports it. Every figure here is the server's
 * own: the `v2.0` engine sized the goal when it was created, and re-deriving the numbers on the
 * client would only introduce a second answer.
 */
data class ProjectedImpactUiData(
    val goalId: String,
    val goalTypeId: Int,
    /** The goal's name, and the type it belongs to, for the header. */
    val goalName: String,
    val goalTypeName: String,
    /** The present-day figure the goal was sized from — a cost, or a chosen corpus. */
    val todaysCost: Double,
    val futureValue: Double,
    val currentSavings: Double,
    val fvCurrentSavings: Double,
    val netRequiredCorpus: Double,
    val monthlySip: Double,
    val lumpsumToday: Double,
    val yearsRemaining: Int,
    val targetYear: Int?,
    val progressPercent: Int,
    val feasibilityScore: Float,
    /** How much inflation adds between today's figure and the target. */
    val increasedBy: Double,
    /** True for "Build My Savings", where the target was named rather than inflated. */
    val isFixedCorpus: Boolean,
    val holdings: List<GoalHoldingDomain>
)

sealed interface ProjectedImpactEvent {
    data object OnBackClicked : ProjectedImpactEvent
    data object OnInvestNowClicked : ProjectedImpactEvent
    data object LoadGoalDetails : ProjectedImpactEvent
    data object OnMapSchemesClick : ProjectedImpactEvent
    data object DeleteGoal : ProjectedImpactEvent
}

sealed interface ProjectedImpactEffect {
    data object NavigateBack : ProjectedImpactEffect
    data object NavigateToInvest : ProjectedImpactEffect
    data object NavigateToMapScheme : ProjectedImpactEffect
    data class ShowError(val message: String) : ProjectedImpactEffect
}

class ProjectedImpactViewModel(
    val id: String,
    private val goalsRepository: GoalsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ProjectedImpactUiData>>(UiState.Loading)
    val uiState: StateFlow<UiState<ProjectedImpactUiData>> = _uiState.asStateFlow()

    /** Kept apart from [uiState] so deleting shows a loader on the button, not an empty screen. */
    private val _deleting = MutableStateFlow(false)
    val deleting: StateFlow<Boolean> = _deleting.asStateFlow()

    private val _effect = Channel<ProjectedImpactEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadGoalDetails()
    }

    fun handleEvent(event: ProjectedImpactEvent) {
        when (event) {
            ProjectedImpactEvent.OnBackClicked -> sendEffect(ProjectedImpactEffect.NavigateBack)
            ProjectedImpactEvent.OnInvestNowClicked -> sendEffect(ProjectedImpactEffect.NavigateToInvest)
            ProjectedImpactEvent.LoadGoalDetails -> loadGoalDetails()
            ProjectedImpactEvent.DeleteGoal -> deleteGoal()
            ProjectedImpactEvent.OnMapSchemesClick -> sendEffect(ProjectedImpactEffect.NavigateToMapScheme)
        }
    }

    private fun loadGoalDetails() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            goalsRepository.getGoalById(id)
                .onSuccess { goal ->
                    _uiState.value = UiState.Success(goal.toUiData())
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
                    _uiState.value = UiState.Error(error.message)
                }
        }
    }

    private fun deleteGoal() {
        if (_deleting.value) return

        viewModelScope.launch {
            _deleting.value = true
            goalsRepository.deleteGoal(id)
                .onSuccess {
                    _deleting.value = false
                    SnackBarController.showSuccess("Goal deleted")
                    AppEventsController.sendGoalRefreshEvent()
                    sendEffect(ProjectedImpactEffect.NavigateBack)
                }
                .onError { error ->
                    _deleting.value = false
                    SnackBarController.showError(error.message)
                }
        }
    }

    private fun sendEffect(effect: ProjectedImpactEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}

fun GoalDomain.toUiData(): ProjectedImpactUiData = ProjectedImpactUiData(
    goalId = id,
    goalTypeId = goalTypeId,
    goalName = displayName,
    goalTypeName = goalType?.displayName ?: "Goal",
    todaysCost = baseAmount,
    futureValue = futureTargetAmount,
    currentSavings = currentSavings,
    fvCurrentSavings = fvCurrentSavings,
    netRequiredCorpus = netRequiredCorpus,
    monthlySip = requiredMonthlySip,
    lumpsumToday = requiredLumpsumToday,
    yearsRemaining = yearsRemaining,
    targetYear = targetYear,
    progressPercent = progressPercent,
    feasibilityScore = feasibilityScore(),
    increasedBy = (futureTargetAmount - baseAmount).coerceAtLeast(0.0),
    isFixedCorpus = goalType?.usesTargetAmount == true,
    holdings = holdings
)

/**
 * How much of the target the money already put aside will have grown into by the target date —
 * which is what "feasibility" means here, and is a figure the server has already worked out
 * (`fv_current_savings` against `future_target_amount`).
 */
private fun GoalDomain.feasibilityScore(): Float {
    if (futureTargetAmount <= 0.0) return (progressPercent / 100f).coerceIn(0f, 1f)
    return (fvCurrentSavings / futureTargetAmount).coerceIn(0.0, 1.0).toFloat()
}
