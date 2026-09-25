package org.velvetinvesting.jantanivesh.app.core.deeplink

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri

class AndroidExternalAppLauncher(
    private val context: Context,
) : ExternalAppLauncher {

    // No resolveActivity() check up front: since Android 11 it needs a <queries> entry per app to
    // see anything, so just starting the activity and catching the miss is the reliable test.
    override suspend fun launch(url: String): Boolean {
        return try {
            context.startActivity(
                url.toLaunchIntent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            true
        } catch (e: ActivityNotFoundException) {
            Log.d(TAG, "No app installed for $url")
            false
        } catch (e: Exception) {
            Log.e(TAG, "Could not open $url", e)
            false
        }
    }

    /**
     * An `intent://…#Intent;…;end` link is parsed the way Chrome does it: stripped of any explicit
     * component or selector so a page can only reach browsable entry points, never an arbitrary
     * activity. Everything else is a plain view of the URI.
     */
    private fun String.toLaunchIntent(): Intent =
        if (startsWith("intent:", ignoreCase = true)) {
            Intent.parseUri(this, Intent.URI_INTENT_SCHEME).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                component = null
                selector = null
            }
        } else {
            Intent(Intent.ACTION_VIEW, toUri())
        }

    private companion object {
        const val TAG = "ExternalAppLauncher"
    }
}
