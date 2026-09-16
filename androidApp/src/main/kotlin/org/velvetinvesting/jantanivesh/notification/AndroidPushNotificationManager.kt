package org.velvetinvesting.jantanivesh.notification

import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.tasks.await
import org.velvetinvesting.jantanivesh.app.core.FirebaseNotification.PushNotificationManager

class AndroidPushNotificationManager: PushNotificationManager {
    override suspend fun getToken(): String {
        val id = FirebaseInstallations.getInstance().id.await()
        return id
    }
}