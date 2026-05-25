package org.velvetinvesting.jantanivesh.app.core.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.core.datastore.createAndroidDataStore

actual val platformModule: Module = module{
    single { createAndroidDataStore(get()) }
}