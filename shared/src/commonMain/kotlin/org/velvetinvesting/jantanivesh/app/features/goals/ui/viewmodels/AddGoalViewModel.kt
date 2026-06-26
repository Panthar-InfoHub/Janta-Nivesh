package org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.UserDataRepo
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalOption
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalRequest
import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.GoalsRepository
import kotlin.math.max
import kotlin.time.Instant

data class AddGoalUiState(
    val form: GoalFormState = GoalFormState(),
    val preview: GoalRequest? = null,
    val isValid: Boolean = false,
    val currentAge: Int = 0,
    val dob: Long = 0
)

data class GoalFormState(
    val selectedOption: GoalOption? = null,
    val childName: String = "",
    val childAge: String = "",
    val goalCost: String = "",
    val targetYear: String = "",
    val retirementAge: String = "60",
    val lifeExpectancy: String = "90",
    val monthlyExpense: String = "",
    val postReturn: String = "",
    val goalName: String = "",
    val goalItemName: String = "",
    val inflation: String = "8",
    val returns: String = "10"
)

sealed interface AddGoalEvent {
    data object OnBackClicked : AddGoalEvent
    data class OnOptionSelected(val option: GoalOption) : AddGoalEvent
    data class UpdateForm(val update: GoalFormState.() -> GoalFormState) : AddGoalEvent
    data object OnSaveGoalClicked : AddGoalEvent
}

sealed interface AddGoalEffect {
    data object NavigateBack : AddGoalEffect
    data class ShowError(val message: String) : AddGoalEffect
}

