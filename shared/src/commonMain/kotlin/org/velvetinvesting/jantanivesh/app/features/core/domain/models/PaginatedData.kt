package org.velvetinvesting.jantanivesh.app.features.core.domain.models

data class PaginatedData<T>(
    val items: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Int,
    val totalPages: Int,
    val hasNextPage: Boolean
)