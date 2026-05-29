package org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.localization.model.AppLanguage
import org.velvetinvesting.jantanivesh.app.core.localization.repository.LanguageRepository

data class ChooseLanguageUiState(
    val isLoading: Boolean = false,
    val availableSecondaryLanguages: List<AppLanguage> = emptyList(),
    val selectedLanguage: AppLanguage? = null,
    val isNextEnabled: Boolean = false
)

sealed interface ChooseLanguageEvent {
    data class OnLanguageSelected(val language: AppLanguage) : ChooseLanguageEvent
    object OnContinueClicked : ChooseLanguageEvent
}

sealed interface ChooseLanguageEffect {
    object NavigateToNextScreen : ChooseLanguageEffect
}

class ChooseLanguageViewModel(private val languageSelector: LanguageRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ChooseLanguageUiState(
            isLoading = true,
            availableSecondaryLanguages = getInitialLanguages(),
            selectedLanguage = null
        )
    )

    val uiState: StateFlow<ChooseLanguageUiState> = _uiState.asStateFlow()
    private val _effect = Channel<ChooseLanguageEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            languageSelector.currentLanguageFlow().collect { currentLanguage ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    selectedLanguage = currentLanguage,
                    isNextEnabled = currentLanguage != null
                )
            }
        }
    }

    fun handleEvent(event: ChooseLanguageEvent) {
        when (event) {
            is ChooseLanguageEvent.OnLanguageSelected -> {
                selectLanguage(event.language)
            }

            ChooseLanguageEvent.OnContinueClicked -> {
                continueToNextScreen()
            }
        }
    }

    private fun selectLanguage(language: AppLanguage) {
        viewModelScope.launch {
            languageSelector.setLanguage(language)
        }
    }

    private fun continueToNextScreen() {
        viewModelScope.launch {
            _effect.send(ChooseLanguageEffect.NavigateToNextScreen)
        }
    }
    private fun getInitialLanguages(): List<AppLanguage> {
        return listOf(
            AppLanguage.HINDI,
            AppLanguage.MARATHI,
            AppLanguage.GUJARATI,
            AppLanguage.TAMIL,
            AppLanguage.TELUGU,
            AppLanguage.BENGALI
        )
    }
}