package org.velvetinvesting.jantanivesh.app.core.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale


private val LocalLanguage = staticCompositionLocalOf {
    Locale.getDefault().toLanguageTag()
}

actual object LocalAppLanguageLocale {

    private var defaultLocale: String? = null

    actual val current: String
        @Composable
        get() = LocalLanguage.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {

        if (defaultLocale == null) {
            defaultLocale = LocalLocale.current.platformLocale.toLanguageTag()
        }

        val language = value ?: defaultLocale!!

        Locale.setDefault(
            Locale.forLanguageTag(language)
        )

        return LocalLanguage provides language
    }
}