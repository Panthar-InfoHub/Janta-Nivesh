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
    val isLoading: Boolean = false, // Always good practice in MVI
    val availableSecondaryLanguages: List<LanguageOption> = emptyList(),
    val selectedLanguageId: String? = null // Holds the ID of the currently selected language
)

sealed interface ChooseLanguageEvent {
    data class OnLanguageSelected(val languageId: String) : ChooseLanguageEvent
    object OnContinueClicked : ChooseLanguageEvent
}

// 1. Define your one-time events
sealed interface ChooseLanguageEffect {
    object NavigateToNextScreen : ChooseLanguageEffect
}

class ChooseLanguageViewModel : ViewModel() {

    // The internal, mutable state.
    // We initialize it with some default data so the screen isn't empty when it first loads.
    private val _uiState = MutableStateFlow(
        ChooseLanguageUiState(
            isLoading = false,
            availableSecondaryLanguages = getInitialLanguages(),
            selectedLanguageId = null
        )
    )

    // The public, read-only state exposed to the UI
    val uiState: StateFlow<ChooseLanguageUiState> = _uiState.asStateFlow()
    private val _effect = Channel<ChooseLanguageEffect>()
    val effect = _effect.receiveAsFlow()

    /**
     * This is the single entry point for all UI events from the Choose Language screen.
     */
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

    /**
     * Handles updating the state when a user clicks a language card.
     */
    private fun selectLanguage(languageId: String) {
        // TODO: Implement logic to update the _uiState with the newly selected languageId
    }

    /**
     * Handles the logic when the user clicks the "Continue" button.
     */
    private fun continueToNextScreen() {
        // Do your backend logic, save to preferences, etc...

        // 3. Send the navigation effect!
        viewModelScope.launch {
            _effect.send(ChooseLanguageEffect.NavigateToNextScreen)
        }
    }

    /**
     * Helper function to provide the initial list of languages.
     * In a real app, this might come from a repository or remote config.
     */
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