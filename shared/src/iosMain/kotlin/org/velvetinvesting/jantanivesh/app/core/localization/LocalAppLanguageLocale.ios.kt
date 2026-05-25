package org.velvetinvesting.jantanivesh.app.core.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import platform.Foundation.NSUserDefaults

actual object LocalAppLanguageLocale {

    private val defaultLocal = getDefaultLanguageLocale()
    private val languageKey = "AppleLanguages"

    private val LocalAppLanguageLocale = staticCompositionLocalOf { defaultLocal }

    actual val current: String
        @Composable
        get() = LocalAppLanguageLocale.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val newLocale = value?: defaultLocal
        if (value==null){
            NSUserDefaults.standardUserDefaults.removeObjectForKey(languageKey)
        }else{
            NSUserDefaults.standardUserDefaults.setObject(
                listOf(newLocale),
                languageKey
            )
        }

        return LocalAppLanguageLocale provides newLocale
    }
}