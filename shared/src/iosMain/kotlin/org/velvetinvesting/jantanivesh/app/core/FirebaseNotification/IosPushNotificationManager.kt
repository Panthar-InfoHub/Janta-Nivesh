package org.velvetinvesting.jantanivesh.app.core.FirebaseNotification

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Implemented in Swift, where the Firebase SDK lives, and handed in through `MainViewController`.
 * The backend registers the device for push by its Firebase Installation ID, as on Android.
 */
interface FirebaseInstallationIdProvider {
    /** Calls [onResult] with the installation ID, or null when Firebase couldn't provide one. */
    fun fetchInstallationId(onResult: (String?) -> Unit)
}

/**
 * The iOS side of [PushNotificationManager]: the Firebase Installation ID, fetched from Swift.
 * A failed fetch reads as blank rather than failing the login it is sent with.
 */
class IosPushNotificationManager(
    private val provider: FirebaseInstallationIdProvider
) : PushNotificationManager {

    override suspend fun getToken(): String = suspendCancellableCoroutine { continuation ->
        provider.fetchInstallationId { id ->
            if (continuation.isActive) continuation.resume(id.orEmpty())
        }
    }
}
