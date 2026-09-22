package org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.core.utils.trimTo
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.CreateGoalRequest
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalCalculationDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalOption
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.toCalculationRequest
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.toOption
import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.GoalsRepository

data class AddGoalUiState(
    /** The goal types on offer, from `GET /user-goal/config`. */
    val options: List<GoalOption> = emptyList(),
    val form: GoalFormState = GoalFormState(),
    /** The server's projection for what is typed so far; null until the form is complete. */
    val projection: GoalCalculationDomain? = null,
    val projecting: Boolean = false,
    val projectionError: String? = null,
    val isValid: Boolean = false
)

/**
 * The create-goal form. One shape serves all six types — which fields are shown, and which of
 * [amount]'s two meanings applies, follows from the selected option's type.
 */
data class GoalFormState(
    val selectedOption: GoalOption? = null,
    val goalName: String = "",
    val childName: String = "",
    val childAge: String = "",
    val assetSubtype: String? = null,
    val years: String = "",
    /** The present-day cost, or for "Build My Savings" the corpus asked for. */
    val amount: String = "",
    val currentSavings: String = "",
    /** Held as percents for display; sent as fractions. Blank means "use the type's default". */
    val inflation: String = "",
    val expectedReturn: String = ""
)

sealed interface AddGoalEvent {
    data object OnBackClicked : AddGoalEvent
    data object LoadData : AddGoalEvent
    data class OnOptionSelected(val option: GoalOption) : AddGoalEvent
    data class UpdateForm(val update: GoalFormState.() -> GoalFormState) : AddGoalEvent
    data object OnSaveGoalClicked : AddGoalEvent
}

sealed interface AddGoalEffect {
    data object NavigateBack : AddGoalEffect
    data class ShowError(val message: String) : AddGoalEffect
}

/**
 * Drives goal creation against the `v2.0` endpoints: `/user-goal/config` says what to ask for,
 * `/user-goal/calculate` previews the result as the user types, and `/user-goal/` commits it.
 *
 * The projection is never computed locally — previewing with client-side arithmetic would show a
 * SIP figure the saved goal then disagrees with.
 */
class AddGoalViewModel(
    private val goalsRepository: GoalsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<AddGoalUiState>>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _effect = Channel<AddGoalEffect>()
    val effect = _effect.receiveAsFlow()

    /** Keeps the form one projection ahead of the user's typing rather than one per keystroke. */
    private var projectionJob: Job? = null

    init {
        loadConfig()
    }

    fun handleEvent(event: AddGoalEvent) {
        when (event) {
            AddGoalEvent.OnBackClicked -> sendEffect(AddGoalEffect.NavigateBack)
            AddGoalEvent.LoadData -> loadConfig()
            is AddGoalEvent.OnOptionSelected -> selectOption(event.option)
            is AddGoalEvent.UpdateForm -> updateForm(event.update)
            AddGoalEvent.OnSaveGoalClicked -> saveGoal()
        }
    }

    private fun loadConfig() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            when (val response = goalsRepository.getGoalConfig()) {
                is NetworkResponse.Error -> _state.value = UiState.Error(response.error.message)
                is NetworkResponse.Success -> _state.value = UiState.Success(
                    AddGoalUiState(options = response.data.map { it.toOption() })
                )
            }
        }
    }

    /**
     * Picking a type restarts the form: the fields it asks for, and the rates it defaults to, are
     * the new type's, so carrying over what was typed for the previous one would mislead.
     */
    private fun selectOption(option: GoalOption) {
        val current = currentData() ?: return
        projectionJob?.cancel()
        _state.value = UiState.Success(
            current.copy(
                form = GoalFormState(
                    selectedOption = option,
                    assetSubtype = option.assetSubtypes.firstOrNull(),
                    inflation = option.inflationRate.toPercentField(),
                    expectedReturn = option.expectedReturnRate.toPercentField()
                ),
                projection = null,
                projecting = false,
                projectionError = null,
                isValid = false
            )
        )
    }

    private fun updateForm(update: GoalFormState.() -> GoalFormState) {
        val current = currentData() ?: return
        val form = current.form.update()
        val request = form.toRequest()

        _state.value = UiState.Success(
            current.copy(
                form = form,
                isValid = request != null,
                // Drop a projection the inputs no longer describe.
                projection = current.projection.takeIf { request != null },
                projectionError = null
            )
        )

        projectionJob?.cancel()
        if (request == null) return
        projectionJob = viewModelScope.launch {
            delay(PROJECTION_DEBOUNCE_MS)
            _state.value = UiState.Success(
                (currentData() ?: return@launch).copy(projecting = true)
            )
            when (val response = goalsRepository.calculateGoal(request.toCalculationRequest())) {
                is NetworkResponse.Success -> _state.value = UiState.Success(
                    (currentData() ?: return@launch).copy(
                        projection = response.data,
                        projecting = false,
                        projectionError = null
                    )
                )
                // A failed preview leaves the form usable: saving recalculates server-side anyway.
                is NetworkResponse.Error -> _state.value = UiState.Success(
                    (currentData() ?: return@launch).copy(
                        projecting = false,
                        projectionError = response.error.message
                    )
                )
            }
        }
    }

    private fun saveGoal() {
        val request = currentData()?.form?.toRequest() ?: return

        viewModelScope.launch {
            _loading.value = true
            val response = goalsRepository.createGoal(request)
            _loading.value = false
            when (response) {
                is NetworkResponse.Success -> {
                    // The home dashboard holds its own copy of the goal list; tell it to refetch.
                    AppEventsController.sendGoalRefreshEvent()
                    sendEffect(AddGoalEffect.NavigateBack)
                }
                is NetworkResponse.Error -> sendEffect(AddGoalEffect.ShowError(response.error.message))
            }
        }
    }

    private fun currentData(): AddGoalUiState? = (_state.value as? UiState.Success)?.data

    private fun sendEffect(effect: AddGoalEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    private companion object {
        const val PROJECTION_DEBOUNCE_MS = 450L
    }
}