class AddGoalViewModel(
    private val goalsRepository: GoalsRepository,
    private val userDataRepo: UserDataRepo
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<AddGoalUiState>>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _effect = Channel<AddGoalEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            val response = userDataRepo.getUserData()
            if (response is NetworkResponse.Success) {
                val data = response.data
                val finance = data.userFinance

                val monthlyExpense = finance?.let {
                    (it.expense_others.toLongOrNull() ?: 0L) +
                    (it.expense_food.toLongOrNull() ?: 0L) +
                    (it.expense_house.toLongOrNull() ?: 0L) +
                    (it.expense_transportation.toLongOrNull() ?: 0L)
                } ?: 0L

                val age = getAgeFromDob(data.dob)
                
                _state.value = UiState.Success(
                    AddGoalUiState(
                        form = GoalFormState(
                            monthlyExpense = monthlyExpense.toString(),
                            retirementAge = "60",
                            lifeExpectancy = "90"
                        ),
                        currentAge = age,
                        dob = dobToEpochMillis(data.dob)
                    )
                )
            } else if (response is NetworkResponse.Error) {
                _state.value = UiState.Error(response.error.message)
            }
        }
    }

    private fun getAgeFromDob(dob: String): Int {
        return try {
            val instant = Instant.parse(dob)
            val birthDate = instant.toLocalDateTime(TimeZone.UTC).date
            val today = DateTimeUtils.today(TimeZone.UTC)
            var age = today.year - birthDate.year
            if (today.month.number < birthDate.month.number || (today.month.number == birthDate.month.number && today.day < birthDate.day)) {
                age--
            }
            age
        } catch (e: Exception) {
            0
        }
    }

    private fun dobToEpochMillis(dob: String): Long {
        return try {
            kotlin.time.Instant.parse(dob).toEpochMilliseconds()
        } catch (e: Exception) {
            0L
        }
    }

    fun handleEvent(event: AddGoalEvent) {
        when (event) {
            AddGoalEvent.OnBackClicked -> sendEffect(AddGoalEffect.NavigateBack)
            is AddGoalEvent.OnOptionSelected -> {
                val current = (_state.value as? UiState.Success)?.data ?: return
                val newForm = GoalFormState(
                    selectedOption = event.option,
                    goalName = event.option.title,
                    goalItemName = event.option.goalItemName ?: event.option.title,
                    retirementAge = "60",
                    lifeExpectancy = "90",
                    inflation = "8",
                    returns = "10"
                )
                _state.value = UiState.Success(
                    current.copy(
                        form = newForm,
                        preview = null,
                        isValid = false
                    )
                )
            }
            is AddGoalEvent.UpdateForm -> {
                val current = (_state.value as? UiState.Success)?.data ?: return
                val newForm = current.form.run(event.update)
                val preview = createPreview(newForm, current.currentAge)
                _state.value = UiState.Success(
                    current.copy(
                        form = newForm,
                        preview = preview,
                        isValid = preview != null
                    )
                )
            }
            AddGoalEvent.OnSaveGoalClicked -> {
                saveGoal()
            }
        }
    }

    private fun createPreview(
        form: GoalFormState,
        currentAge: Int
    ): GoalRequest? {
        val option = form.selectedOption ?: return null
        val inflation = form.inflation.toIntOrNull() ?: return null
        val returns = form.returns.toIntOrNull() ?: return null
        val currentYear = DateTimeUtils.getCurrentYear()

        return when (option.type) {
            GoalType.ChildEducation -> {
                val age = form.childAge.toIntOrNull()
                val year = form.targetYear.toIntOrNull()
                val cost = form.goalCost.replace(",", "").toLongOrNull()
                if (form.childName.isBlank() || age == null || year == null || cost == null) return null
                GoalRequest.ChildEducation(
                    childName = form.childName,
                    childAge = age,
                    yearsToGoal = max(0, year - currentYear),
                    currentGoalCost = cost,
                    inflationRate = inflation,
                    returnRate = returns,
                    currentSavedAmount = 0,
                    title = option.title
                )
            }
            GoalType.ChildMarriage -> {
                val age = form.childAge.toIntOrNull()
                val year = form.targetYear.toIntOrNull()
                val cost = form.goalCost.replace(",", "").toLongOrNull()
                if (form.childName.isBlank() || age == null || year == null || cost == null) return null
                GoalRequest.ChildMarriage(
                    childName = form.childName,
                    childAge = age,
                    yearsToGoal = max(0, year - currentYear),
                    currentGoalCost = cost,
                    inflationRate = inflation,
                    returnRate = returns,
                    currentSavedAmount = 0,
                    title = option.title
                )
            }
            GoalType.Retirement -> {
                val retirementAge = form.retirementAge.toIntOrNull()
                val life = form.lifeExpectancy.toIntOrNull()
                val expense = form.monthlyExpense.replace(",", "").toLongOrNull()
                val post = form.postReturn.toIntOrNull()
                if (retirementAge == null || life == null || expense == null || post == null) return null
                if (retirementAge <= currentAge || life <= retirementAge) return null
                GoalRequest.Retirement(
                    currentAge = currentAge,
                    retirementAge = retirementAge,
                    lifeExpectancy = life,
                    currentMonthlyExpense = expense,
                    postRetirementReturn = post,
                    inflationRate = inflation,
                    returnRate = returns,
                    currentSavedAmount = 0,
                    yearsToGoal = retirementAge - currentAge,
                    title = option.title
                )
            }
            GoalType.WealthBuilding -> {
                val cost = form.goalCost.replace(",", "").toLongOrNull()
                val year = form.targetYear.toIntOrNull()
                if (form.goalName.isBlank() || cost == null || year == null) return null
                GoalRequest.WealthBuildingGoal(
                    goalName = form.goalName,
                    goalItemId = option.goalItemId ?: 1,
                    goalItemName = form.goalItemName,
                    yearsToGoal = max(0, year - currentYear),
                    currentGoalCost = cost,
                    inflationRate = inflation,
                    returnRate = returns,
                    currentSavedAmount = 0,
                    title = option.title
                )
            }
        }
    }

    private fun saveGoal() {
        val current = (_state.value as? UiState.Success)?.data ?: return
        val goal = current.preview ?: return

        viewModelScope.launch {
            _loading.value = true
            val response = when (goal) {
                is GoalRequest.ChildEducation -> goalsRepository.addChildEducationGoal(goal)
                is GoalRequest.ChildMarriage -> goalsRepository.addChildMarriageGoal(goal)
                is GoalRequest.Retirement -> goalsRepository.addRetirementGoal(goal)
                is GoalRequest.WealthBuildingGoal -> goalsRepository.addWealthBuildingGoal(goal)
            }

            _loading.value = false
            when (response) {
                is NetworkResponse.Success -> sendEffect(AddGoalEffect.NavigateBack)
                is NetworkResponse.Error -> sendEffect(AddGoalEffect.ShowError(response.error.message))
            }
        }
    }

    private fun sendEffect(effect: AddGoalEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
