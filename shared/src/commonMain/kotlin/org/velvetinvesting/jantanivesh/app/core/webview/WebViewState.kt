package org.velvetinvesting.jantanivesh.app.core.webview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class WebViewState(initialUrl: String) {
    var currentUrl by mutableStateOf(initialUrl)
        internal set
    var isLoading by mutableStateOf(true)
        internal set
    var canGoBack by mutableStateOf(false)
        internal set
    var errorMessage by mutableStateOf<String?>(null)
        internal set
}

@Composable
fun rememberWebViewState(initialUrl: String): WebViewState {
    return remember(initialUrl) { WebViewState(initialUrl) }
}
