package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class KycSplashUiState(
    val isLoading: Boolean = false
)

sealed interface KycSplashEvent {
    data object OnProceedClick : KycSplashEvent
}

sealed interface KycSplashEffect {
    data object OnProceedClick : KycSplashEffect
}

class KycSplashViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(KycSplashUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<KycSplashEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: KycSplashEvent) {
        when (event) {
            KycSplashEvent.OnProceedClick -> onProceedClick()
        }
    }

    private fun onProceedClick() {
        // TODO: Add any pre-proceed checks or analytics tracking here
        sendEffect(KycSplashEffect.OnProceedClick)
    }

    private fun sendEffect(effect: KycSplashEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}