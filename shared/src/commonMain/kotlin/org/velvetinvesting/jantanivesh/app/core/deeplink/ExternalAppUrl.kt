package org.velvetinvesting.jantanivesh.app.core.deeplink

/**
 * Tells a URL the web view can load itself apart from one only another app can open. Anything
 * whose scheme is not a web one is treated as an app link, since payment pages link to each UPI
 * app by its own scheme (`gpay://`, `phonepe://`, `paytmmp://`) rather than a generic one.
 */
object ExternalAppUrl {

    /** Schemes the web view resolves on its own; loading these must never leave the page. */
    private val webViewSchemes = setOf(
        "http", "https", "about", "data", "blob", "javascript", "file", "content"
    )

    private val appNames = mapOf(
        "gpay" to "Google Pay",
        "tez" to "Google Pay",
        "phonepe" to "PhonePe",
        "paytm" to "Paytm",
        "paytmmp" to "Paytm",
        "bhim" to "BHIM",
        "credpay" to "CRED",
        "cred" to "CRED"
    )

    fun isExternalAppUrl(url: String?): Boolean {
        val scheme = schemeOf(url) ?: return false
        return scheme !in webViewSchemes
    }

    /** What to tell the user when no installed app took [url]. */
    fun appNotInstalledMessage(url: String): String {
        val scheme = targetSchemeOf(url)
        val appName = scheme?.let(appNames::get)
        return when {
            appName != null -> "$appName is not installed on this device. Please choose another payment option."
            scheme == "upi" -> "No UPI app found on this device. Please choose another payment option."
            else -> "The app for this payment option is not installed. Please choose another payment option."
        }
    }

    /**
     * The scheme the link is really aimed at. An Android `intent://` link carries it in its
     * fragment (`#Intent;scheme=gpay;…;end`) rather than up front.
     */
    private fun targetSchemeOf(url: String): String? {
        val scheme = schemeOf(url) ?: return null
        if (scheme != "intent") return scheme
        return url.substringAfter(";scheme=", missingDelimiterValue = "")
            .substringBefore(';')
            .lowercase()
            .ifBlank { null }
    }

    /** The URL's scheme, lower-cased, or null when it has none (a relative URL, a blank string). */
    private fun schemeOf(url: String?): String? {
        if (url.isNullOrBlank()) return null
        val scheme = url.trim().substringBefore(':', missingDelimiterValue = "")
        if (scheme.isEmpty() || !scheme.first().isLetter()) return null
        if (scheme.any { !it.isLetterOrDigit() && it != '+' && it != '-' && it != '.' }) return null
        return scheme.lowercase()
    }
}
