package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundledfunds

import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    val limit: Int,
    val page: Int,
    val total: Int,
    val totalPages: Int
)