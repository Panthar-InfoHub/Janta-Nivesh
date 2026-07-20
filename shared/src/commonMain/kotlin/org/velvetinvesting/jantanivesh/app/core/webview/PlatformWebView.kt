package org.velvetinvesting.jantanivesh.app.core.webview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformWebView(
    state: WebViewState,
    modifier: Modifier,
    onUrlChanged: (String) -> Unit
)
