package org.velvetinvesting.jantanivesh.app.core.localization

import org.velvetinvesting.jantanivesh.app.core.localization.model.AppLanguage
import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

actual fun getDefaultLanguageLocale(): String {
    return (NSLocale.preferredLanguages.firstOrNull() as? String) ?: AppLanguage.HINDI.code
}