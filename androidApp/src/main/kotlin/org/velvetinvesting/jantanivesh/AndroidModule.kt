package org.velvetinvesting.jantanivesh

import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.core.FirebaseNotification.PushNotificationManager
import org.velvetinvesting.jantanivesh.notification.AndroidPushNotificationManager

val androidModule= module {
    single<PushNotificationManager> { AndroidPushNotificationManager() }
}