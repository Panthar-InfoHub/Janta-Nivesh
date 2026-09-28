package org.velvetinvesting.jantanivesh.app.core.webview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/**
 * A UPI payment page whose only job is to turn a payment link into the UPI app link it redirects
 * to (`decpay…?app=gpay` → `gpay://…`, or a generic `upi://…` from the QR page). It never opens
 * that app itself: the link is handed to [onAppLinkCaught] and the page is done, so the caller
 * opens the app from its own screen and watches for the user's return there — nobody is left on
 * a blank page behind the app, or behind a dismissed app chooser.
 *
 * Until an app link turns up it is the plain [WebViewScreen]: the exit URL, the cross and back all
 * behave as they do there.
 *
 * @param onAppLinkCaught gets the app link, once; the caller must close the page and open it.
 */
@Composable
fun UpiPaymentWebViewScreen(
    config: WebViewConfig,
    onAppLinkCaught: (String) -> Unit,
    onExitUrlReached: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnAppLinkCaught by rememberUpdatedState(onAppLinkCaught)
    val currentOnExitUrlReached by rememberUpdatedState(onExitUrlReached)
    val currentOnBackClick by rememberUpdatedState(onBackClick)

    // The app link, the cross, back and the exit URL can race each other; the first one wins so
    // the caller is only ever handed the page once.
    var finished by remember { mutableStateOf(false) }

    fun finish(action: () -> Unit) {
        if (!finished) {
            finished = true
            action()
        }
    }

    WebViewScreen(
        config = config,
        onExitUrlReached = { url -> finish { currentOnExitUrlReached(url) } },
        onBackClick = { finish { currentOnBackClick() } },
        modifier = modifier,
        openExternalApp = { url ->
            finish { currentOnAppLinkCaught(url) }
            // Handled — opening it, and saying if its app is missing, is the caller's job now.
            true
        }
    )
}
