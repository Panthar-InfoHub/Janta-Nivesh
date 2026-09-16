package org.velvetinvesting.jantanivesh.notification

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import org.velvetinvesting.jantanivesh.R
import kotlin.random.Random

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class JantaNiveshFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "janta_nivesh_notifications"
        private const val CHANNEL_NAME = "Janta Nivesh Alerts"
        private const val CHANNEL_DESC = "Notifications for updates and transactions"
    }

    override fun onRegistered(installationId: String) {
        super.onRegistered(installationId)
        Log.d("FCM", "Installation ID: $installationId")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM", "Message received: ${remoteMessage.messageId}")
        Log.d("FCM", "Data: ${remoteMessage.data}")
        Log.d("FCM", "Notification: ${remoteMessage.notification}")

        // 1. Check if the message contains a notification payload
        remoteMessage.notification?.let { notification ->
            val title = notification.title ?: "Janta Nivesh"
            val body = notification.body ?: ""
            showNotification(title, body, remoteMessage.data)
            return
        }

        // 2. Fallback: If it's a data-only payload, extract details from the data map
        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"] ?: "Janta Nivesh"
            val body = remoteMessage.data["body"] ?: ""
            showNotification(title, body, remoteMessage.data)
        }
    }

    private fun showNotification(title: String, body: String, data: Map<String, String>) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 3. Create the Notification Channel for Android 8.0 (Oreo) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
            }
            notificationManager.createNotificationChannel(channel)
        }

        // 4. (Optional) Set up click action to open your launcher activity
        // Replace 'MainActivity::class.java' with your target activity if needed
        val intent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            data.forEach { (key, value) -> putExtra(key, value) }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_main)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationId = Random.nextInt(1000, 9999)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
