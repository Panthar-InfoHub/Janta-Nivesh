package org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class ProfileSettingUiState(
    val loading: Boolean=false
)
sealed interface ProfileSettingEvent {


        data object OnProfileBackClicked : ProfileSettingEvent

        // Preferences
        data object OnNotificationClicked : ProfileSettingEvent

        // Security
        data object OnChangePinClicked : ProfileSettingEvent
//        data object OnBiometricLoginClicked : ProfileSettingEvent

        // Data & Privacy
        data object OnPrivacyPolicyClicked : ProfileSettingEvent
        data object OnTermsOfServiceClicked : ProfileSettingEvent
        data object OnDeleteAccountClicked : ProfileSettingEvent
    }

sealed interface ProfileSettingEffect {

    data object NavigateBack : ProfileSettingEffect

    data object NavigateToNotification : ProfileSettingEffect

    data object NavigateToChangePin : ProfileSettingEffect

//    data object NavigateToBiometricLogin : ProfileSettingEffect

    data object NavigateToPrivacyPolicy : ProfileSettingEffect

    data object NavigateToTermsOfService : ProfileSettingEffect

    data object NavigateToDeleteAccount : ProfileSettingEffect
}


class ProfileSettingViewModel: ViewModel(){
    private val _uiState = MutableStateFlow(ProfileSettingUiState())
    val uiState: StateFlow<ProfileSettingUiState> = _uiState.asStateFlow()

    private val _effect = Channel<ProfileSettingEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: ProfileSettingEvent) {
        when (event) {

            ProfileSettingEvent.OnProfileBackClicked->
                sendEffect(ProfileSettingEffect.NavigateBack)

            ProfileSettingEvent.OnNotificationClicked ->
                sendEffect(ProfileSettingEffect.NavigateToNotification)

            ProfileSettingEvent.OnChangePinClicked ->
                sendEffect(ProfileSettingEffect.NavigateToChangePin)

//            ProfileSettingEvent.OnBiometricLoginClicked ->
//                sendEffect(ProfileSettingEffect.NavigateToBiometricLogin)

            ProfileSettingEvent.OnPrivacyPolicyClicked ->
                sendEffect(ProfileSettingEffect.NavigateToPrivacyPolicy)

            ProfileSettingEvent.OnTermsOfServiceClicked ->
                sendEffect(ProfileSettingEffect.NavigateToTermsOfService)

            ProfileSettingEvent.OnDeleteAccountClicked ->
                sendEffect(ProfileSettingEffect.NavigateToDeleteAccount)
        }
    }
    private fun sendEffect(effect: ProfileSettingEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

}