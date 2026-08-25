package org.velvetinvesting.jantanivesh.app.core.database

import androidx.room3.Room
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/** Same documents directory the DataStore file uses, so both are backed up and cleared alike. */
@OptIn(ExperimentalForeignApi::class)
fun createIosDatabase(): JantaNiveshDatabase {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )

    return Room.databaseBuilder<JantaNiveshDatabase>(
        name = requireNotNull(documentDirectory).path + "/$DATABASE_FILE_NAME"
    ).buildJantaNiveshDatabase()
}
