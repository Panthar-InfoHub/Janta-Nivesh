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
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalRequest
import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.GoalsRepository
import org.velvetinvesting.jantanivesh.app.features.goals.utils.GoalCalculator
import kotlin.math.max

data class FinancialGoalUiState(
    val categories: List<GoalType> = listOf(
        GoalType.ChildEducation,
        GoalType.ChildMarriage,
        GoalType.Retirement,
        GoalType.WealthBuilding
    ),
    val selectedCategory: GoalType? = GoalType.ChildEducation,
    val goalName: String = "",
    val targetYear: String = "",
    val targetAmount: String = "",
    val expectedInflationRate: String = "6",
    val expectedReturnRate: String = "12",
    val childName: String = "",
    val childAge: String = "",
    val currentAge: String = "",
    val retirementAge: String = "60",
    val lifeExpectancy: String = "85",
    val postRetirementReturn: String = "8",
    val currentMonthlyExpense: String = "",
    val todayCost: Long = 0,
    val timeHorizon: Int = 0,
    val futureValue: Long = 0,
    val requiredSip: Long = 0,
    val isLoading: Boolean = false
)

sealed interface FinancialGoalEvent {
    data object OnBackClicked : FinancialGoalEvent
    data class OnCategorySelected(val category: GoalType) : FinancialGoalEvent
    data class OnGoalNameChanged(val name: String) : FinancialGoalEvent
    data class OnTargetYearChanged(val year: String) : FinancialGoalEvent
    data class OnTargetAmountChanged(val amount: String) : FinancialGoalEvent
    data class OnInflationRateChanged(val rate: String) : FinancialGoalEvent
    data class OnReturnRateChanged(val rate: String) : FinancialGoalEvent
    data class OnChildNameChanged(val name: String) : FinancialGoalEvent
    data class OnChildAgeChanged(val age: String) : FinancialGoalEvent
    data class OnCurrentAgeChanged(val age: String) : FinancialGoalEvent
    data class OnRetirementAgeChanged(val age: String) : FinancialGoalEvent
    data class OnLifeExpectancyChanged(val age: String) : FinancialGoalEvent
    data class OnPostRetirementReturnChanged(val rate: String) : FinancialGoalEvent
    data class OnCurrentMonthlyExpenseChanged(val amount: String) : FinancialGoalEvent
    data object OnSaveGoalClicked : FinancialGoalEvent
}

sealed interface FinancialGoalEffect {
    data object NavigateBack : FinancialGoalEffect
    data object NavigateToProjectedImpact : FinancialGoalEffect
    data class ShowError(val message: String) : FinancialGoalEffect
}

