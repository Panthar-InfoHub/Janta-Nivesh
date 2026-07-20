package org.velvetinvesting.jantanivesh.app.core.webview

object WebViewExitUrlMatcher {

    fun matches(
        currentUrl: String,
        patterns: List<String>,
        matchType: WebViewUrlMatchType
    ): Boolean {
        if (patterns.isEmpty()) return false
        val url = currentUrl.lowercase()
        return patterns.any { pattern ->
            val needle = pattern.lowercase()
            when (matchType) {
                WebViewUrlMatchType.CONTAINS -> url.contains(needle)
                WebViewUrlMatchType.STARTS_WITH -> url.startsWith(needle)
                WebViewUrlMatchType.EXACT -> url == needle
            }
        }
    }
}
