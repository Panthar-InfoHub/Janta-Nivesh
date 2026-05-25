package org.velvetinvesting.jantanivesh.app.core.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue

expect object LocalAppLanguageLocale {
    val current: String @Composable get

    @Composable infix fun provides(value: String?) : ProvidedValue<*>
}