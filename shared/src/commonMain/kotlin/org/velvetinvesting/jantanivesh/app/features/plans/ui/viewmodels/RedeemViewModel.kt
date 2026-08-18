package org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchasePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.GetPurchasePlansUseCase

enum class RedeemMode(val label: String) {
    AMOUNT("Amount"),
    UNITS("Units"),
    ALL_UNITS("All units")
}

/**
 * One redeemable line, taken straight from `GET /mf-purchase-plan/` and nothing else. Every field
 * that endpoint does not return stays null rather than carrying a placeholder, so the UI can omit
 * what it has no value for instead of showing something invented.
 */
data class RedeemHolding(
    val planId: String,
    /** ISIN — the purchase-plan payload carries no readable scheme name. */
    val scheme: String,
    val folioNumber: String?,
    val availableUnits: String?,
    val currentValue: String?,
    val monthlyAmount: String?,
    val state: String
) {
    /** Nothing can be redeemed from a plan the gateway has not assigned a folio to yet. */
    val isRedeemable: Boolean
        get() = !folioNumber.isNullOrBlank()
}

data class RedeemUiState(
    val holdings: List<RedeemHolding> = emptyList(),
    val selectedPlanId: String? = null,
    val mode: RedeemMode = RedeemMode.ALL_UNITS,
    val amountInput: String = "",
    val unitsInput: String = "",
    val isLoading: Boolean = false,
    val isRedeeming: Boolean = false
) {
    val selectedHolding: RedeemHolding?
        get() = holdings.firstOrNull { it.planId == selectedPlanId } ?: holdings.firstOrNull()

    val isEmpty: Boolean
        get() = holdings.isEmpty()

    /** Gates both the redeem controls and the button. */
    val canRedeemSelected: Boolean
        get() = selectedHolding?.isRedeemable == true

    private val isInputValid: Boolean
        get() = when (mode) {
            RedeemMode.AMOUNT -> (amountInput.toDoubleOrNull() ?: 0.0) > 0.0
            RedeemMode.UNITS -> (unitsInput.toDoubleOrNull() ?: 0.0) > 0.0
            RedeemMode.ALL_UNITS -> true
        }

    val canRedeem: Boolean
        get() = !isLoading && !isRedeeming && canRedeemSelected && isInputValid
}

sealed interface RedeemEvent {
    data class OnHoldingSelected(val planId: String) : RedeemEvent
    data class OnModeSelected(val mode: RedeemMode) : RedeemEvent
    data class OnAmountChange(val value: String) : RedeemEvent
    data class OnUnitsChange(val value: String) : RedeemEvent
    data object OnRedeemClick : RedeemEvent
}

class RedeemViewModel(
    private val getPurchasePlans: GetPurchasePlansUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RedeemUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadHoldings()
    }

    fun handleEvent(event: RedeemEvent) {
        when (event) {
            is RedeemEvent.OnHoldingSelected ->
                _uiState.update { it.copy(selectedPlanId = event.planId) }

            is RedeemEvent.OnModeSelected -> _uiState.update { it.copy(mode = event.mode) }

            is RedeemEvent.OnAmountChange -> _uiState.update {
                it.copy(amountInput = sanitizeDecimal(event.value))
            }

            is RedeemEvent.OnUnitsChange -> _uiState.update {
                it.copy(unitsInput = sanitizeDecimal(event.value))
            }

            // TODO: wire the redemption call once that endpoint exists.
            RedeemEvent.OnRedeemClick -> Unit
        }
    }

    private fun loadHoldings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getPurchasePlans()) {
                is NetworkResponse.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    SnackBarController.showError(result.error.message)
                }

                is NetworkResponse.Success -> {
                    val holdings = result.data.map { it.toHolding() }
                    _uiState.update {
                        it.copy(
                            holdings = holdings,
                            selectedPlanId = holdings.firstOrNull()?.planId,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun PurchasePlan.toHolding() = RedeemHolding(
        planId = id,
        scheme = scheme,
        folioNumber = folioNumber?.takeIf { it.isNotBlank() },
        // Units and valuation are not part of the purchase-plan payload.
        availableUnits = null,
        currentValue = null,
        monthlyAmount = amount.takeIf { it.isNotBlank() }?.let { "₹$it" },
        state = state
    )

    /** Digits with at most one decimal point — units are fractional, amounts usually are not. */
    private fun sanitizeDecimal(input: String): String {
        val filtered = input.filter { it.isDigit() || it == '.' }
        val firstDot = filtered.indexOf('.')
        if (firstDot < 0) return filtered.take(MAX_INPUT_LENGTH)

        val whole = filtered.substring(0, firstDot)
        val fraction = filtered.substring(firstDot + 1).filter { it.isDigit() }
        return "$whole.$fraction".take(MAX_INPUT_LENGTH)
    }

    private companion object {
        const val MAX_INPUT_LENGTH = 12
    }
}
