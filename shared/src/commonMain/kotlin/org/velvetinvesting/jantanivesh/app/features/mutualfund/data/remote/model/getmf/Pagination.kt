package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.getmf

import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    val limit: Int,
    val page: Int,
    val total: Int,
    val totalPages: Int
)