class FinancialGoalViewModel(
    private val goalsRepository: GoalsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FinancialGoalUiState())
    val uiState: StateFlow<FinancialGoalUiState> = _uiState.asStateFlow()

    private val _effect = Channel<FinancialGoalEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: FinancialGoalEvent) {
        when (event) {
            FinancialGoalEvent.OnBackClicked -> sendEffect(FinancialGoalEffect.NavigateBack)
            is FinancialGoalEvent.OnCategorySelected -> {
                _uiState.update { it.copy(selectedCategory = event.category) }
                calculateProjectedImpact()
            }
            is FinancialGoalEvent.OnGoalNameChanged -> {
                _uiState.update { it.copy(goalName = event.name) }
            }
            is FinancialGoalEvent.OnTargetYearChanged -> {
                _uiState.update { it.copy(targetYear = event.year) }
                calculateProjectedImpact()
            }
            is FinancialGoalEvent.OnTargetAmountChanged -> {
                _uiState.update { it.copy(targetAmount = event.amount) }
                calculateProjectedImpact()
            }
            is FinancialGoalEvent.OnInflationRateChanged -> {
                _uiState.update { it.copy(expectedInflationRate = event.rate) }
                calculateProjectedImpact()
            }
            is FinancialGoalEvent.OnReturnRateChanged -> {
                _uiState.update { it.copy(expectedReturnRate = event.rate) }
                calculateProjectedImpact()
            }
            is FinancialGoalEvent.OnChildNameChanged -> {
                _uiState.update { it.copy(childName = event.name) }
            }
            is FinancialGoalEvent.OnChildAgeChanged -> {
                _uiState.update { it.copy(childAge = event.age) }
                calculateProjectedImpact()
            }
            is FinancialGoalEvent.OnCurrentAgeChanged -> {
                _uiState.update { it.copy(currentAge = event.age) }
                calculateProjectedImpact()
            }
            is FinancialGoalEvent.OnRetirementAgeChanged -> {
                _uiState.update { it.copy(retirementAge = event.age) }
                calculateProjectedImpact()
            }
            is FinancialGoalEvent.OnLifeExpectancyChanged -> {
                _uiState.update { it.copy(lifeExpectancy = event.age) }
                calculateProjectedImpact()
            }
            is FinancialGoalEvent.OnPostRetirementReturnChanged -> {
                _uiState.update { it.copy(postRetirementReturn = event.rate) }
                calculateProjectedImpact()
            }
            is FinancialGoalEvent.OnCurrentMonthlyExpenseChanged -> {
                _uiState.update { it.copy(currentMonthlyExpense = event.amount) }
                calculateProjectedImpact()
            }
            FinancialGoalEvent.OnSaveGoalClicked -> {
                saveGoal()
            }
        }
    }

    private fun calculateProjectedImpact() {
        val state = _uiState.value
        val inflation = state.expectedInflationRate.toIntOrNull() ?: 0
        val returns = state.expectedReturnRate.toIntOrNull() ?: 0

        when (state.selectedCategory) {
            GoalType.ChildEducation, GoalType.ChildMarriage, GoalType.WealthBuilding -> {
                val targetAmount = state.targetAmount.replace(",", "").toLongOrNull() ?: 0L
                val targetYear = state.targetYear.toIntOrNull() ?: 2024
                val currentYear = 2024 
                val years = max(0, targetYear - currentYear)

                val futureValue = GoalCalculator.calculateFutureValue(targetAmount, inflation, years)
                val requiredSip = GoalCalculator.calculateSip(futureValue, returns, years)

                _uiState.update {
                    it.copy(
                        todayCost = targetAmount,
                        timeHorizon = years,
                        futureValue = futureValue.toLong(),
                        requiredSip = requiredSip.toLong()
                    )
                }
            }
            GoalType.Retirement -> {
                val currentAge = state.currentAge.toIntOrNull() ?: 0
                val retirementAge = state.retirementAge.toIntOrNull() ?: 60
                val lifeExpectancy = state.lifeExpectancy.toIntOrNull() ?: 85
                val monthlyExpense = state.currentMonthlyExpense.replace(",", "").toDoubleOrNull() ?: 0.0
                val postReturn = state.postRetirementReturn.toDoubleOrNull() ?: 8.0
                val preReturn = state.expectedReturnRate.toDoubleOrNull() ?: 12.0

                val result = GoalCalculator.calculateRetirementFromInputs(
                    currentAge = currentAge,
                    retirementAge = retirementAge,
                    lifeExpectancy = lifeExpectancy,
                    monthlyExpense = monthlyExpense,
                    inflationRate = inflation / 100.0,
                    postRetirementReturn = postReturn / 100.0,
                    preRetirementReturn = preReturn / 100.0
                )

                _uiState.update {
                    it.copy(
                        todayCost = (monthlyExpense * 12).toLong(),
                        timeHorizon = max(0, retirementAge - currentAge),
                        futureValue = result.retirementCorpus.toLong(),
                        requiredSip = result.sip.toLong()
                    )
                }
            }
            null -> {}
        }
    }

    private fun saveGoal() {
        val state = _uiState.value
        val request = when (state.selectedCategory) {
            GoalType.ChildEducation -> GoalRequest.ChildEducation(
                childName = state.childName,
                childAge = state.childAge.toIntOrNull() ?: 0,
                yearsToGoal = max(0, (state.targetYear.toIntOrNull() ?: 2024) - 2024),
                currentGoalCost = state.targetAmount.replace(",", "").toLongOrNull() ?: 0L,
                inflationRate = state.expectedInflationRate.toIntOrNull() ?: 0,
                returnRate = state.expectedReturnRate.toIntOrNull() ?: 0,
                currentSavedAmount = 0,
                title = state.goalName.ifBlank { "Child Education" }
            )
            GoalType.ChildMarriage -> GoalRequest.ChildMarriage(
                childName = state.childName,
                childAge = state.childAge.toIntOrNull() ?: 0,
                yearsToGoal = max(0, (state.targetYear.toIntOrNull() ?: 2024) - 2024),
                currentGoalCost = state.targetAmount.replace(",", "").toLongOrNull() ?: 0L,
                inflationRate = state.expectedInflationRate.toIntOrNull() ?: 0,
                returnRate = state.expectedReturnRate.toIntOrNull() ?: 0,
                currentSavedAmount = 0,
                title = state.goalName.ifBlank { "Child Marriage" }
            )
            GoalType.Retirement -> GoalRequest.Retirement(
                currentAge = state.currentAge.toIntOrNull() ?: 0,
                retirementAge = state.retirementAge.toIntOrNull() ?: 60,
                lifeExpectancy = state.lifeExpectancy.toIntOrNull() ?: 85,
                currentMonthlyExpense = state.currentMonthlyExpense.replace(",", "").toLongOrNull() ?: 0L,
                postRetirementReturn = state.postRetirementReturn.toIntOrNull() ?: 8,
                inflationRate = state.expectedInflationRate.toIntOrNull() ?: 0,
                returnRate = state.expectedReturnRate.toIntOrNull() ?: 0,
                currentSavedAmount = 0,
                yearsToGoal = max(0, (state.retirementAge.toIntOrNull() ?: 60) - (state.currentAge.toIntOrNull() ?: 0)),
                title = state.goalName.ifBlank { "Retirement" }
            )
            GoalType.WealthBuilding -> GoalRequest.WealthBuildingGoal(
                goalName = state.goalName,
                goalItemId = 4, 
                goalItemName = "Wealth Building",
                yearsToGoal = max(0, (state.targetYear.toIntOrNull() ?: 2024) - 2024),
                currentGoalCost = state.targetAmount.replace(",", "").toLongOrNull() ?: 0L,
                inflationRate = state.expectedInflationRate.toIntOrNull() ?: 0,
                returnRate = state.expectedReturnRate.toIntOrNull() ?: 0,
                currentSavedAmount = 0,
                title = state.goalName.ifBlank { "Wealth Building" }
            )
            null -> return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val response = when (request) {
                is GoalRequest.ChildEducation -> goalsRepository.addChildEducationGoal(request)
                is GoalRequest.ChildMarriage -> goalsRepository.addChildMarriageGoal(request)
                is GoalRequest.Retirement -> goalsRepository.addRetirementGoal(request)
                is GoalRequest.WealthBuildingGoal -> goalsRepository.addWealthBuildingGoal(request)
            }

            _uiState.update { it.copy(isLoading = false) }
            when (response) {
                is NetworkResponse.Success -> sendEffect(FinancialGoalEffect.NavigateToProjectedImpact)
                is NetworkResponse.Error -> sendEffect(FinancialGoalEffect.ShowError(response.error.message))
            }
        }
    }

    private fun sendEffect(effect: FinancialGoalEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
