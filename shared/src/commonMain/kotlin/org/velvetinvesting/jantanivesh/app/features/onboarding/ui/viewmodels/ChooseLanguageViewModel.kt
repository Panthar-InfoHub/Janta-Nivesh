package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.models.LanguageOption

data class ChooseLanguageUiState(
    val isLoading: Boolean = false,
    val availableSecondaryLanguages: List<LanguageOption> = emptyList(),
    val selectedLanguageId: String? = null 
)

sealed interface ChooseLanguageEvent {
    data class OnLanguageSelected(val languageId: String) : ChooseLanguageEvent
    object OnContinueClicked : ChooseLanguageEvent
}

sealed interface ChooseLanguageEffect {
    object NavigateToNextScreen : ChooseLanguageEffect
}

class ChooseLanguageViewModel : ViewModel() {

   private val _uiState = MutableStateFlow(
        ChooseLanguageUiState(
            isLoading = false,
            availableSecondaryLanguages = getInitialLanguages(),
            selectedLanguageId = null
        )
    )

    val uiState: StateFlow<ChooseLanguageUiState> = _uiState.asStateFlow()
    private val _effect = Channel<ChooseLanguageEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: ChooseLanguageEvent) {
        when (event) {
            is ChooseLanguageEvent.OnLanguageSelected -> {
                selectLanguage(event.languageId)
            }

            ChooseLanguageEvent.OnContinueClicked -> {
                continueToNextScreen()
            }
        }
    }
    private fun selectLanguage(languageId: String) {
         // TODO: Implement logic to update the _uiState with the newly selected languageId
    }
    private fun continueToNextScreen() {
        viewModelScope.launch {
            _effect.send(ChooseLanguageEffect.NavigateToNextScreen)
        }
    }
    private fun getInitialLanguages(): List<LanguageOption> {
        return listOf(
            LanguageOption("hi", "हिन्दी", "Hindi"),
            LanguageOption("mr", "मराठी", "Marathi"),
            LanguageOption("gu", "ગુજરાતી", "Gujarati"),
            LanguageOption("ta", "தமிழ்", "Tamil"),
            LanguageOption("te", "తెలుగు", "Telugu"),
            LanguageOption("bn", "বাংলা", "Bengali")
        )
    }
}