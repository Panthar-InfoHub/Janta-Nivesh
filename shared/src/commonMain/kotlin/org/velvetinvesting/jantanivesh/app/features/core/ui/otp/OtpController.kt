package org.velvetinvesting.jantanivesh.app.features.core.ui.otp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * The parts of an OTP screen that are the same everywhere: digit filtering, length capping and the
 * resend cooldown. A ViewModel *owns* one of these rather than inheriting from a base class, so it
 * stays free to define what submitting and resending actually do and where the user goes next.
 *
 * ```
 * class MyViewModel(...) : ViewModel() {
 *     val otp = OtpController(viewModelScope)
 *     fun submit() = viewModelScope.launch {
 *         otp.withLoading { myUseCase(otp.state.value.otpValue) }
 *     }
 * }
 * ```
 */
class OtpController(
    private val scope: CoroutineScope,
    otpLength: Int = OtpUiState.DEFAULT_OTP_LENGTH,
    private val resendCooldownSeconds: Int = DEFAULT_RESEND_COOLDOWN_SECONDS,
    startTimerImmediately: Boolean = true
) {
    private val _state = MutableStateFlow(OtpUiState(otpLength = otpLength))
    val state: StateFlow<OtpUiState> = _state.asStateFlow()

    /** Held so a resend restarts the countdown instead of racing a second one against it. */
    private var timerJob: Job? = null

    init {
        if (startTimerImmediately) startResendTimer()
    }

    /** Ignores anything that is not a digit, and anything past [OtpUiState.otpLength]. */
    fun onOtpChange(value: String) {
        if (!value.all { it.isDigit() }) return
        _state.update { it.copy(otpValue = value.take(it.otpLength)) }
    }

    fun clearOtp() {
        _state.update { it.copy(otpValue = "") }
    }

    fun setLoading(loading: Boolean) {
        _state.update { it.copy(isLoading = loading) }
    }

    fun startResendTimer() {
        timerJob?.cancel()
        timerJob = scope.launch {
            for (second in resendCooldownSeconds downTo 0) {
                _state.update { it.copy(resendTimerSeconds = second) }
                if (second > 0) delay(1000)
            }
        }
    }

    /**
     * Runs [block] with the loading flag raised, clearing it however [block] ends so a thrown or
     * cancelled request cannot strand the button in its spinner.
     */
    suspend fun <T> withLoading(block: suspend () -> T): T {
        setLoading(true)
        return try {
            block()
        } finally {
            setLoading(false)
        }
    }

    companion object {
        const val DEFAULT_RESEND_COOLDOWN_SECONDS = 30
    }
}
