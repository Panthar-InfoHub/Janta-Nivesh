package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    val limit: Int,
    val page: Int,
    val total: Int,
    val totalPages: Int
)