package org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalSchemeDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.GoalsRepository
import org.velvetinvesting.jantanivesh.app.features.goals.utils.GoalCalculator
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.UserDataRepo
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import kotlin.math.pow

data class ProjectedImpactUiData(
    val goalItemName: String,
    val todaysCost: Long,
    val futureValue: Double,
    val targetYear: Int,
    val monthlySip: Double,
    val feasibilityScore: Float,
    val currentSaved: Long,
    val targetAmount: Long,
    val increasedBy: Double,
    val requiredMonthly: Double,
    val schemes: List<GoalSchemeDomain>,
    val goalId: Int,
    val goalName: String,
    val goalTypeId: Int?
)

data class SelectableSchemeUiModel(
    val schemeId: Int,
    val name: String,
    val units: String,
    val value: Double,
    val isSelected: Boolean,
    val folio: String
)

sealed interface ProjectedImpactEvent {
    data object OnBackClicked : ProjectedImpactEvent
    data object OnInvestNowClicked : ProjectedImpactEvent
    data object LoadGoalDetails : ProjectedImpactEvent
    data object OpenPortfolioSelector : ProjectedImpactEvent
    data object ClosePortfolioSelector : ProjectedImpactEvent
    data class ToggleSchemeSelection(val schemeId: Int) : ProjectedImpactEvent
    data object MapSelectedSchemes : ProjectedImpactEvent
    data class UnMapScheme(val schemeId: Int) : ProjectedImpactEvent
    data object DeleteGoal : ProjectedImpactEvent
}

sealed interface ProjectedImpactEffect {
    data object NavigateBack : ProjectedImpactEffect
    data object NavigateToInvest : ProjectedImpactEffect
    data object OpenPortfolioBottomSheet : ProjectedImpactEffect
    data object ClosePortfolioBottomSheet : ProjectedImpactEffect
    data class ShowError(val message: String) : ProjectedImpactEffect
}

