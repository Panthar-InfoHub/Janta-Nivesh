package org.velvetinvesting.jantanivesh.app.features.search.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.features.search.domain.model.RecentSearch
import org.velvetinvesting.jantanivesh.app.features.search.domain.usecases.ClearRecentSearchesUseCase
import org.velvetinvesting.jantanivesh.app.features.search.domain.usecases.DeleteRecentSearchUseCase
import org.velvetinvesting.jantanivesh.app.features.search.domain.usecases.ObserveRecentSearchesUseCase
import org.velvetinvesting.jantanivesh.app.features.search.domain.usecases.SaveRecentSearchUseCase

data class SearchOverlayUiState(
    val query: String = "",
    val recentSearches: List<RecentSearch> = emptyList()
) {
    val canSubmit: Boolean
        get() = query.isNotBlank()

    /** The recents card is hidden entirely rather than shown empty on a first run. */
    val showRecents: Boolean
        get() = recentSearches.isNotEmpty()
}

sealed interface SearchOverlayEvent {
    data class OnQueryChange(val query: String) : SearchOverlayEvent

    /** Keyboard search action, or the magnifier in the field. */
    data object OnSubmit : SearchOverlayEvent

    /** Tapping a recent term searches it immediately rather than only filling the field. */
    data class OnRecentClick(val query: String) : SearchOverlayEvent
    data class OnRecentDelete(val query: String) : SearchOverlayEvent
    data object OnClearRecents : SearchOverlayEvent
}

sealed interface SearchOverlayEffect {
    /** The term has already been recorded by the time this is emitted. */
    data class Search(val query: String) : SearchOverlayEffect
}

/**
 * Backs the search overlay: the field, the recent terms and what happens when one is run.
 *
 * The overlay does no searching of its own — it hands the term back through [effect] and the
 * caller navigates. All this owns is the recents list.
 */
class SearchOverlayViewModel(
    observeRecentSearches: ObserveRecentSearchesUseCase,
    private val saveRecentSearch: SaveRecentSearchUseCase,
    private val deleteRecentSearch: DeleteRecentSearchUseCase,
    private val clearRecentSearches: ClearRecentSearchesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchOverlayUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SearchOverlayEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            observeRecentSearches().collect { searches ->
                _uiState.update { it.copy(recentSearches = searches) }
            }
        }
    }

    fun handleEvent(event: SearchOverlayEvent) {
        when (event) {
            is SearchOverlayEvent.OnQueryChange ->
                _uiState.update { it.copy(query = event.query) }

            SearchOverlayEvent.OnSubmit -> submit(_uiState.value.query)

            is SearchOverlayEvent.OnRecentClick -> submit(event.query)

            is SearchOverlayEvent.OnRecentDelete -> viewModelScope.launch {
                deleteRecentSearch(event.query)
            }

            SearchOverlayEvent.OnClearRecents -> viewModelScope.launch {
                clearRecentSearches()
            }
        }
    }

    /**
     * Records the term first, then hands it on. Saving before navigating means a term is in the
     * list even if the user comes straight back — the recents list tracks what was searched, not
     * what the search returned.
     */
    private fun submit(query: String) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return

        viewModelScope.launch {
            saveRecentSearch(trimmed)
            _effect.send(SearchOverlayEffect.Search(trimmed))
        }
    }

    /** Clears the field so reopening the overlay does not start on the previous term. */
    fun resetQuery() {
        _uiState.update { it.copy(query = "") }
    }
}
