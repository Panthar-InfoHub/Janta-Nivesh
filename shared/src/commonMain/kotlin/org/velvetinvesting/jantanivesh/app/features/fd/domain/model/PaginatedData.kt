package org.velvetinvesting.jantanivesh.app.features.fd.domain.model

data class PaginatedData<T>(
    val items: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Int,
    val totalPages: Int,
    val hasNextPage: Boolean
)
