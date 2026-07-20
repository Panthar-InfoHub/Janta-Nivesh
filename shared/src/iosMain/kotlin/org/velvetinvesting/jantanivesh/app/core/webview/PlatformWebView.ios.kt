package org.velvetinvesting.jantanivesh.app.core.webview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ObjCSignatureOverride
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationAction
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKUIDelegateProtocol
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.WebKit.WKWindowFeatures
import platform.darwin.NSObject

@Composable
actual fun PlatformWebView(
    state: WebViewState,
    modifier: Modifier,
    onUrlChanged: (String) -> Unit
) {
    val delegate = remember(state, onUrlChanged) {
        WebViewNavigationDelegate(state, onUrlChanged)
    }

    UIKitView(
        factory = {
            val webView = WKWebView()
            webView.navigationDelegate = delegate
            webView.UIDelegate = delegate
            NSURL.URLWithString(state.currentUrl)?.let { nsUrl ->
                webView.loadRequest(NSURLRequest.requestWithURL(nsUrl))
            }
            webView
        },
        modifier = modifier
    )
}

private class WebViewNavigationDelegate(
    private val state: WebViewState,
    private val onUrlChanged: (String) -> Unit
) : NSObject(), WKNavigationDelegateProtocol, WKUIDelegateProtocol {

    @ObjCSignatureOverride
    override fun webView(webView: WKWebView, didStartProvisionalNavigation: WKNavigation?) {
        state.isLoading = true
        updateUrl(webView)
    }

    @ObjCSignatureOverride
    override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
        state.isLoading = false
        state.canGoBack = webView.canGoBack
        updateUrl(webView)
    }

    // Payment/e-sign checkout widgets often render their modal via
    // window.open() rather than an in-page element. WKWebView never opens
    // a real new window on its own; without this delegate the call is a
    // silent no-op. Loading the request in the same webview instead is
    // the standard fix for keeping everything inside one embedded view.
    @ObjCSignatureOverride
    override fun webView(
        webView: WKWebView,
        createWebViewWithConfiguration: WKWebViewConfiguration,
        forNavigationAction: WKNavigationAction,
        windowFeatures: WKWindowFeatures
    ): WKWebView? {
        if (forNavigationAction.targetFrame == null) {
            webView.loadRequest(forNavigationAction.request)
        }
        return null
    }

    private fun updateUrl(webView: WKWebView) {
        val url = webView.URL?.absoluteString ?: return
        state.currentUrl = url
        onUrlChanged(url)
    }
}
