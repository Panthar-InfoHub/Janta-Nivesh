package org.velvetinvesting.jantanivesh.app.core.localization

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale

actual object LocalAppLanguageLocale {

    private var defaultLocal: Locale? = null

    actual val current: String
        @Composable
        get() = LocalLocale.current.platformLocale.toString()

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val configuration = LocalConfiguration.current

        if (LocalInspectionMode.current) {
            val context=LocalContext.current

            val previewConfiguration =
                Configuration(
                    context.resources.configuration
                )
            previewConfiguration.setLocale(
                Locale.forLanguageTag("hi")
            )
            val previewContext =
                context.createConfigurationContext(
                    previewConfiguration
                )
            return LocalContext provides
                    previewContext
        }

        if (defaultLocal==null) {
            defaultLocal= LocalLocale.current.platformLocale
        }

        val newLocale = if (value==null){
            defaultLocal!!
        } else {
            Locale.forLanguageTag(value)
        }
        Locale.setDefault(newLocale)
        configuration.setLocale(newLocale)

        val context = LocalContext.current
        val newContext= context.createConfigurationContext(configuration)

        return LocalContext provides newContext
    }
}