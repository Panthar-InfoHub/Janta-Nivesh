package org.velvetinvesting.jantanivesh.app.core.FirebaseNotification

interface PushNotificationManager {
    suspend fun getToken() : String
}