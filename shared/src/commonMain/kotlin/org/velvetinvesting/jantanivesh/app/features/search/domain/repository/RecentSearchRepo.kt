package org.velvetinvesting.jantanivesh.app.features.search.domain.repository

import kotlinx.coroutines.flow.Flow
import org.velvetinvesting.jantanivesh.app.features.search.domain.model.RecentSearch

/** Recent searches, held locally. Nothing here touches the network. */
interface RecentSearchRepo {

    fun observeRecentSearches(limit: Int = DEFAULT_LIMIT): Flow<List<RecentSearch>>

    /** Records a term, or moves it back to the top if it is already in the list. */
    suspend fun saveRecentSearch(query: String)

    suspend fun deleteRecentSearch(query: String)

    suspend fun clearRecentSearches()

    companion object {
        /** As many as the overlay card shows without scrolling on the smallest screen. */
        const val DEFAULT_LIMIT = 6
    }
}
