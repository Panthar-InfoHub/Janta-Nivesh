package org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels

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
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.auth.domain.repository.MpinRepo
import org.velvetinvesting.jantanivesh.app.features.auth.domain.usecase.UpdateMpinUseCase
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.core.domain.usecase.GetUserDataUseCase

const val MPIN_LENGTH = 4

data class ChangePinUiState(
    val newPin: String = "",
    val confirmPin: String = "",
    val newPinVisible: Boolean = false,
    val confirmPinVisible: Boolean = false,
    /** True once the two entries have diverged; the screen owns the wording. */
    val pinsMismatch: Boolean = false,
    val mpinEnabled: Boolean = false,
    val mpinSetup: Boolean = false,
    val saving: Boolean = false
) {
    /**
     * Both fields complete and equal. The new PIN is only length-checked — the endpoint owns any
     * rule beyond that, and reports it back as a server error.
     */
    val canSubmit: Boolean
        get() = !saving &&
            newPin.length == MPIN_LENGTH &&
            confirmPin.length == MPIN_LENGTH &&
            newPin == confirmPin
}

sealed interface ChangePinEvent {
    data class OnNewPinChanged(val pin: String) : ChangePinEvent
    data class OnConfirmPinChanged(val pin: String) : ChangePinEvent
    data object OnToggleNewPinVisibility : ChangePinEvent
    data object OnToggleConfirmPinVisibility : ChangePinEvent
    data class OnMpinToggleChanged(val enabled: Boolean) : ChangePinEvent
    data object OnSaveClicked : ChangePinEvent
    data object OnBackClicked : ChangePinEvent
}

sealed interface ChangePinEffect {
    data object NavigateBack : ChangePinEffect

    /** The PIN is now the new one — the caller pops back to wherever the change was started from. */
    data object PinUpdated : ChangePinEffect
}

/**
 * Only ever reached behind [EnterPinPurpose.CHANGE_PIN], which is why there is no current-PIN
 * field here: the user has already proved the old PIN against `/user/verify-mpin` on the way in.
 */
class ChangePinViewModel(
    private val updateMpin: UpdateMpinUseCase,
    private val getUserData: GetUserDataUseCase,
    private val mpinRepo: MpinRepo,
    private val authPrefs: AuthPrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePinUiState(
        mpinEnabled = authPrefs.isMpinEnabled(),
        mpinSetup = authPrefs.isMpinSetup()
    ))
    val uiState: StateFlow<ChangePinUiState> = _uiState.asStateFlow()

    private val _effect = Channel<ChangePinEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: ChangePinEvent) {
        when (event) {
            is ChangePinEvent.OnNewPinChanged -> {
                val pin = event.pin.sanitised() ?: return
                _uiState.update { it.copy(newPin = pin).withMismatchChecked() }
            }

            is ChangePinEvent.OnConfirmPinChanged -> {
                val pin = event.pin.sanitised() ?: return
                _uiState.update { it.copy(confirmPin = pin).withMismatchChecked() }
            }

            ChangePinEvent.OnToggleNewPinVisibility ->
                _uiState.update { it.copy(newPinVisible = !it.newPinVisible) }

            ChangePinEvent.OnToggleConfirmPinVisibility ->
                _uiState.update { it.copy(confirmPinVisible = !it.confirmPinVisible) }

            is ChangePinEvent.OnMpinToggleChanged -> toggleMpin(event.enabled)

            ChangePinEvent.OnSaveClicked -> save()

            ChangePinEvent.OnBackClicked -> sendEffect(ChangePinEffect.NavigateBack)
        }
    }

    private fun toggleMpin(enabled: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(saving = true) }
            when (val response = mpinRepo.updateMpinStatus(enabled)) {
                is NetworkResponse.Success -> {
                    authPrefs.setMpinEnabled(enabled)
                    _uiState.update { it.copy(saving = false, mpinEnabled = enabled) }
                }

                is NetworkResponse.Error -> {
                    _uiState.update { it.copy(saving = false) }
                    SnackBarController.showError(response.error.message)
                }
            }
        }
    }

    /** Digits only, never longer than the PIN; null means the edit is rejected outright. */
    private fun String.sanitised(): String? =
        takeIf { it.length <= MPIN_LENGTH && it.all(Char::isDigit) }

    /**
     * The mismatch only surfaces once the confirm field is as long as the new one — complaining
     * while the user is still typing the second PIN would be noise.
     */
    private fun ChangePinUiState.withMismatchChecked(): ChangePinUiState {
        val diverged = confirmPin.isNotEmpty() &&
            confirmPin.length >= newPin.length &&
            confirmPin != newPin
        return copy(pinsMismatch = diverged)
    }

    private fun save() {
        val state = _uiState.value
        if (!state.canSubmit) return

        viewModelScope.launch {
            _uiState.update { it.copy(saving = true) }
            when (val response = updateMpin(state.newPin)) {
                is NetworkResponse.Success -> {
                    // Refresh data to get mpin_is_setup and other flags from server
                    when (val userResponse = getUserData()) {
                        is NetworkResponse.Success -> {
                            val user = userResponse.data
                            authPrefs.setMpinSetup(user.mpinIsSetup)
                            authPrefs.setMpinEnabled(user.mpinEnabled)
                            _uiState.update { 
                                it.copy(
                                    saving = false, 
                                    mpinSetup = user.mpinIsSetup,
                                    mpinEnabled = user.mpinEnabled
                                ) 
                            }
                        }
                        else -> {
                            // If userdata fetch fails, we still consider the PIN updated.
                            _uiState.update { it.copy(saving = false, mpinSetup = true) }
                            authPrefs.setMpinSetup(true)
                        }
                    }
                    SnackBarController.showSuccess("PIN updated successfully")
                    _effect.send(ChangePinEffect.PinUpdated)
                }

                is NetworkResponse.Error -> {
                    _uiState.update { it.copy(saving = false) }
                    SnackBarController.showError(response.error.message)
                }
            }
        }
    }

    private fun sendEffect(effect: ChangePinEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
