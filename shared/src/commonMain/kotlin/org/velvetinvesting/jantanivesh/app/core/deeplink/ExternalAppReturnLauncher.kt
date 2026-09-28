package org.velvetinvesting.jantanivesh.app.core.deeplink

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.koin.compose.koinInject
import kotlin.coroutines.cancellation.CancellationException

/**
 * Opens an app link and calls back once the user has gone to that app and come back — the deep
 * link counterpart of [org.velvetinvesting.jantanivesh.app.features.core.utils.BrowserReturnLauncher].
 *
 * Any resume after this app was paused (ON_PAUSE) counts as the return — not only one after it
 * was fully backgrounded (ON_STOP). The Android app chooser that a generic `upi://` link opens, an
 * iOS "Open in…" prompt, and UPI apps that pay in a bottom sheet all sit on top of this app
 * without stopping it; dismissing or finishing them resumes it straight from paused.
 *
 * The return reports which of the two it was: `wasStopped` means a full-screen app took over, so
 * the user most likely went off to pay; without it they may only have dismissed a tray or prompt.
 * A resume with no pause before it (a navigation transition finishing, say) is not a return.
 */
class ExternalAppReturnLauncher(
    private val externalAppLauncher: ExternalAppLauncher,
) {

    /** True from the launch until the user returns or the wait is dropped; drives UI. */
    var isAwaitingReturn by mutableStateOf(false)
        private set

    /** Set once something has come up over this app since the launch. */
    private var wentAway = false
    /** Set once this app was fully backgrounded since the launch. */
    private var wasStopped = false
    private var onReturn: ((wasStopped: Boolean) -> Unit)? = null

    /**
     * Opens [url] and returns whether an app took it; [onReturn] runs when the user comes back,
     * told whether this app was fully backgrounded meanwhile.
     */
    suspend fun launch(
        url: String,
        onReturn: (wasStopped: Boolean) -> Unit,
    ): Boolean {
        // Armed before the launch, not after: on iOS the app can be backgrounded before the open
        // call reports back, and that pause must not be missed.
        isAwaitingReturn = true
        wentAway = false
        wasStopped = false
        this.onReturn = onReturn

        val opened = try {
            externalAppLauncher.launch(url)
        } catch (e: CancellationException) {
            reset()
            throw e
        } catch (e: Exception) {
            false
        }

        if (!opened) reset()
        return opened
    }

    fun handleLifecycleEvent(event: Lifecycle.Event) {
        if (!isAwaitingReturn) return

        when (event) {
            Lifecycle.Event.ON_PAUSE -> wentAway = true

            Lifecycle.Event.ON_STOP -> {
                wentAway = true
                wasStopped = true
            }

            // A resume only follows a pause, so without one there is nothing to return from yet.
            Lifecycle.Event.ON_RESUME -> if (wentAway) {
                val callback = onReturn
                val stopped = wasStopped
                reset()
                callback?.invoke(stopped)
            }

            else -> Unit
        }
    }

    fun reset() {
        isAwaitingReturn = false
        wentAway = false
        wasStopped = false
        onReturn = null
    }
}

@Composable
fun rememberExternalAppReturnLauncher(): ExternalAppReturnLauncher {
    val externalAppLauncher: ExternalAppLauncher = koinInject()
    val lifecycleOwner = LocalLifecycleOwner.current

    val launcher = remember(externalAppLauncher) {
        ExternalAppReturnLauncher(externalAppLauncher)
    }

    DisposableEffect(lifecycleOwner, launcher) {
        val observer = LifecycleEventObserver { _, event ->
            launcher.handleLifecycleEvent(event)
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            launcher.reset()
        }
    }

    return launcher
}
