package org.velvetinvesting.jantanivesh

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.core.FirebaseNotification.FirebaseInstallationIdProvider
import org.velvetinvesting.jantanivesh.app.core.FirebaseNotification.IosPushNotificationManager
import org.velvetinvesting.jantanivesh.app.core.FirebaseNotification.PushNotificationManager
import org.velvetinvesting.jantanivesh.app.core.di.initializeKoin

/** [installationIdProvider] comes from Swift, which owns the Firebase SDK. */
fun MainViewController(installationIdProvider: FirebaseInstallationIdProvider) = ComposeUIViewController(
    configure = {
        initializeKoin(
            extaModules = listOf(
                module {
                    single<PushNotificationManager> { IosPushNotificationManager(installationIdProvider) }
                }
            )
        )
    }
) { App() }
