package org.velvetinvesting.jantanivesh.app.core.deeplink

/**
 * Opens a deep link (`gpay://`, `phonepe://`, `upi://`, Android `intent://` …) in the app that owns
 * it. Unlike [org.velvetinvesting.jantanivesh.app.core.utils.BrowserLauncher] there is no browser
 * to fall back to: these links are app-specific, so when the app is missing the link simply fails.
 */
interface ExternalAppLauncher {

    /**
     * Hands [url] to whichever installed app handles it. Returns false when no app took it —
     * never throws, so a missing app can be reported rather than crash the caller.
     */
    suspend fun launch(url: String): Boolean
}
