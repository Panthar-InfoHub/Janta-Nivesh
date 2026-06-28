package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.fdredirect

import kotlinx.serialization.Serializable

@Serializable
data class RedirectBody(
    val fd_trans_id: String,
    val event: String
)
