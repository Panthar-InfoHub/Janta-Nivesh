package org.velvetinvesting.jantanivesh.app.core.deeplink

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import kotlin.coroutines.resume

class IosExternalAppLauncher : ExternalAppLauncher {

    // openURL's completion reports whether an app took the URL, which needs no
    // LSApplicationQueriesSchemes entry — unlike canOpenURL, which would for every UPI app.
    override suspend fun launch(url: String): Boolean {
        val nsUrl = NSURL.URLWithString(url) ?: return false

        return withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { continuation ->
                UIApplication.sharedApplication.openURL(
                    nsUrl,
                    options = emptyMap<Any?, Any>(),
                    completionHandler = { success ->
                        if (continuation.isActive) continuation.resume(success)
                    }
                )
            }
        }
    }
}
