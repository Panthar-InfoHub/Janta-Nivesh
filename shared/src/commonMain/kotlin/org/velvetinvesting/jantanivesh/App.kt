package org.velvetinvesting.jantanivesh

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.core.localization.LocalAppLanguageLocale
import org.velvetinvesting.jantanivesh.app.core.localization.model.AppLanguage
import org.velvetinvesting.jantanivesh.app.core.localization.repository.LanguageRepository
import org.velvetinvesting.jantanivesh.app.core.navigation.AppNavigation
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens.KycFormScreen
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCFormScreenUiState

@Composable
fun App() {

    val languageRepository: LanguageRepository = koinInject()

    val currentLanguage by languageRepository.currentLanguageFlow()
        .collectAsState(initial = AppLanguage.HINDI)

    CompositionLocalProvider(
        LocalAppLanguageLocale provides currentLanguage.code
    ) {
        JantaNiveshTheme {
            //AppNavigation()
            KycFormScreen(
                state = KYCFormScreenUiState(),
                onEvent = {},
                onBack = {},
            )
        }
    }
}