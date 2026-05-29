package org.velvetinvesting.jantanivesh.app.core.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.core.datastore.createIosDataStore
import org.velvetinvesting.jantanivesh.app.core.platform.IosSharedPreferences
import org.velvetinvesting.jantanivesh.app.core.platform.SharedPreference
import org.velvetinvesting.jantanivesh.app.core.utils.deviceinfo.DeviceInfoRetriever
import org.velvetinvesting.jantanivesh.app.core.utils.deviceinfo.DeviceInfoRetrieverIos

actual val platformModule: Module = module {
    single { createIosDataStore() }
    single<SharedPreference> { IosSharedPreferences() }
    single<DeviceInfoRetriever> { DeviceInfoRetrieverIos() }
}