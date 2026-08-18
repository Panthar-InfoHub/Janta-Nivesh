package org.velvetinvesting.jantanivesh.app.core.location

import androidx.compose.runtime.Composable

fun interface LocationPermissionRequester {
    /**
     * Asks for foreground location permission. Safe to call when permission is already
     * granted or permanently denied — the callback then fires without showing a prompt, so
     * callers can treat this as "make sure we have permission, then tell me the answer".
     */
    fun request()
}

/**
 * Returns a requester bound to the current composition. The permission prompt has to be raised
 * from the UI layer on both platforms, which is why this is a composable rather than a method on
 * [LocationProvider].
 */
@Composable
expect fun rememberLocationPermissionRequester(
    onResult: (granted: Boolean) -> Unit
): LocationPermissionRequester
