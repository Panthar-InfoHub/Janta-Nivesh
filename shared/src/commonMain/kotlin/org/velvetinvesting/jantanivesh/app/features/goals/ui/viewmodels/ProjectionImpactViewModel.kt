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
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.features.goals.data.mapper.toBody
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalSchemeDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.GoalsRepository
import org.velvetinvesting.jantanivesh.app.features.goals.domain.usecases.GetPortfolioUseCase
import org.velvetinvesting.jantanivesh.app.features.goals.utils.GoalCalculator
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetAllBundledFundsUseCase
import kotlin.math.pow

data class ProjectionImpactUiData(
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
    data class UnMapGoal(val goalId: Int) : ProjectionImpactEvent
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
                        it.copy(goalDetailsState = UiState.Success(deriveProjectionData(goal)))
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

    private fun unMapGoal(goalId: Int) {
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

    private fun deriveProjectionData(goal: GoalDomain): ProjectionImpactUiData {
        if (goal.goalTypeId == 3) {
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
            val targetYear = DateTimeUtils.getCurrentYear() + yearsLeft

            return ProjectionImpactUiData(
                goalItemName = goal.goalItemName ?: goal.goalName ?: "Retirement",
                goalName = goal.goalName ?: "Retirement",
                todaysCost = currentMonthlyExpense.toLong(),
                futureValue = retirementCorpus,
                targetYear = targetYear,
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
            } else 1.0

            val monthlySip = if (denominator > 0) numerator / denominator else 0.0
            val progress = if (targetAmount > 0) currentSaved.toDouble() / targetAmount else 0.0
            val timeFactor = (yearsLeft.toDouble() / 30.0).coerceIn(0.0, 1.0)
            val feasibilityScore = (progress * 0.7 + timeFactor * 0.3).coerceIn(0.1, 1.0).toFloat()
            val targetYear = DateTimeUtils.getCurrentYear() + yearsLeft

            return ProjectionImpactUiData(
                goalItemName = goal.goalItemName ?: goal.goalName ?: "Goal",
                todaysCost = todaysCost,
                futureValue = futureValue,
                targetYear = targetYear,
                monthlySip = monthlySip,
                feasibilityScore = feasibilityScore,
                currentSaved = currentSaved,
                targetAmount = targetAmount,
                increasedBy = futureValue - todaysCost,
                requiredMonthly = monthlySip,
                schemes = goal.schemes,
                goalId = goal.goalId,
                goalName = goal.goalName ?: "Goal",
                goalTypeId = goal.goalTypeId
            )
        }
    }
}