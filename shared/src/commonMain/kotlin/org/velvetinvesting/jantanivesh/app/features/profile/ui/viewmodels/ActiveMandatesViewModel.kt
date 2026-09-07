package org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.GetMandatesUseCase
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.ActiveMandate
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.toActiveMandate

data class ActiveMandatesUiState(
    val isLoading: Boolean = false,
    val mandates: List<ActiveMandate> = emptyList(),
    val error: String? = null
)

sealed interface ActiveMandatesEvent {
    data object Retry : ActiveMandatesEvent
    data object OnBackClicked : ActiveMandatesEvent
}

sealed interface ActiveMandatesEffect {
    data object NavigateBack : ActiveMandatesEffect
}

/**
 * Read-only listing of the user's autopay mandates. The screen carries no action on a mandate, so
 * every state the gateway reports is shown as-is rather than filtered down to the usable ones.
 */
class ActiveMandatesViewModel(
    private val getMandatesUseCase: GetMandatesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActiveMandatesUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<ActiveMandatesEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadMandates()
    }

    fun handleEvent(event: ActiveMandatesEvent) {
        when (event) {
            ActiveMandatesEvent.Retry -> loadMandates()
            ActiveMandatesEvent.OnBackClicked -> viewModelScope.launch {
                _effect.send(ActiveMandatesEffect.NavigateBack)
            }
        }
    }

    private fun loadMandates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val response = getMandatesUseCase()) {
                is NetworkResponse.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        mandates = response.data.map { mandate -> mandate.toActiveMandate() },
                        error = null
                    )
                }

                is NetworkResponse.Error -> _uiState.update {
                    it.copy(isLoading = false, error = response.error.message)
                }
            }
        }
    }
}
