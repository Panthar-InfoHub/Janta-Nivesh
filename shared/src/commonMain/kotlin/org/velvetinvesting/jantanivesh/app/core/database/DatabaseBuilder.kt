package org.velvetinvesting.jantanivesh.app.core.database

import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

/**
 * The half of database creation that is the same everywhere. Only the file path differs per
 * platform, so each platform builds the [RoomDatabase.Builder] and hands it here to be finished.
 */
fun RoomDatabase.Builder<JantaNiveshDatabase>.buildJantaNiveshDatabase(): JantaNiveshDatabase =
    this
        .setDriver(BundledSQLiteDriver())
        // Room needs a context to run queries off the main thread. Dispatchers.IO is not
        // available in common code, and Default is backed by a real thread pool on both targets.
        .setQueryCoroutineContext(Dispatchers.Default)
        .build()
