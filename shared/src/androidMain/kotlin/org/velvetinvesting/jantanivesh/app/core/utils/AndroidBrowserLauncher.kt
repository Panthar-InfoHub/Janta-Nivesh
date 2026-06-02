package org.velvetinvesting.jantanivesh.app.core.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri

class AndroidBrowserLauncher(
    private val context: Context,
) : BrowserLauncher {

    override fun launchBrowser(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())

            val chooser = Intent.createChooser(intent, "Complete KYC with : ").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(chooser)
        } catch (e: Exception) {
            Log.d("AndroidBrowserLauncher Exception", "launchBrowser: $e")
        }
    }
}