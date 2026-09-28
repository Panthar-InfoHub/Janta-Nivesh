package org.velvetinvesting.jantanivesh.app.core.webview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * App links (see [org.velvetinvesting.jantanivesh.app.core.deeplink.ExternalAppUrl]) are never
 * loaded in the page — not when tapped, not when the page redirects to one by itself — since the
 * web view can only fail on them.
 *
 * @param onExternalAppUrl gets each such link, to open it in its app. Left null, the link is
 * simply blocked.
 */
@Composable
expect fun PlatformWebView(
    state: WebViewState,
    modifier: Modifier,
    onUrlChanged: (String) -> Unit,
    onExternalAppUrl: ((String) -> Unit)? = null
)
