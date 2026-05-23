package org.velvetinvesting.jantanivesh

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.models.LanguageOption
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.OnboardingChooseLanguage
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ChooseLanguageUiState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme

@Composable
@Preview
fun App() {
    val mockLanguages = listOf(
        LanguageOption("hi", "हिन्दी", "Hindi"),
        LanguageOption("mr", "मराठी", "Marathi"),
        LanguageOption("gu", "ગુજરાતી", "Gujarati"),
        LanguageOption("ta", "தமிழ்", "Tamil"),
        LanguageOption("te", "తెలుగు", "Telugu"),
        LanguageOption("bn", "বাংলা", "Bengali")
    )

    val dummyState = ChooseLanguageUiState(
        availableSecondaryLanguages = mockLanguages,
        selectedLanguageId = "mr"
    )
    JantaNiveshTheme {

    JantaNiveshTheme {
        OnboardingChooseLanguage(
            state = dummyState,
            onEvent = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}