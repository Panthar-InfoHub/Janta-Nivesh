package org.velvetinvesting.jantanivesh.app.core.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.localization.model.LocalizedText

@Composable
fun languageResource(
    key: StringResource
): LocalizedText {
    val translated = stringResource(key)

    var english = ""


    CompositionLocalProvider(
        LocalAppLanguageLocale provides "en"
    ) {
        english = stringResource(key)
    }

    return remember(english, translated) {
        LocalizedText(
            english = english,
            translated = translated
        )
    }
}