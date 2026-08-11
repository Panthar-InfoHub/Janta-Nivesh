package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels

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
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.KycFormStatus
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.usecases.GetKycFormStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.usecases.InitiateKycFormUseCase

data class VerifyWithDigilockerUiState(
    val isLoading: Boolean = false
)

sealed interface VerifyWithDigilockerEvent {
    data object OnProceedClick : VerifyWithDigilockerEvent

    /**
     * Raised once the user comes back from the DigiLocker web view. The KYC form status is the
     * only source of truth for what happened there, so every return re-reads it.
     */
    data object OnStepReturned : VerifyWithDigilockerEvent
}

sealed interface VerifyWithDigilockerEffect {
    data class OpenWebView(val url: String, val title: String) : VerifyWithDigilockerEffect

    /**
     * Identity documents have been pulled from DigiLocker; the signature is collected next. The
     * eSign those two produce is resolved later, on the investor profile screen.
     */
    data object ProofVerified : VerifyWithDigilockerEffect
}

class VerifyWithDigilockerViewModel(
    private val initiateKycForm: InitiateKycFormUseCase,
    private val getKycFormStatus: GetKycFormStatusUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(VerifyWithDigilockerUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<VerifyWithDigilockerEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: VerifyWithDigilockerEvent) {
        when (event) {
            VerifyWithDigilockerEvent.OnProceedClick -> onProceedClick()
            VerifyWithDigilockerEvent.OnStepReturned -> onStepReturned()
        }
    }

    private fun onProceedClick() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            setLoading(true)

            when (val initiateResult = initiateKycForm()) {
                is NetworkResponse.Error -> {
                    setLoading(false)
                    SnackBarController.showError(initiateResult.error.message)
                }

                is NetworkResponse.Success -> refreshStatus()
            }
        }
    }

    private fun onStepReturned() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            setLoading(true)
            refreshStatus()
        }
    }

    private suspend fun refreshStatus() {
        when (val statusResult = getKycFormStatus()) {
            is NetworkResponse.Error -> {
                setLoading(false)
                SnackBarController.showError(statusResult.error.message)
            }

            is NetworkResponse.Success -> {
                setLoading(false)
                routeTo(statusResult.data)
            }
        }
    }

    /**
     * This screen only drives the document-fetch stage. Anything at or past "fetched" — including
     * a form whose eSign is already done — means there is nothing left to do here.
     */
    private suspend fun routeTo(status: KycFormStatus) {
        when {
            status.isProofFetched || status.isESignSuccessful ->
                sendEffect(VerifyWithDigilockerEffect.ProofVerified)

            status.isProofPending -> sendEffect(
                VerifyWithDigilockerEffect.OpenWebView(
                    url = status.proofFetchUrl.orEmpty(),
                    title = "DigiLocker Verification"
                )
            )

            else -> SnackBarController.showError(
                "Your KYC verification is still being processed. Please try again in a moment."
            )
        }
    }

    private fun setLoading(loading: Boolean) {
        _uiState.update { it.copy(isLoading = loading) }
    }

    private suspend fun sendEffect(effect: VerifyWithDigilockerEffect) {
        _effect.send(effect)
    }
}
