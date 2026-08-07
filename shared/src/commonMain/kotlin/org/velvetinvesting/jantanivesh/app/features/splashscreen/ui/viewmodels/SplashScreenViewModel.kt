package org.velvetinvesting.jantanivesh.app.features.splashscreen.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


data class SplashScreenUiState(
    val currentImage: Int = 0,
)

sealed interface SplashScreenEvent {
    data object OnGetStartedClick : SplashScreenEvent
}

sealed interface SplashScreenEffect {
    data object OnGetStartedClick : SplashScreenEffect
}

class SplashScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SplashScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SplashScreenEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: SplashScreenEvent){
        when(event){
            SplashScreenEvent.OnGetStartedClick -> onGetStartedClick()
        }
    }

    private fun onGetStartedClick(){
        sendEffect(SplashScreenEffect.OnGetStartedClick)
    }
    private fun sendEffect(effect: SplashScreenEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}