package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.ConfirmMandateUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.CreateMandateUseCase

data class SetupAutopayUiState(
    val autopayType: String = "UPI Autopay",
    val autopayLimit: String = "₹1,00,000 / debit",
    val bankDetails: String = "HDFC BANK ••1193",
    val isLoading: Boolean = false
)

sealed interface SetupAutopayEvent {
    data object OnSetAutopayClick : SetupAutopayEvent

    /**
     * Raised once the user comes back from the mandate authorization page. Whether they actually
     * authorized it is only knowable from the server, so the return confirms with the mandate API.
     */
    data object OnAuthorizationReturned : SetupAutopayEvent
}

sealed interface SetupAutopayEffect {
    data class OpenMandateWebView(val url: String) : SetupAutopayEffect
    data object AutopayCompleted : SetupAutopayEffect
}

class SetupAutopayViewModel(
    private val createMandate: CreateMandateUseCase,
    private val confirmMandate: ConfirmMandateUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SetupAutopayUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SetupAutopayEffect>()
    val effect = _effect.receiveAsFlow()

    /** Held from the create call so the confirmation after the web view can be keyed on it. */
    private var mandateId: Int? = null

    fun handleEvent(event: SetupAutopayEvent) {
        when (event) {
            SetupAutopayEvent.OnSetAutopayClick -> onSetAutopayClick()
            SetupAutopayEvent.OnAuthorizationReturned -> onAuthorizationReturned()
        }
    }

    private fun onSetAutopayClick() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            setLoading(true)

            val result = createMandate(
                mandateLimit = MANDATE_LIMIT,
                validFrom = DateTimeUtils.today().toString(),
                paymentPostbackUrl = PAYMENT_POSTBACK_URL
            )

            setLoading(false)

            when (result) {
                is NetworkResponse.Error -> SnackBarController.showError(result.error.message)

                is NetworkResponse.Success -> {
                    val mandate = result.data
                    val tokenUrl = mandate.tokenUrl

                    if (mandate.id == null || tokenUrl.isNullOrBlank()) {
                        SnackBarController.showError(MANDATE_FAILED_MESSAGE)
                        return@launch
                    }

                    mandateId = mandate.id
                    _effect.send(SetupAutopayEffect.OpenMandateWebView(tokenUrl))
                }
            }
        }
    }

    /**
     * The bank approves the mandate asynchronously, so the read-back is polled until it reports
     * approval. Onboarding only continues on an approved mandate — a lookup that never settles
     * leaves the user on this screen to try again.
     */
    private fun onAuthorizationReturned() {
        if (_uiState.value.isLoading) return

        val id = mandateId ?: run {
            viewModelScope.launch { SnackBarController.showError(MANDATE_FAILED_MESSAGE) }
            return
        }

        viewModelScope.launch {
            setLoading(true)

            repeat(MANDATE_POLL_ATTEMPTS) { attempt ->
                // The first read happens immediately; the bank has often answered by then.
                if (attempt > 0) delay(MANDATE_POLL_INTERVAL_MS)

                when (val result = confirmMandate(id)) {
                    is NetworkResponse.Error -> {
                        setLoading(false)
                        SnackBarController.showError(MANDATE_FAILED_MESSAGE)
                        return@launch
                    }

                    is NetworkResponse.Success -> if (result.data.isApproved) {
                        setLoading(false)
                        SnackBarController.showSuccess("UPI Autopay has been set up successfully.")
                        _effect.send(SetupAutopayEffect.AutopayCompleted)
                        return@launch
                    }
                }
            }

            setLoading(false)
            SnackBarController.showError(MANDATE_PENDING_MESSAGE)
        }
    }

    private fun setLoading(loading: Boolean) {
        _uiState.update { it.copy(isLoading = loading) }
    }

    private companion object {
        /** Fixed per-debit ceiling; mirrors the limit shown on the screen. */
        const val MANDATE_LIMIT = 100_000L

        /** No server-side postback is wired up yet, so the API is sent an empty URL. */
        const val PAYMENT_POSTBACK_URL = "https://yourapp.com/payment_confirmation"

        const val MANDATE_FAILED_MESSAGE = "Mandate creation failed. Please try again."

        /** Four reads gives three 5-second gaps — about 15 seconds for the bank to approve. */
        const val MANDATE_POLL_ATTEMPTS = 4
        const val MANDATE_POLL_INTERVAL_MS = 5_000L

        const val MANDATE_PENDING_MESSAGE =
            "Your autopay mandate has not been approved yet. Please try again in a moment."
    }
}
