package org.velvetinvesting.jantanivesh.app.features.search.domain.usecases

import kotlinx.coroutines.flow.Flow
import org.velvetinvesting.jantanivesh.app.features.search.domain.model.RecentSearch
import org.velvetinvesting.jantanivesh.app.features.search.domain.repository.RecentSearchRepo

class ObserveRecentSearchesUseCase(
    private val repo: RecentSearchRepo
) {
    operator fun invoke(
        limit: Int = RecentSearchRepo.DEFAULT_LIMIT
    ): Flow<List<RecentSearch>> = repo.observeRecentSearches(limit)
}

class SaveRecentSearchUseCase(
    private val repo: RecentSearchRepo
) {
    suspend operator fun invoke(query: String) = repo.saveRecentSearch(query)
}

class DeleteRecentSearchUseCase(
    private val repo: RecentSearchRepo
) {
    suspend operator fun invoke(query: String) = repo.deleteRecentSearch(query)
}

class ClearRecentSearchesUseCase(
    private val repo: RecentSearchRepo
) {
    suspend operator fun invoke() = repo.clearRecentSearches()
}
