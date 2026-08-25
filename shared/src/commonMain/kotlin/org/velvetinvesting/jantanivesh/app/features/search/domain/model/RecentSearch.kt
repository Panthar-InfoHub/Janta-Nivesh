package org.velvetinvesting.jantanivesh.app.features.search.domain.model

/** A term the user searched for, newest first wherever a list of these appears. */
data class RecentSearch(
    val query: String,
    val searchedAt: Long
)
