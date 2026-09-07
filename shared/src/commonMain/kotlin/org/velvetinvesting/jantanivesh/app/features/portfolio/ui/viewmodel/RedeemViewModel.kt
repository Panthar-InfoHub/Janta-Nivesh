package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.RedemptionState
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.AwaitMfRedemptionUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.CreateMfRedemptionUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.RedemptionRequest
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.RequestMfRedemptionOtpUseCase

enum class RedeemMode(val label: String) {
    AMOUNT("Amount"),
    UNITS("Units"),
    ALL_UNITS("All units")
}

/**
 * The holding being redeemed, handed over by the order-details screen that opened this one.
 *
 * Nothing here is fetched: the portfolio already holds every figure, so this screen has no load
 * state and no list of holdings to pick from.
 */
data class RedeemHolding(
    /** `mf_holding_id` — what `POST /mf/redemption/` is keyed on. */
    val holdingId: String,
    val scheme: String,
    val folioNumber: String,
    val availableUnits: Double,
    val currentValue: Double,
    val isSip: Boolean
)

data class RedeemUiState(
    val holding: RedeemHolding,
    val mode: RedeemMode = RedeemMode.ALL_UNITS,
    val amountInput: String = "",
    val unitsInput: String = "",
    val isRedeeming: Boolean = false
) {
    private val amount: Double?
        get() = amountInput.toDoubleOrNull()

    private val units: Double?
        get() = unitsInput.toDoubleOrNull()

    /**
     * Stated under the field rather than only blocking the button, so the user is told *why*
     * they cannot continue. Null while the field is empty — an untouched field is not an error.
     */
    val inputError: String?
        get() = when (mode) {
            RedeemMode.AMOUNT -> when {
                amountInput.isBlank() -> null
                amount == null || amount!! <= 0.0 -> "Enter an amount to redeem"
                amount!! > holding.currentValue -> "You can redeem at most the current value"
                else -> null
            }

            RedeemMode.UNITS -> when {
                unitsInput.isBlank() -> null
                units == null || units!! <= 0.0 -> "Enter the units to redeem"
                units!! > holding.availableUnits -> "You hold only ${holding.availableUnits} units"
                else -> null
            }

            RedeemMode.ALL_UNITS -> null
        }

    /** All units needs no input; the other two need a positive value within what is held. */
    private val isInputValid: Boolean
        get() = when (mode) {
            RedeemMode.AMOUNT ->
                amount != null && amount!! > 0.0 && amount!! <= holding.currentValue

            RedeemMode.UNITS ->
                units != null && units!! > 0.0 && units!! <= holding.availableUnits

            RedeemMode.ALL_UNITS -> holding.availableUnits > 0.0
        }

    val canRedeem: Boolean
        get() = !isRedeeming && isInputValid
}

sealed interface RedeemEvent {
    data class OnModeSelected(val mode: RedeemMode) : RedeemEvent
    data class OnAmountChange(val value: String) : RedeemEvent
    data class OnUnitsChange(val value: String) : RedeemEvent
    data object OnRedeemClick : RedeemEvent
}

sealed interface RedeemEffect {
    /**
     * The redemption is placed and the code is on its way. [redemptionId] is the gateway's
     * `fp_id`, which the OTP screen confirms against.
     */
    data class NavigateToOtp(val redemptionId: String) : RedeemEffect
}

/**
 * Drives the three-step lead-in to a redemption: place it, wait for the gateway to accept it,
 * then ask for the confirmation code. Only after all three does the user reach the OTP screen —
 * sending them there earlier would put them in front of a code that was never issued.
 */
class RedeemViewModel(
    holding: RedeemHolding,
    private val createRedemption: CreateMfRedemptionUseCase,
    private val awaitRedemption: AwaitMfRedemptionUseCase,
    private val requestOtp: RequestMfRedemptionOtpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RedeemUiState(holding = holding))
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<RedeemEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: RedeemEvent) {
        when (event) {
            is RedeemEvent.OnModeSelected -> _uiState.update { it.copy(mode = event.mode) }

            is RedeemEvent.OnAmountChange -> _uiState.update {
                it.copy(amountInput = sanitizeDecimal(event.value))
            }

            is RedeemEvent.OnUnitsChange -> _uiState.update {
                it.copy(unitsInput = sanitizeDecimal(event.value))
            }

            RedeemEvent.OnRedeemClick -> redeem()
        }
    }

    private fun redeem() {
        val state = _uiState.value
        if (!state.canRedeem) return

        val request = when (state.mode) {
            RedeemMode.AMOUNT -> RedemptionRequest.ByAmount(state.amountInput.toDouble())
            RedeemMode.UNITS -> RedemptionRequest.ByUnits(state.unitsInput.toDouble())
            // "Everything" is expressed as the full unit balance, which is what the user is told.
            RedeemMode.ALL_UNITS -> RedemptionRequest.ByUnits(state.holding.availableUnits)
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isRedeeming = true) }

            val created = createRedemption(state.holding.holdingId, request)
            if (created is NetworkResponse.Error) {
                fail(created.error.message)
                return@launch
            }

            val redemptionId = (created as NetworkResponse.Success).data.id

            // Placed but not yet accepted — the OTP endpoint rejects it until it is.
            val settled = awaitRedemption(redemptionId, RedemptionState.PENDING)
            if (settled is NetworkResponse.Error) {
                fail(settled.error.message)
                return@launch
            }

            val otpSent = requestOtp(redemptionId)
            if (otpSent is NetworkResponse.Error) {
                fail(otpSent.error.message)
                return@launch
            }

            _uiState.update { it.copy(isRedeeming = false) }
            _effect.send(RedeemEffect.NavigateToOtp(redemptionId))
        }
    }

    private suspend fun fail(message: String) {
        _uiState.update { it.copy(isRedeeming = false) }
        SnackBarController.showError(message)
    }

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
