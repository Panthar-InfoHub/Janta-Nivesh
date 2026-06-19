package org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LanguageItem(
    val title:String,
    val subTitle: String
)

val languageList = listOf(
    LanguageItem(title = "हिंदी", subTitle = "Hindi"),
    LanguageItem(title = "मराठी", subTitle = "Marathi"),
    LanguageItem(title = "ગુજરાતી", subTitle = "Gujarati"),
    LanguageItem(title = "தமிழ்", subTitle = "Tamil"),
    LanguageItem(title = "తెలుగు", subTitle = "Telugu"),
    LanguageItem(title = "বাংলা", subTitle = "Bengali"),
    )

data class ProfileLanguageUiState(
    val languages: List<LanguageItem> = languageList,
    val selectedLanguage: LanguageItem? = null,
    val isLoading: Boolean = false,
    val isSaveEnabled: Boolean = false
)

sealed interface ProfileLanguageEvent {

    data object OnBackClicked : ProfileLanguageEvent

    data class OnLanguageSelected(
        val language: LanguageItem
    ) : ProfileLanguageEvent

    data object OnSaveClicked : ProfileLanguageEvent
}

sealed interface ProfileLanguageEffect {

    data object NavigateBack : ProfileLanguageEffect

    data object LanguageSaved : ProfileLanguageEffect

    data class ShowError(
        val message: String
    ) : ProfileLanguageEffect
}


class ProfileLanguageViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileLanguageUiState())
    val uiState: StateFlow<ProfileLanguageUiState> = _uiState.asStateFlow()

    private val _effect = Channel<ProfileLanguageEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: ProfileLanguageEvent) {
        when (event) {

            ProfileLanguageEvent.OnBackClicked -> {
                sendEffect(ProfileLanguageEffect.NavigateBack)
            }

            is ProfileLanguageEvent.OnLanguageSelected -> {
                _uiState.update {
                    it.copy(
                        selectedLanguage = event.language,
                        isSaveEnabled = true
                    )
                }
            }

            ProfileLanguageEvent.OnSaveClicked -> {
                saveLanguage()
            }
        }
    }

    private fun saveLanguage() {
        viewModelScope.launch {

            val selectedLanguage = _uiState.value.selectedLanguage

            if (selectedLanguage == null) {
                sendEffect(
                    ProfileLanguageEffect.ShowError(
                        "Please select a language"
                    )
                )
                return@launch
            }

            // TODO: Save selected language to DataStore/API

            sendEffect(ProfileLanguageEffect.LanguageSaved)
        }
    }

    private fun sendEffect(effect: ProfileLanguageEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}