package org.velvetinvesting.jantanivesh.app.core.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Message
import android.util.Log
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun PlatformWebView(
    state: WebViewState,
    modifier: Modifier,
    onUrlChanged: (String) -> Unit
) {
    val context = LocalContext.current

    val webView = remember {
        WebView.setWebContentsDebuggingEnabled(true)

        WebView(context).apply {
            setLayerType(WebView.LAYER_TYPE_HARDWARE, null)

            settings.apply {
                databaseEnabled = true
                javaScriptEnabled = true
                domStorageEnabled = true
                blockNetworkLoads = false
                blockNetworkImage = false

                javaScriptCanOpenWindowsAutomatically = true
                setSupportMultipleWindows(true)

                allowFileAccess = true
                allowContentAccess = true

                useWideViewPort = true
                loadWithOverviewMode = true
                loadsImagesAutomatically = true

                mediaPlaybackRequiresUserGesture = false
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            Log.d(
                "WEB-UA",
                settings.userAgentString
            )

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            webViewClient = object : WebViewClient() {

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {

                    Log.d(
                        "WEB-REQUEST",
                        """
        URL: ${request?.url}
        Method: ${request?.method}
        Headers:
        ${request?.requestHeaders}
        """.trimIndent()
                    )

                    return super.shouldInterceptRequest(view, request)
                }

                override fun onReceivedHttpError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    errorResponse: WebResourceResponse?
                ) {

                    Log.e(
                        "WEB-HTTP",
                        """
        URL: ${request?.url}
        Status: ${errorResponse?.statusCode}
        Reason: ${errorResponse?.reasonPhrase}
        """.trimIndent()
                    )

                    super.onReceivedHttpError(view, request, errorResponse)
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {

                    Log.e(
                        "WEB-ERROR",
                        """
        URL: ${request?.url}
        Code: ${error?.errorCode}
        Description: ${error?.description}
        """.trimIndent()
                    )

                    super.onReceivedError(view, request, error)
                }

                override fun onPageStarted(
                    view: WebView?,
                    url: String?,
                    favicon: Bitmap?
                ) {
                    super.onPageStarted(view, url, favicon)

                    Log.d("WEB", "Started : $url")

                    state.isLoading = true
                    state.canGoBack = view?.canGoBack() ?: false

                    url?.let {
                        state.currentUrl = it
                        onUrlChanged(it)
                    }
                }

                override fun onPageFinished(
                    view: WebView?,
                    url: String?
                ) {
                    super.onPageFinished(view, url)

                    view?.evaluateJavascript(
                        """
    (function(){

        if(window.__networkHookInstalled)
            return;

        window.__networkHookInstalled = true;

        const oldFetch = window.fetch;

        window.fetch = function(){

            console.log(
                "[FETCH]",
                arguments[0]
            );

            return oldFetch.apply(this, arguments);
        };

        const oldOpen =
            XMLHttpRequest.prototype.open;

        XMLHttpRequest.prototype.open =
            function(method, url){

                console.log(
                    "[XHR]",
                    method,
                    url
                );

                return oldOpen.apply(this, arguments);
            };

    })();
    """.trimIndent(),
                        null
                    )

                    CookieManager.getInstance().flush()

                    Log.d(
                        "WEB-COOKIE",
                        CookieManager.getInstance().getCookie(url ?: "").orEmpty()
                    )

                    Log.d("WEB", "Finished : $url")

                    state.isLoading = false
                    state.canGoBack = view?.canGoBack() ?: false

                    url?.let {
                        state.currentUrl = it
                        onUrlChanged(it)
                    }
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    Log.d("WEB", "Loading : ${request?.url}")
                    return false
                }
            }

            webChromeClient = object : WebChromeClient() {

                override fun onConsoleMessage(
                    consoleMessage: ConsoleMessage
                ): Boolean {

                    Log.d(
                        "WEB-CONSOLE",
                        """
        ${consoleMessage.messageLevel()}
        ${consoleMessage.message()}
        ${consoleMessage.sourceId()}
        line ${consoleMessage.lineNumber()}
        """.trimIndent()
                    )

                    return super.onConsoleMessage(consoleMessage)
                }


                override fun onCreateWindow(
                    view: WebView?,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message?
                ): Boolean {

                    Log.d("ANDWEB", "onCreateWindow()")

                    val transport =
                        resultMsg?.obj as? WebView.WebViewTransport ?: return false

                    val popup = WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.javaScriptCanOpenWindowsAutomatically = true
                        settings.setSupportMultipleWindows(true)
                    }

                    popup.webViewClient = object : WebViewClient() {

                        override fun shouldOverrideUrlLoading(
                            child: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {

                            view?.loadUrl(
                                request?.url.toString()
                            )

                            child?.destroy()

                            return true
                        }

                        override fun onPageStarted(
                            child: WebView?,
                            url: String?,
                            favicon: Bitmap?
                        ) {

                            url?.let {
                                view?.loadUrl(it)
                            }

                            child?.destroy()
                        }
                    }
                    transport.webView = popup
                    resultMsg.sendToTarget()

                    return true
                }
            }

            loadUrl(state.currentUrl)
        }
    }

    webView.settings.userAgentString =
        "Mozilla/5.0 (Linux; Android 15; Pixel 9) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Mobile Safari/537.36"

    Log.d("UA", webView.settings.userAgentString)

    DisposableEffect(webView) {
        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }

    LaunchedEffect(state.currentUrl) {
        if (webView.url != state.currentUrl) {
            webView.loadUrl(state.currentUrl)
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            FrameLayout(it).apply {
                if (webView.parent != null) {
                    (webView.parent as ViewGroup).removeView(webView)
                }
                addView(webView)
            }
        }
    )
}