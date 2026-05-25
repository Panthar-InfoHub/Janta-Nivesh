package org.velvetinvesting.jantanivesh.app.core.localization

import java.util.Locale

actual fun getDefaultLanguageLocale(): String {
    return Locale.getDefault().toString()
}