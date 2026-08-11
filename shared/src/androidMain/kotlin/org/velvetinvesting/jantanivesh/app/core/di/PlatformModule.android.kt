package org.velvetinvesting.jantanivesh.app.core.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.core.datastore.createAndroidDataStore
import org.velvetinvesting.jantanivesh.app.core.location.AndroidLocationProvider
import org.velvetinvesting.jantanivesh.app.core.location.LocationProvider
import org.velvetinvesting.jantanivesh.app.core.platform.AndroidSharedPreferences
import org.velvetinvesting.jantanivesh.app.core.platform.PdfDownloadManager
import org.velvetinvesting.jantanivesh.app.core.platform.PdfDownloader
import org.velvetinvesting.jantanivesh.app.core.platform.PdfViewer
import org.velvetinvesting.jantanivesh.app.core.platform.PdfViewerAndroid
import org.velvetinvesting.jantanivesh.app.core.platform.SharedPreference
import org.velvetinvesting.jantanivesh.app.core.utils.AndroidBrowserLauncher
import org.velvetinvesting.jantanivesh.app.core.utils.BrowserLauncher
import org.velvetinvesting.jantanivesh.app.core.utils.deviceinfo.DeviceInfoRetriever
import org.velvetinvesting.jantanivesh.app.core.utils.deviceinfo.DeviceInfoRetrieverAndroid

actual val platformModule: Module = module{
    single { createAndroidDataStore(get()) }
    single<SharedPreference> { AndroidSharedPreferences(get()) }
    single<DeviceInfoRetriever> { DeviceInfoRetrieverAndroid(get()) }
    single<PdfViewer> { PdfViewerAndroid(get()) }
    single<BrowserLauncher> { AndroidBrowserLauncher(get()) }
    single<PdfDownloadManager> { PdfDownloader(get(), get()) }
    single<LocationProvider> { AndroidLocationProvider(get()) }
}