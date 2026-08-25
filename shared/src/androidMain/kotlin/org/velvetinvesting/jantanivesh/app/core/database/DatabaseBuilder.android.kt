package org.velvetinvesting.jantanivesh.app.core.database

import android.content.Context
import androidx.room3.Room

/**
 * Built against the application context and an absolute path, matching how the DataStore file is
 * placed — the database lives in the app's private storage and goes away with the app.
 */
fun createAndroidDatabase(context: Context): JantaNiveshDatabase {
    val dbFile = context.applicationContext.getDatabasePath(DATABASE_FILE_NAME)

    return Room.databaseBuilder<JantaNiveshDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    ).buildJantaNiveshDatabase()
}
