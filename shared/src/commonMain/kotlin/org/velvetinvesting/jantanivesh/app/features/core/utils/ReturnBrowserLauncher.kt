package org.velvetinvesting.jantanivesh.app.features.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.koin.compose.koinInject
import org.velvetinvesting.jantanivesh.app.core.utils.BrowserLauncher

class BrowserReturnLauncher(
    private val browserLauncher: BrowserLauncher,
) {

    private var awaitingReturn = false
    private var onReturn: (() -> Unit)? = null

    fun launch(
        url: String,
        onReturn: () -> Unit,
    ) {
        awaitingReturn = true
        this.onReturn = onReturn

        browserLauncher.launchBrowser(url)
    }

    fun handleResume() {
        if (awaitingReturn) {
            awaitingReturn = false
            onReturn?.invoke()
            onReturn = null
        }
    }
}

@Composable
fun rememberBrowserReturnLauncher(
): BrowserReturnLauncher {
    val browserLauncher: BrowserLauncher = koinInject()
    val lifecycleOwner = LocalLifecycleOwner.current

    val launcher = remember(browserLauncher) {
        BrowserReturnLauncher(browserLauncher)
    }

    DisposableEffect(lifecycleOwner, launcher) {

        val observer = LifecycleEventObserver { _, event ->

            if (event == Lifecycle.Event.ON_RESUME) {
                launcher.handleResume()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    return launcher
}