class ProjectedImpactViewModel(
    val id: String,
    private val goalsRepository: GoalsRepository,
    private val userDataRepo: UserDataRepo
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ProjectedImpactUiData>>(UiState.Loading)
    val uiState: StateFlow<UiState<ProjectedImpactUiData>> = _uiState.asStateFlow()

    private val _effect = Channel<ProjectedImpactEffect>()
    val effect = _effect.receiveAsFlow()

    private val _portfolioData = MutableStateFlow<UiState<List<SelectableSchemeUiModel>>>(UiState.Loading)
    val portfolioData = _portfolioData.asStateFlow()

    init {
        loadGoalDetails(id)
    }

    fun handleEvent(event: ProjectedImpactEvent) {
        when (event) {
            ProjectedImpactEvent.OnBackClicked -> sendEffect(ProjectedImpactEffect.NavigateBack)
            ProjectedImpactEvent.OnInvestNowClicked -> sendEffect(ProjectedImpactEffect.NavigateToInvest)
            ProjectedImpactEvent.LoadGoalDetails -> loadGoalDetails(id)
            ProjectedImpactEvent.OpenPortfolioSelector -> {
                sendEffect(ProjectedImpactEffect.OpenPortfolioBottomSheet)
                loadPortfolio()
            }
            ProjectedImpactEvent.ClosePortfolioSelector -> sendEffect(ProjectedImpactEffect.ClosePortfolioBottomSheet)
            is ProjectedImpactEvent.ToggleSchemeSelection -> toggleSchemeSelection(event.schemeId)
            ProjectedImpactEvent.MapSelectedSchemes -> mapSchemes()
            is ProjectedImpactEvent.UnMapScheme -> unMapScheme(event.schemeId)
            ProjectedImpactEvent.DeleteGoal -> deleteGoal()
        }
    }

    private fun loadGoalDetails(goalId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            goalsRepository.getGoalById(goalId)
                .onSuccess { goal ->
                    _uiState.value = UiState.Success(deriveProjectionData(goal))
                }
                .onError { error ->
                    _uiState.value = UiState.Error(error.message)
                }
        }
    }

    private fun deriveProjectionData(goal: GoalDomain): ProjectedImpactUiData {
        val currentYear = 2024 

        if (goal.goalTypeId == 3) { // Retirement
            val currentAge = goal.currentAge ?: 0
            val retirementAge = goal.retirementAge ?: 60
            val lifeExpectancy = goal.lifeExpectancy ?: 85
            val yearsLeft = retirementAge - currentAge
            val currentMonthlyExpense = goal.currentMonthlyExpense?.toDoubleOrNull() ?: 0.0
            val inflationRate = goal.inflationRate / 100.0
            val preRetirementReturn = goal.returnRate / 100.0
            val postRetirementReturn = goal.postRetirementReturn?.toDoubleOrNull()?.div(100.0) ?: 0.06

            val retirementCorpus = GoalCalculator.calculateRetirementCorpus(
                currentMonthlyExpense = currentMonthlyExpense,
                inflationRate = inflationRate,
                returnRate = postRetirementReturn,
                yearsToRetirement = yearsLeft,
                yearsPostRetirement = lifeExpectancy - retirementAge
            )

            val monthlySip = GoalCalculator.calculateRetirementSip(
                retirementCorpus = retirementCorpus,
                annualReturnRate = preRetirementReturn,
                yearsToRetirement = yearsLeft
            )

            val currentSaved = goal.currentSavedAmount.toLongOrNull() ?: 0L
            val targetAmount = retirementCorpus.toLong()
            val progress = if (targetAmount > 0) currentSaved.toDouble() / targetAmount else 0.0
            val timeFactor = (yearsLeft.toDouble() / 30.0).coerceIn(0.0, 1.0)
            val feasibilityScore = (progress * 0.7 + timeFactor * 0.3).coerceIn(0.1, 1.0).toFloat()

            return ProjectedImpactUiData(
                goalItemName = goal.goalItemName ?: goal.goalName ?: "Retirement",
                goalName = goal.goalName ?: "Retirement",
                todaysCost = currentMonthlyExpense.toLong(),
                futureValue = retirementCorpus,
                targetYear = currentYear + yearsLeft,
                monthlySip = monthlySip,
                feasibilityScore = feasibilityScore,
                currentSaved = currentSaved,
                targetAmount = targetAmount,
                increasedBy = retirementCorpus - currentMonthlyExpense,
                requiredMonthly = monthlySip,
                schemes = goal.schemes,
                goalId = goal.goalId,
                goalTypeId = goal.goalTypeId
            )
        } else {
            val todaysCost = goal.currentGoalCost?.toLong() ?: 0L
            val yearsLeft = goal.yearsLeft ?: 0
            val inflationRate = goal.inflationRate / 100.0
            val returnRate = goal.returnRate / 100.0

            val futureValue = todaysCost * (1 + inflationRate).pow(yearsLeft.toDouble())
            val currentSaved = goal.currentSavedAmount.toLongOrNull() ?: 0L
            val targetAmount = futureValue.toLong()

            val monthlyReturnRate = returnRate / 12
            val totalMonths = yearsLeft * 12
            val numerator = futureValue - (currentSaved * (1 + returnRate).pow(yearsLeft.toDouble()))
            val denominator = if (totalMonths > 0) {
                ((1 + monthlyReturnRate).pow(totalMonths.toDouble()) - 1) / monthlyReturnRate
            } else {
                1.0
            }

            val monthlySip = if (denominator > 0) numerator / denominator else 0.0
            val progress = if (targetAmount > 0) currentSaved.toDouble() / targetAmount else 0.0
            val timeFactor = (yearsLeft.toDouble() / 30.0).coerceIn(0.0, 1.0)
            val feasibilityScore = (progress * 0.7 + timeFactor * 0.3).coerceIn(0.1, 1.0).toFloat()

            return ProjectedImpactUiData(
                goalItemName = goal.goalItemName ?: goal.goalName ?: "Goal",
                goalName = goal.goalName ?: "Goal",
                todaysCost = todaysCost,
                futureValue = futureValue,
                targetYear = currentYear + yearsLeft,
                monthlySip = monthlySip,
                feasibilityScore = feasibilityScore,
                currentSaved = currentSaved,
                targetAmount = targetAmount,
                increasedBy = futureValue - todaysCost,
                requiredMonthly = monthlySip,
                schemes = goal.schemes,
                goalId = goal.goalId,
                goalTypeId = goal.goalTypeId
            )
        }
    }

    private fun loadPortfolio() {
        _portfolioData.value = UiState.Loading
        viewModelScope.launch {
            // Placeholder: Velvet used a dedicated endpoint /user/portfolio
            // For now, we'll just emit an empty success state or error
            // until we have the repository method.
            _portfolioData.value = UiState.Success(emptyList())
        }
    }

    private fun toggleSchemeSelection(schemeId: Int) {
        val currentState = _portfolioData.value
        if (currentState !is UiState.Success) return
        _portfolioData.value = UiState.Success(
            currentState.data.map { scheme ->
                if (scheme.schemeId == schemeId) scheme.copy(isSelected = !scheme.isSelected) else scheme
            }
        )
    }

    private fun mapSchemes() {
        val selected = (_portfolioData.value as? UiState.Success)?.data?.filter { it.isSelected }
        if (selected.isNullOrEmpty()) return
        
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            // Placeholder: goalsRepository.mapGoal(...) 
            loadGoalDetails(id)
            AppEventsController.sendGoalRefreshEvent()
        }
    }

    private fun unMapScheme(schemeId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            // Placeholder: goalsRepository.unMapGoal(...)
            loadGoalDetails(id)
            AppEventsController.sendGoalRefreshEvent()
        }
    }

    private fun deleteGoal() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            goalsRepository.deleteGoal(id)
                .onSuccess {
                    AppEventsController.sendGoalRefreshEvent()
                    sendEffect(ProjectedImpactEffect.NavigateBack)
                }
                .onError { error ->
                    // Re-derive the data to restore state
                    loadGoalDetails(id) 
                    sendEffect(ProjectedImpactEffect.ShowError(error.message))
                }
        }
    }

    private fun sendEffect(effect: ProjectedImpactEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
