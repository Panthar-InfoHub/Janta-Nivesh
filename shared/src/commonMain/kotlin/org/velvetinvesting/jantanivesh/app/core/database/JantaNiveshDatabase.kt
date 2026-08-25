package org.velvetinvesting.jantanivesh.app.core.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor

/** The app's local database. Everything on the server stays on the server; this is cache only. */
@Database(
    entities = [RecentSearchEntity::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(JantaNiveshDatabaseConstructor::class)
abstract class JantaNiveshDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
}

/**
 * KSP generates the `actual` for this on every target, which is why the body is empty and the
 * "no actual" warning is suppressed — the compiler cannot see the generated declarations yet.
 */
@Suppress("KotlinNoActualForExpect", "NO_ACTUAL_FOR_EXPECT")
expect object JantaNiveshDatabaseConstructor : RoomDatabaseConstructor<JantaNiveshDatabase> {
    override fun initialize(): JantaNiveshDatabase
}

internal const val DATABASE_FILE_NAME = "janta_nivesh.db"