/**
 * The request the form currently describes, or null while it is incomplete — which is also what
 * gates the Save button and the projection call, so the screen cannot ask the server to size a
 * goal it would reject.
 */
fun GoalFormState.toRequest(): CreateGoalRequest? {
    val option = selectedOption ?: return null
    val type = option.type ?: return null

    val years = years.toIntOrNull() ?: return null
    if (years < option.minYears || years > option.maxYears) return null

    val amount = amount.toAmountOrNull() ?: return null
    if (amount <= 0.0) return null

    val savings = currentSavings.takeIf { it.isNotBlank() }?.toAmountOrNull() ?: 0.0
    if (savings < 0.0) return null

    if (type.needsChildDetails) {
        if (childName.isBlank()) return null
        if (childAge.toIntOrNull() == null) return null
    } else if (goalName.isBlank()) {
        return null
    }

    if (type.needsAssetSubtype && assetSubtype.isNullOrBlank()) return null

    return CreateGoalRequest(
        goalType = type,
        goalName = goalName.takeIf { type.needsGoalName },
        childName = childName.takeIf { type.needsChildDetails },
        childAge = childAge.toIntOrNull().takeIf { type.needsChildDetails },
        assetSubtype = assetSubtype.takeIf { type.needsAssetSubtype },
        yearsRemaining = years,
        currentCost = amount.takeIf { !type.usesTargetAmount },
        targetAmount = amount.takeIf { type.usesTargetAmount },
        currentSavings = savings,
        // Sent only where the user moved a rate off the type's configured default.
        inflationRate = inflation.toRateOverride(option.inflationRate),
        expectedReturnRate = expectedReturn.toRateOverride(option.expectedReturnRate)
    )
}

/** Amount fields are typed with grouping separators; the server wants the bare number. */
private fun String.toAmountOrNull(): Double? =
    replace(",", "").trim().takeIf { it.isNotEmpty() }?.toDoubleOrNull()

/** A config rate (0.06) as the percent the form shows (6). Blank when the type has no such rate. */
private fun Double?.toPercentField(): String = this?.let { (it * 100).trimTo(2) } ?: ""

/**
 * A percent field back to a fraction, but only when it differs from [configured] — leaving the
 * field alone has to mean "use the type's own rate", not "pin whatever was prefilled".
 */
private fun String.toRateOverride(configured: Double?): Double? {
    val entered = trim().takeIf { it.isNotEmpty() }?.toDoubleOrNull() ?: return null
    val fraction = entered / 100.0
    if (configured != null && kotlin.math.abs(fraction - configured) < 1e-9) return null
    return fraction
}
