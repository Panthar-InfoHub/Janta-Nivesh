package org.velvetinvesting.jantanivesh.app.core.webview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * @param onExternalAppUrl when set, any navigation to an app link (see
 * [org.velvetinvesting.jantanivesh.app.core.deeplink.ExternalAppUrl]) is cancelled before the web
 * view tries to load it and handed here instead. Left null, such links load as they always did.
 */
@Composable
expect fun PlatformWebView(
    state: WebViewState,
    modifier: Modifier,
    onUrlChanged: (String) -> Unit,
    onExternalAppUrl: ((String) -> Unit)? = null
)
