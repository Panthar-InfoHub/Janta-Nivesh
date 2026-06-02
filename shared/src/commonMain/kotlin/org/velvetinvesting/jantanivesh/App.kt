package org.velvetinvesting.jantanivesh

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import org.velvetinvesting.jantanivesh.app.core.localization.LocalAppLanguageLocale
import org.velvetinvesting.jantanivesh.app.core.localization.model.AppLanguage
import org.velvetinvesting.jantanivesh.app.core.localization.repository.LanguageRepository
import org.velvetinvesting.jantanivesh.app.core.navigation.AppNavigation
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarType

@Composable
fun App() {

    val languageRepository: LanguageRepository = koinInject()

    val currentLanguage by languageRepository.currentLanguageFlow()
        .collectAsState(initial = AppLanguage.HINDI)

    CompositionLocalProvider(
        LocalAppLanguageLocale provides currentLanguage.code
    ) {

        val snackbarHostState = remember { SnackbarHostState() }
        var currentSnackBar by remember { mutableStateOf<SnackBarType?>(null) }
        LaunchedEffect(Unit) {
            SnackBarController.snackBars.collect { snackBar ->
                currentSnackBar = snackBar
                snackbarHostState.showSnackbar(
                    message = when (snackBar) {
                        is SnackBarType.Success -> snackBar.message
                        is SnackBarType.Error -> snackBar.message
                        is SnackBarType.Warning -> snackBar.message
                        is SnackBarType.Info -> snackBar.message
                        is SnackBarType.Neutral -> snackBar.message
                    }
                )
            }
        }

        JantaNiveshTheme {
            Scaffold(
                containerColor = Color.White,
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState
                    )
                    { data ->
                        val containerColor = when (currentSnackBar) {
                            is SnackBarType.Success -> Color(0xFF2E7D32)
                            is SnackBarType.Error -> Color(0xFFC62828)
                            is SnackBarType.Warning -> Color(0xFFF9A825)
                            is SnackBarType.Info -> Color(0xFF1565C0)
                            is SnackBarType.Neutral, null -> Color.DarkGray
                        }
                        Snackbar(
                            containerColor = containerColor,
                            contentColor = Color.White,
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = data.visuals.message,
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }
                }
            ) {
                AppNavigation()
            }
        }
    }
}