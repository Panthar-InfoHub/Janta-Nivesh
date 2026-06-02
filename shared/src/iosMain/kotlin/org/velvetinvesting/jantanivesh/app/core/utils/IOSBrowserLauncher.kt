package org.velvetinvesting.jantanivesh.app.core.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class IOSBrowserLauncher : BrowserLauncher {

    override fun launchBrowser(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        UIApplication.sharedApplication.openURL(
            nsUrl,
            options = emptyMap<Any?, Any>(),
            completionHandler = null
        )
    }
}