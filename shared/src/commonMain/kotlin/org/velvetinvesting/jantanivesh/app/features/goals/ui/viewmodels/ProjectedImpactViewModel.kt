package org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.GoalsRepository
import org.velvetinvesting.jantanivesh.app.features.goals.utils.GoalCalculator
import kotlin.math.pow

data class ProjectedImpactUiState(
    val goalItemName: String = "",
    val todayCost: String = "0",
    val futureValue: String = "0",
    val targetYear: String = "",
    val monthlySip: String = "0",
    val feasibilityScore: Float = 0.0f,
    val wealthBuildingStatus: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface ProjectedImpactEvent {
    data object OnBackClicked : ProjectedImpactEvent
    data object OnInvestNowClicked : ProjectedImpactEvent
    data class LoadGoalDetails(val goalId: String) : ProjectedImpactEvent
}

sealed interface ProjectedImpactEffect {
    data object NavigateBack : ProjectedImpactEffect
    data object NavigateToInvest : ProjectedImpactEffect
}

class ProjectedImpactViewModel(
    private val goalsRepository: GoalsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectedImpactUiState())
    val uiState: StateFlow<ProjectedImpactUiState> = _uiState.asStateFlow()

    private val _effect = Channel<ProjectedImpactEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: ProjectedImpactEvent) {
        when (event) {
            ProjectedImpactEvent.OnBackClicked -> sendEffect(ProjectedImpactEffect.NavigateBack)
            ProjectedImpactEvent.OnInvestNowClicked -> sendEffect(ProjectedImpactEffect.NavigateToInvest)
            is ProjectedImpactEvent.LoadGoalDetails -> loadGoalDetails(event.goalId)
        }
    }

    private fun loadGoalDetails(goalId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            goalsRepository.getGoalById(goalId)
                .onSuccess { goal ->
                    val projectionData = deriveProjectionData(goal)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            goalItemName = projectionData.goalItemName,
                            todayCost = projectionData.todaysCost.toString(),
                            futureValue = projectionData.futureValue.toLong().toString(),
                            targetYear = projectionData.targetYear.toString(),
                            monthlySip = projectionData.monthlySip.toLong().toString(),
                            feasibilityScore = projectionData.feasibilityScore,
                            wealthBuildingStatus = "Increased By ₹ ${(projectionData.futureValue - projectionData.todaysCost).toLong()}"
                        )
                    }
                }
                .onError { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun deriveProjectionData(goal: GoalDomain): ProjectionImpactData {
        val currentYear = 2024 // Placeholder, should ideally use a DateTime utility

        if (goal.goalTypeId == 3) { // Retirement
            val currentAge = goal.currentAge ?: 0
            val retirementAge = goal.retirementAge ?: 60
            val lifeExpectancy = goal.lifeExpectancy ?: 85
            val yearsLeft = retirementAge - currentAge
            val currentMonthlyExpense = goal.currentMonthlyExpense?.toDoubleOrNull() ?: 0.0
            val inflationRate = goal.inflationRate / 100.0
            val preRetirementReturn = goal.returnRate / 100.0
            val postRetirementReturn = goal.postRetirementReturn?.toDoubleOrNull()?.div(100.0) ?: 0.08

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

            return ProjectionImpactData(
                goalItemName = goal.goalItemName ?: goal.goalName ?: "Retirement",
                todaysCost = (currentMonthlyExpense * 12).toLong(),
                futureValue = retirementCorpus,
                targetYear = currentYear + yearsLeft,
                monthlySip = monthlySip,
                feasibilityScore = feasibilityScore
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

            return ProjectionImpactData(
                goalItemName = goal.goalItemName ?: goal.goalName ?: "Goal",
                todaysCost = todaysCost,
                futureValue = futureValue,
                targetYear = currentYear + yearsLeft,
                monthlySip = monthlySip,
                feasibilityScore = feasibilityScore
            )
        }
    }

    private fun sendEffect(effect: ProjectedImpactEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}

private data class ProjectionImpactData(
    val goalItemName: String,
    val todaysCost: Long,
    val futureValue: Double,
    val targetYear: Int,
    val monthlySip: Double,
    val feasibilityScore: Float
)
