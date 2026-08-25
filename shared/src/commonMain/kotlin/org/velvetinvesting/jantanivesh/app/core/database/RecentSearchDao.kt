package org.velvetinvesting.jantanivesh.app.core.database

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    /** Newest first. A Flow so the overlay updates the moment a term is added or removed. */
    @Query(
        "SELECT * FROM ${RecentSearchEntity.TABLE_NAME} " +
                "ORDER BY searchedAt DESC LIMIT :limit"
    )
    fun observeRecentSearches(limit: Int): Flow<List<RecentSearchEntity>>

    /** Insert, or bump an existing term's timestamp so it moves back to the top. */
    @Upsert
    suspend fun save(search: RecentSearchEntity)

    @Query("DELETE FROM ${RecentSearchEntity.TABLE_NAME} WHERE `query` = :query")
    suspend fun delete(query: String)

    @Query("DELETE FROM ${RecentSearchEntity.TABLE_NAME}")
    suspend fun clear()

    /**
     * Drops everything past the newest [limit] rows, so the table cannot grow without bound. Run
     * after each save rather than on a schedule — one search can only ever push out one term.
     */
    @Query(
        "DELETE FROM ${RecentSearchEntity.TABLE_NAME} WHERE `query` NOT IN (" +
                "SELECT `query` FROM ${RecentSearchEntity.TABLE_NAME} " +
                "ORDER BY searchedAt DESC LIMIT :limit)"
    )
    suspend fun trimTo(limit: Int)
}
