package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mffunds

import kotlinx.serialization.Serializable

/**
 * [search] echoes back the term the server actually filtered on, and is absent when the call
 * carried no query — every field is optional so a partial page still parses.
 */
@Serializable
data class MfFundsData(
    val search: String? = null,
    val funds: List<MfFundDto> = emptyList(),
    val pagination: MfFundsPagination? = null
)
