package org.velvetinvesting.jantanivesh.app.core.datastore

    import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun createAndroidDataStore(context: Context): DataStore<Preferences>{
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            context.filesDir
                .resolve(dataStoreFileName)
                .absolutePath
                .toPath()
        }
    )
}