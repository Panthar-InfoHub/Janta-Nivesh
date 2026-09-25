package org.velvetinvesting.jantanivesh.app.core.webview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ObjCSignatureOverride
import org.velvetinvesting.jantanivesh.app.core.deeplink.ExternalAppUrl
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationAction
import platform.WebKit.WKNavigationActionPolicy
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
    onUrlChanged: (String) -> Unit,
    onExternalAppUrl: ((String) -> Unit)?
) {
    val currentOnUrlChanged by rememberUpdatedState(onUrlChanged)
    val currentOnExternalAppUrl by rememberUpdatedState(onExternalAppUrl)

    // WKWebView holds its delegates weakly, so this one must live as long as the web view: keyed
    // on the state alone, with the callbacks read through, rather than rebuilt per new lambda.
    val delegate = remember(state) {
        WebViewNavigationDelegate(
            state = state,
            onUrlChanged = { currentOnUrlChanged(it) },
            onExternalAppUrl = { url ->
                currentOnExternalAppUrl?.let { handler ->
                    handler(url)
                    true
                } ?: false
            }
        )
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
    private val onUrlChanged: (String) -> Unit,
    /** Takes an app link off the web view's hands; false when nobody is listening for them. */
    private val onExternalAppUrl: (String) -> Boolean
) : NSObject(), WKNavigationDelegateProtocol, WKUIDelegateProtocol {

    // The URL a flow ends on is often posted to a host the app owns but nothing serves, so the
    // navigation never commits and no load callback ever carries it. The policy decision is the
    // one point it is always seen, so the URL is reported from there and the load still allowed.
    override fun webView(
        webView: WKWebView,
        decidePolicyForNavigationAction: WKNavigationAction,
        decisionHandler: (WKNavigationActionPolicy) -> Unit
    ) {
        val url = decidePolicyForNavigationAction.request.URL?.absoluteString

        // An app link (gpay://, phonepe:// …) is cancelled here, before WebKit tries it and fails
        // with an unsupported-URL error, and handed off to be opened in the app itself.
        if (handOffExternalAppUrl(url)) {
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
            return
        }

        url?.let(onUrlChanged)
        decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
    }

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
        if (forNavigationAction.targetFrame == null &&
            !handOffExternalAppUrl(forNavigationAction.request.URL?.absoluteString)
        ) {
            webView.loadRequest(forNavigationAction.request)
        }
        return null
    }

    private fun handOffExternalAppUrl(url: String?): Boolean {
        if (url == null || !ExternalAppUrl.isExternalAppUrl(url)) return false
        return onExternalAppUrl(url)
    }

    private fun updateUrl(webView: WKWebView) {
        val url = webView.URL?.absoluteString ?: return
        state.currentUrl = url
        onUrlChanged(url)
    }
}
