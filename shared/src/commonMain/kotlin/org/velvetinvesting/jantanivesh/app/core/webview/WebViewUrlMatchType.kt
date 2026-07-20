package org.velvetinvesting.jantanivesh.app.core.webview

import kotlinx.serialization.Serializable

@Serializable
enum class WebViewUrlMatchType {
    CONTAINS,
    STARTS_WITH,
    EXACT
}
