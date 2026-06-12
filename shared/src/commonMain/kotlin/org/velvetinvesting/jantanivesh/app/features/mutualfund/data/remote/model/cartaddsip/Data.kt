package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.cartaddsip

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val code: Int,
    val results: List<Result>
)