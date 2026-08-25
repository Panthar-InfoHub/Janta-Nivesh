package org.velvetinvesting.jantanivesh.app.features.search.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.velvetinvesting.jantanivesh.app.core.database.RecentSearchDao
import org.velvetinvesting.jantanivesh.app.core.database.RecentSearchEntity
import org.velvetinvesting.jantanivesh.app.features.search.domain.model.RecentSearch
import org.velvetinvesting.jantanivesh.app.features.search.domain.repository.RecentSearchRepo
import kotlin.time.Clock

class RecentSearchRepoImpl(
    private val dao: RecentSearchDao
) : RecentSearchRepo {

    override fun observeRecentSearches(limit: Int): Flow<List<RecentSearch>> =
        dao.observeRecentSearches(limit).map { rows -> rows.map { it.toDomain() } }

    /**
     * The term is trimmed before it is stored, so " gold etf " and "gold etf" are the same row
     * rather than two. Case is left alone — the list reads back as the user typed it.
     */
    override suspend fun saveRecentSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return

        dao.save(
            RecentSearchEntity(
                query = trimmed,
                searchedAt = Clock.System.now().toEpochMilliseconds()
            )
        )
        dao.trimTo(MAX_STORED_SEARCHES)
    }

    override suspend fun deleteRecentSearch(query: String) = dao.delete(query.trim())

    override suspend fun clearRecentSearches() = dao.clear()

    private fun RecentSearchEntity.toDomain() = RecentSearch(
        query = query,
        searchedAt = searchedAt
    )

    private companion object {
        /** More than the overlay shows, so the list survives a few searches being cleared. */
        const val MAX_STORED_SEARCHES = 20
    }
}
