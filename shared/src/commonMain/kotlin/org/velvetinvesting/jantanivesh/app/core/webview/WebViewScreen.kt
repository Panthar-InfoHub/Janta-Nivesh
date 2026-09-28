package org.velvetinvesting.jantanivesh.app.core.webview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.icon_cross
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.velvetinvesting.jantanivesh.app.core.deeplink.ExternalAppLauncher
import org.velvetinvesting.jantanivesh.app.core.deeplink.ExternalAppUrl
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.utils.AppBackHandler
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun WebViewScreen(
    config: WebViewConfig,
    onExitUrlReached: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    /**
     * Whether an app link the page leads to is opened in its app. Off, it is only blocked — the
     * page never loads app links either way; see [PlatformWebView].
     */
    interceptExternalAppUrls: Boolean = true,
    /**
     * How an app link is opened; returns whether an app took it. Defaults to opening it straight
     * away — a caller that has to know when the user comes back supplies its own.
     */
    openExternalApp: (suspend (String) -> Boolean)? = null,
    /** Drawn over the web content (not the header), e.g. while the user is away in another app. */
    overlay: @Composable BoxScope.() -> Unit = {}
) {
    val state = rememberWebViewState(config.url)
    val externalAppLauncher: ExternalAppLauncher = koinInject()
    val scope = rememberCoroutineScope()

    // The exit URL can now be seen by more than one platform callback (the request, the page
    // load, the load error), and the user can close the page by the cross or the system back as
    // well, so the hand-back is latched to whichever comes first.
    val exitReported = remember { mutableStateOf(false) }

    val close: () -> Unit = {
        if (!exitReported.value) {
            exitReported.value = true
            onBackClick()
        }
    }

    // An app link (gpay://, phonepe:// …) is opened in its app while this page stays put and
    // carries on with its own status check and exit URL. With the app missing, the link is never
    // loaded, so the page is still usable for another option.
    val handleExternalAppUrl: (String) -> Unit = { url ->
        scope.launch {
            try {
                val opened = openExternalApp?.invoke(url) ?: externalAppLauncher.launch(url)
                if (!opened) {
                    SnackBarController.showError(ExternalAppUrl.appNotInstalledMessage(url))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                SnackBarController.showError(ExternalAppUrl.appNotInstalledMessage(url))
            }
        }
    }

    // System back leaves the page the same way the cross does, so the caller always hears
    // about it — a bare pop would skip whatever check the caller runs on return.
    AppBackHandler(enabled = !exitReported.value, onBack = close)

    Column(modifier = modifier.fillMaxSize()) {
        WebViewHeader(
            heading = config.title.orEmpty(),
            showBack = true,
            onBackClick = close
        )

        Box(modifier = Modifier.fillMaxSize()) {
            PlatformWebView(
                state = state,
                modifier = Modifier.fillMaxSize(),
                onUrlChanged = { url ->
                    if (!exitReported.value &&
                        WebViewExitUrlMatcher.matches(
                            url,
                            config.exitUrlPatterns,
                            config.matchType
                        )
                    ) {
                        exitReported.value = true
                        onExitUrlReached(url)
                    }
                },
                onExternalAppUrl = if (interceptExternalAppUrls) handleExternalAppUrl else null
            )

            overlay()
        }
    }
}


@Composable
private fun WebViewHeader(
    heading: String,
    showBack: Boolean = false,
    modifier:Modifier=Modifier.fillMaxWidth(),
    onBackClick: () -> Unit={},
    rightContent: @Composable () -> Unit={}
){
    Box(
        modifier=modifier
            .background(Color.White)
            .padding(bottom = 16.dp, start = 12.dp, end = 12.dp, top = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = heading,
            style = MaterialTheme.typography.headlineSmall,
            color = Primary
        )

        if (showBack){
            Icon(
                painter = painterResource(Res.drawable.icon_cross),
                contentDescription = null,
                modifier = Modifier.size(22.dp).clickable(
                    onClick = onBackClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ).align(Alignment.CenterStart)
            )
        }

        Box(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            rightContent()
        }
    }
}