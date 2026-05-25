package org.velvetinvesting.jantanivesh.app.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences

fun createDataStore(storage: Storage<Preferences>): DataStore<Preferences> =
    DataStoreFactory.create(storage = storage)

internal const val dataStoreFileName = "janta_nivesh_settings.preferences_pb"