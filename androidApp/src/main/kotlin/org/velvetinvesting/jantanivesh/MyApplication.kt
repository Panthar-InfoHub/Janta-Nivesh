package org.velvetinvesting.jantanivesh

import android.app.Application
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
    }
}