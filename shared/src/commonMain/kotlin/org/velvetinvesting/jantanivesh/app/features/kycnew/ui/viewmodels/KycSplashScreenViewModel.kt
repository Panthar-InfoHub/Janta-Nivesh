package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface KycSplashEvent {
    data object OnProceedClick : KycSplashEvent
}

sealed interface KycSplashEffect {
    data object OnProceedClick : KycSplashEffect
}

class KycSplashViewModel : ViewModel() {
    private val _effect = MutableSharedFlow<KycSplashEffect>()
    val effect = _effect.asSharedFlow()

    fun handleEvent(event: KycSplashEvent) {
        when (event) {
            KycSplashEvent.OnProceedClick -> onProceedClick()
        }
    }

    private fun onProceedClick() {
        sendEffect(KycSplashEffect.OnProceedClick)
    }

    private fun sendEffect(effect: KycSplashEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}