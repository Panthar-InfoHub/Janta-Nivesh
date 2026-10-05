package org.velvetinvesting.jantanivesh

import android.app.Application
import com.posthog.android.PostHogAndroid
import com.posthog.android.PostHogAndroidConfig
import org.PostHogDefaults
import org.koin.android.ext.koin.androidContext
import org.velvetinvesting.jantanivesh.app.core.di.initializeKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initializeKoin(
            extaModules = listOf(androidModule)
        ) {
            androidContext(this@MyApplication)
        }

        val config = PostHogAndroidConfig(
            apiKey = PostHogDefaults.token,
            host = PostHogDefaults.host
        ).apply {
            errorTrackingConfig.autoCapture = true
        }

        config.sessionReplay=true
        config.sessionReplayConfig.maskAllTextInputs = false
        config.sessionReplayConfig.maskAllImages = false
        config.sessionReplayConfig.screenshot = true


        PostHogAndroid.setup(this, config)

    }
}