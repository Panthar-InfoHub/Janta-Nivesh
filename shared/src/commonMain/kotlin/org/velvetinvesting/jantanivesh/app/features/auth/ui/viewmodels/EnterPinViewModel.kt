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
import org.velvetinvesting.jantanivesh.app.features.auth.domain.usecase.VerifyMpinUseCase
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpUiState

private const val PIN_LENGTH = 4

/**
 * Why the PIN is being asked for. The screen is the app lock on a cold start, and it is also the
 * gate in front of anything that has to re-establish who is holding the phone — changing the PIN,
 * for one. Carried as a string so it survives the [org.velvetinvesting.jantanivesh.app.core.navigation.Route]
 * serialization.
 */
object EnterPinPurpose {
    /** The once-per-cold-start lock; biometrics are allowed to clear it. */
    const val APP_LOCK = "app_lock"

    /**
     * Re-authentication ahead of the change-PIN screen. Biometrics are deliberately not offered:
     * the user has to prove they know the PIN they are about to replace.
     */
    const val CHANGE_PIN = "change_pin"
}

data class EnterPinUiState(
    val userName: String = "",
    val otp: OtpUiState = OtpUiState(otpLength = PIN_LENGTH),
    val errorMessage: String? = null,
    val verifying: Boolean = false,
    val subtitle: String = "",
    /** Whether the "Use Biometrics" shortcut is on offer at all — see [EnterPinPurpose]. */
    val biometricsAllowed: Boolean = true,
    /**
     * Drives the biometric prompt from the screen, which owns the platform API. Set once on
     * entry so a cold start goes straight to the prompt, and again whenever the user taps
     * "Use Biometrics"; the screen clears it through [EnterPinEvent.OnBiometricPromptShown].
     */
    val biometricPromptRequested: Boolean = false
)

sealed interface EnterPinEvent {
    data class OnPinChanged(val pin: String) : EnterPinEvent
    data object OnUseBiometricsClicked : EnterPinEvent
    data object OnBiometricPromptShown : EnterPinEvent
    data object OnBiometricSuccess : EnterPinEvent
    data class OnBiometricError(val message: String) : EnterPinEvent
}

sealed interface EnterPinEffect {
    /** The user is through — where that leads is the caller's call, and follows the purpose. */
    data object PinVerified : EnterPinEffect
}

class EnterPinViewModel(
    private val purpose: String,
    private val authPrefs: AuthPrefs,
    private val verifyMpin: VerifyMpinUseCase
) : ViewModel() {

    /**
     * Biometrics are on offer only for the app lock, and only while the user has left the switch
     * on the Biometric Login screen turned on — re-authenticating to change the PIN always wants
     * the PIN itself.
     */
    private val biometricsAllowed =
        purpose != EnterPinPurpose.CHANGE_PIN && authPrefs.isBiometricLoginEnabled()

    private val _uiState = MutableStateFlow(
        EnterPinUiState(
            userName = authPrefs.getFullName().orEmpty(),
            subtitle = when (purpose) {
                EnterPinPurpose.CHANGE_PIN -> "Enter your current PIN to continue"
                else -> "Enter your Janta Nivesh PIN"
            },
            biometricsAllowed = biometricsAllowed,
            // The lock prompts as soon as it appears; the tap only re-arms it.
            biometricPromptRequested = biometricsAllowed
        )
    )
    val uiState: StateFlow<EnterPinUiState> = _uiState.asStateFlow()

    private val _effect = Channel<EnterPinEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: EnterPinEvent) {
        when (event) {
            is EnterPinEvent.OnPinChanged -> onPinChanged(event.pin)

            EnterPinEvent.OnUseBiometricsClicked ->
                if (_uiState.value.biometricsAllowed) {
                    _uiState.update { it.copy(biometricPromptRequested = true, errorMessage = null) }
                }

            EnterPinEvent.OnBiometricPromptShown ->
                _uiState.update { it.copy(biometricPromptRequested = false) }

            EnterPinEvent.OnBiometricSuccess -> sendEffect(EnterPinEffect.PinVerified)

            is EnterPinEvent.OnBiometricError ->
                // Nothing is blocked by a failed prompt — the PIN field is still there.
                _uiState.update { it.copy(errorMessage = event.message.takeIf { m -> m.isNotBlank() }) }
        }
    }

    private fun onPinChanged(pin: String) {
        if (_uiState.value.verifying) return
        if (pin.length > PIN_LENGTH) return
        if (pin.any { !it.isDigit() }) return
        _uiState.update { it.copy(otp = it.otp.copy(otpValue = pin), errorMessage = null) }
        if (pin.length == PIN_LENGTH) verifyPin(pin)
    }

    private fun verifyPin(pin: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(verifying = true, errorMessage = null) }
            when (val response = verifyMpin(pin)) {
                is NetworkResponse.Success -> {
                    _uiState.update { it.copy(verifying = false) }
                    sendEffect(EnterPinEffect.PinVerified)
                }

                is NetworkResponse.Error -> {
                    // Clear the field so the next attempt starts from an empty row of boxes.
                    _uiState.update {
                        it.copy(
                            verifying = false,
                            otp = it.otp.copy(otpValue = ""),
                            errorMessage = response.error.message
                        )
                    }
                }
            }
        }
    }

    private fun sendEffect(effect: EnterPinEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
