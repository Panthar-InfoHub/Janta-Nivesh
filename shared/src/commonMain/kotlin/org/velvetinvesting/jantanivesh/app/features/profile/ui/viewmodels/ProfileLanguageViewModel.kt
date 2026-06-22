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
import org.velvetinvesting.jantanivesh.app.core.localization.model.AppLanguage
import org.velvetinvesting.jantanivesh.app.core.localization.repository.LanguageRepository


data class ProfileLanguageUiState(
    val languages: List<AppLanguage> = AppLanguage.entries,
    val selectedLanguage: AppLanguage? = null,
    val isLoading: Boolean = false,
    val isSaveEnabled: Boolean = false
)

sealed interface ProfileLanguageEvent {

    data object OnBackClicked : ProfileLanguageEvent

    data class OnLanguageSelected(
        val language: AppLanguage
    ) : ProfileLanguageEvent

    data object OnSaveClicked : ProfileLanguageEvent
}

sealed interface ProfileLanguageEffect {

    data object NavigateBack : ProfileLanguageEffect


    data class ShowError(
        val message: String
    ) : ProfileLanguageEffect
}


class ProfileLanguageViewModel(
    private val languageRepository: LanguageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileLanguageUiState())
    val uiState: StateFlow<ProfileLanguageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch{
            val currentSecondaryLanguage = languageRepository.getLanguage()
            _uiState.update {
                it.copy(
                    selectedLanguage = currentSecondaryLanguage
                )
            }
        }
    }

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

            languageRepository.setLanguage(selectedLanguage)

            sendEffect(ProfileLanguageEffect.NavigateBack)
        }
    }

    private fun sendEffect(effect: ProfileLanguageEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}