package org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels

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
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.FinalizeKycUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.GetContractPdfUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.GetESignUrlUseCase

data class KycContractUiState(
    val isContractLoading: Boolean = false,
    val isSubmitLoading: Boolean = false,
    val contractPdfUrl: String? = null,
    val isMarkedAsRead: Boolean = false,
    val isKycFinalized: Boolean = false,
    val showError: Boolean = false,
    val errorMessage: String = ""
)

sealed interface KycContractEvent {
    data object OnLoadContract : KycContractEvent
    data object OnStartESignClicked : KycContractEvent
    data object OnESignCompleted : KycContractEvent
    data object OnToggleMarkedAsRead : KycContractEvent
}

sealed interface KycContractEffect {
    data class OpenBrowser(val url: String) : KycContractEffect
    data object NavigateToSuccess : KycContractEffect
    data class ShowError(val message: String) : KycContractEffect
}

class KycContractViewModel(
    private val getContractPdfUseCase: GetContractPdfUseCase,
    private val getESignUrlUseCase: GetESignUrlUseCase,
    private val finalizeKycUseCase: FinalizeKycUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(KycContractUiState())
    val uiState: StateFlow<KycContractUiState> = _uiState.asStateFlow()

    private val _effect = Channel<KycContractEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        handleEvent(KycContractEvent.OnLoadContract)
    }

    fun handleEvent(event: KycContractEvent) {
        when (event) {
            KycContractEvent.OnLoadContract -> loadContractPdf()
            KycContractEvent.OnStartESignClicked -> loadESignUrl()
            KycContractEvent.OnESignCompleted -> finalizeKyc()

            KycContractEvent.OnToggleMarkedAsRead -> {
                _uiState.update {
                    it.copy(
                        isMarkedAsRead = !it.isMarkedAsRead
                    )
                }
            }
        }
    }

    private fun loadContractPdf() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(isContractLoading = true)
            }

            when (val response = getContractPdfUseCase()) {

                is NetworkResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            contractPdfUrl = response.data,
                            isContractLoading = false
                        )
                    }
                }

                is NetworkResponse.Error -> {
                    _uiState.update {
                        it.copy(isContractLoading = false,
                            showError = true,
                            errorMessage = response.error.message)
                    }
                }
            }
        }
    }

    private fun loadESignUrl() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(isSubmitLoading = true)
            }

            when (val response = getESignUrlUseCase()) {

                is NetworkResponse.Success -> {

                    _uiState.update {
                        it.copy(isSubmitLoading = false)
                    }

                    sendEffect(
                        KycContractEffect.OpenBrowser(
                            response.data
                        )
                    )
                }

                is NetworkResponse.Error -> {

                    _uiState.update {
                        it.copy(isSubmitLoading = false)
                    }

                    sendEffect(
                        KycContractEffect.ShowError(
                            response.error.message
                        )
                    )
                }
            }
        }
    }

    private fun finalizeKyc() {

        val existingPdfUrl = _uiState.value.contractPdfUrl

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isContractLoading = true
                )
            }

            when (val response = finalizeKycUseCase()) {

                is NetworkResponse.Success -> {

                    AppEventsController.sendHomeRefreshEvent()

                    _uiState.update {
                        it.copy(
                            isContractLoading = false,
                            contractPdfUrl = existingPdfUrl,
                            isKycFinalized = true
                        )
                    }

                    sendEffect(
                        KycContractEffect.NavigateToSuccess
                    )
                }

                is NetworkResponse.Error -> {

                    _uiState.update {
                        it.copy(
                            isContractLoading = false,
                            contractPdfUrl = existingPdfUrl
                        )
                    }

                    sendEffect(
                        KycContractEffect.ShowError(
                            response.error.message
                        )
                    )
                }
            }
        }
    }

    private fun sendEffect(effect: KycContractEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}