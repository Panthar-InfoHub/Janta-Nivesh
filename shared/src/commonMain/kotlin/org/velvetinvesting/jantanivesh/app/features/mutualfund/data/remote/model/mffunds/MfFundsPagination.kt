package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mffunds

import kotlinx.serialization.Serializable

@Serializable
data class MfFundsPagination(
    val page: Int = 1,
    val limit: Int = 0,
    val total: Int = 0,
    val total_pages: Int = 0
)
