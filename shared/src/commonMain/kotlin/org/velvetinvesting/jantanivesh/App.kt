package org.velvetinvesting.jantanivesh

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import org.koin.compose.koinInject
import org.velvetinvesting.jantanivesh.app.core.localization.LocalAppLanguageLocale
import org.velvetinvesting.jantanivesh.app.core.localization.model.AppLanguage
import org.velvetinvesting.jantanivesh.app.core.localization.repository.LanguageRepository
import org.velvetinvesting.jantanivesh.app.core.navigation.BaseNavigation
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.utils.AppSnackbarHost

@Composable
fun App() {

    val languageRepository: LanguageRepository = koinInject()
    val currentLanguage by languageRepository.currentLanguageFlow()
        .collectAsState(initial = AppLanguage.HINDI)

    CompositionLocalProvider(
        LocalAppLanguageLocale provides currentLanguage.code
    ) {
        JantaNiveshTheme {
            Scaffold(
                containerColor = Color.White,
                snackbarHost = {
                    AppSnackbarHost()
                }
            ) {
                BaseNavigation()
            }
        }
    }
}